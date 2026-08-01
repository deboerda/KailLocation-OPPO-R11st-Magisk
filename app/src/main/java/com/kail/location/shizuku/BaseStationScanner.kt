package com.kail.location.shizuku

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrengthLte
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.kail.location.models.CellInfo
import com.kail.location.utils.KailLog
import java.util.UUID

/**
 * 基站扫描和搜索管理器
 * 
 * 功能：
 * 1. 扫描当前设备连接的基站
 * 2. 通过OpenCellID API搜索附近基站
 * 3. 获取当前位置信息
 */
object BaseStationScanner {

    private const val TAG = "BaseStationScanner"

    /**
     * 扫描当前设备连接的基站
     * 
     * @param context 上下文
     * @return 基站列表
     */
    fun scanCurrentCells(context: Context): List<CellInfo> {
        if (!hasLocationPermission(context)) {
            KailLog.w(null, TAG, "No location permission")
            return emptyList()
        }

        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        if (telephonyManager == null) {
            KailLog.w(null, TAG, "TelephonyManager not available")
            return emptyList()
        }

        try {
            val cellInfos = telephonyManager.allCellInfo
            if (cellInfos.isNullOrEmpty()) {
                KailLog.w(null, TAG, "No cell info available")
                return emptyList()
            }

            // 获取当前位置作为基站位置参考
            val (lat, lng) = getCurrentLocation(context)

            val results = mutableListOf<CellInfo>()

            for (cellInfo in cellInfos) {
                if (!cellInfo.isRegistered) continue

                val cell = when (cellInfo) {
                    is CellInfoLte -> parseLteCell(cellInfo, lat, lng)
                    is CellInfoGsm -> parseGsmCell(cellInfo, lat, lng)
                    is CellInfoWcdma -> parseWcdmaCell(cellInfo, lat, lng)
                    is CellInfoCdma -> parseCdmaCell(cellInfo, lat, lng)
                    else -> null
                }

                cell?.let { results.add(it) }
            }

            KailLog.i(null, TAG, "Scanned ${results.size} cells")
            return results

        } catch (e: Exception) {
            KailLog.e(null, TAG, "Scan failed: ${e.message}")
            return emptyList()
        }
    }

    private fun parseLteCell(cellInfo: CellInfoLte, defaultLat: Double, defaultLng: Double): CellInfo? {
        return try {
            val identity = cellInfo.cellIdentity
            val signal = cellInfo.cellSignalStrength

            CellInfo(
                id = UUID.randomUUID().toString(),
                networkType = "LTE",
                mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mccString?.toIntOrNull() ?: 460
                } else {
                    @Suppress("DEPRECATION")
                    identity.mcc.takeIf { it != Integer.MAX_VALUE } ?: 460
                },
                mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mncString?.toIntOrNull() ?: 0
                } else {
                    @Suppress("DEPRECATION")
                    identity.mnc.takeIf { it != Integer.MAX_VALUE } ?: 0
                },
                lac = identity.tac.takeIf { it != Integer.MAX_VALUE } ?: 0,
                cid = identity.ci.takeIf { it != Integer.MAX_VALUE }?.toLong() ?: 0L,
                psc = identity.pci.takeIf { it != Integer.MAX_VALUE } ?: 0,
                latitude = defaultLat,
                longitude = defaultLng,
                radius = 1000f
            )
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Parse LTE cell failed: ${e.message}")
            null
        }
    }

    private fun parseGsmCell(cellInfo: CellInfoGsm, defaultLat: Double, defaultLng: Double): CellInfo? {
        return try {
            val identity = cellInfo.cellIdentity

            CellInfo(
                id = UUID.randomUUID().toString(),
                networkType = "GSM",
                mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mccString?.toIntOrNull() ?: 460
                } else {
                    @Suppress("DEPRECATION")
                    identity.mcc.takeIf { it != Integer.MAX_VALUE } ?: 460
                },
                mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mncString?.toIntOrNull() ?: 0
                } else {
                    @Suppress("DEPRECATION")
                    identity.mnc.takeIf { it != Integer.MAX_VALUE } ?: 0
                },
                lac = identity.lac.takeIf { it != Integer.MAX_VALUE } ?: 0,
                cid = identity.cid.takeIf { it != Integer.MAX_VALUE }?.toLong() ?: 0L,
                psc = 0,
                latitude = defaultLat,
                longitude = defaultLng,
                radius = 1000f
            )
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Parse GSM cell failed: ${e.message}")
            null
        }
    }

    private fun parseWcdmaCell(cellInfo: CellInfoWcdma, defaultLat: Double, defaultLng: Double): CellInfo? {
        return try {
            val identity = cellInfo.cellIdentity

            CellInfo(
                id = UUID.randomUUID().toString(),
                networkType = "WCDMA",
                mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mccString?.toIntOrNull() ?: 460
                } else {
                    @Suppress("DEPRECATION")
                    identity.mcc.takeIf { it != Integer.MAX_VALUE } ?: 460
                },
                mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    identity.mncString?.toIntOrNull() ?: 0
                } else {
                    @Suppress("DEPRECATION")
                    identity.mnc.takeIf { it != Integer.MAX_VALUE } ?: 0
                },
                lac = identity.lac.takeIf { it != Integer.MAX_VALUE } ?: 0,
                cid = identity.cid.takeIf { it != Integer.MAX_VALUE }?.toLong() ?: 0L,
                psc = identity.psc.takeIf { it != Integer.MAX_VALUE } ?: 0,
                latitude = defaultLat,
                longitude = defaultLng,
                radius = 1000f
            )
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Parse WCDMA cell failed: ${e.message}")
            null
        }
    }

    private fun parseCdmaCell(cellInfo: CellInfoCdma, defaultLat: Double, defaultLng: Double): CellInfo? {
        return try {
            val identity = cellInfo.cellIdentity

            CellInfo(
                id = UUID.randomUUID().toString(),
                networkType = "CDMA",
                mcc = 460,
                mnc = identity.systemId.takeIf { it != Integer.MAX_VALUE } ?: 0,
                lac = identity.networkId.takeIf { it != Integer.MAX_VALUE } ?: 0,
                cid = identity.basestationId.takeIf { it != Integer.MAX_VALUE }?.toLong() ?: 0L,
                psc = 0,
                latitude = defaultLat,
                longitude = defaultLng,
                radius = 1000f
            )
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Parse CDMA cell failed: ${e.message}")
            null
        }
    }

    /**
     * 获取当前位置
     */
    fun getCurrentLocation(context: Context): Pair<Double, Double> {
        try {
            if (!hasLocationPermission(context)) {
                return Pair(0.0, 0.0)
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            
            // 尝试GPS
            if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLocation != null) {
                    return Pair(gpsLocation.latitude, gpsLocation.longitude)
                }
            }

            // 尝试网络定位
            if (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLocation != null) {
                    return Pair(networkLocation.latitude, networkLocation.longitude)
                }
            }

        } catch (_: Exception) {
        }

        return Pair(0.0, 0.0)
    }

    /**
     * 检查是否有定位权限
     */
    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 获取扫描结果摘要
     */
    fun getScanSummary(cells: List<CellInfo>): Map<String, Any> {
        val networkTypes = cells.groupBy { it.networkType }
        
        return mapOf(
            "totalCells" to cells.size,
            "networkTypes" to networkTypes.keys.toList(),
            "cellsByType" to networkTypes.mapValues { it.value.size },
            "sampleCells" to cells.take(3).map { 
                mapOf(
                    "type" to it.networkType,
                    "lac" to it.lac,
                    "cid" to it.cid,
                    "mcc" to it.mcc,
                    "mnc" to it.mnc
                )
            }
        )
    }
}
