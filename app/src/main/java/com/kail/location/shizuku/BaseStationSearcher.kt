package com.kail.location.shizuku

import android.content.Context
import com.kail.location.models.CellInfo
import com.kail.location.network.OpenCellIdClient
import com.kail.location.utils.KailLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 基站网络搜索器
 * 使用OpenCellID API搜索附近基站
 */
object BaseStationSearcher {

    private const val TAG = "BaseStationSearcher"

    // 用户提供的API Key
    // 注意：在实际应用中，应该将API Key存储在安全的地方（如SharedPreferences）
    private var userApiKey: String = ""

    /**
     * 设置用户的API Key
     */
    fun setApiKey(key: String) {
        userApiKey = key
        KailLog.i(null, TAG, "API Key configured")
    }

    /**
     * 获取当前API Key
     */
    fun getApiKey(): String = userApiKey

    /**
     * 是否已配置API Key
     */
    fun hasApiKey(): Boolean = userApiKey.isNotBlank()

    /**
     * 搜索指定位置附近的基站
     * 
     * @param context 上下文
     * @param lat 纬度
     * @param lon 经度
     * @param radiusKm 搜索半径（公里）
     * @return 基站列表
     */
    suspend fun searchNearbyCells(
        context: Context,
        lat: Double,
        lon: Double,
        radiusKm: Double = 0.5
    ): List<CellInfo> = withContext(Dispatchers.IO) {
        if (!hasApiKey()) {
            KailLog.w(null, TAG, "No API Key configured")
            return@withContext emptyList()
        }

        if (lat == 0.0 && lon == 0.0) {
            KailLog.w(null, TAG, "Invalid coordinates")
            return@withContext emptyList()
        }

        try {
            KailLog.i(null, TAG, "Searching cells at lat=$lat, lon=$lon, radius=$radiusKm km")
            
            val cells = OpenCellIdClient.fetchCellsInArea(
                apiKey = userApiKey,
                lat = lat,
                lon = lon,
                radiusKm = radiusKm
            )

            KailLog.i(null, TAG, "Found ${cells.size} cells from OpenCellID")
            cells
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Search failed: ${e.message}")
            emptyList()
        }
    }

    /**
     * 搜索当前GPS位置附近的基站
     */
    suspend fun searchCellsAtCurrentLocation(context: Context, radiusKm: Double = 0.5): List<CellInfo> {
        val location = BaseStationScanner.getCurrentLocation(context)
        if (location.first == 0.0 && location.second == 0.0) {
            KailLog.w(null, TAG, "Cannot get current location")
            return emptyList()
        }
        return searchNearbyCells(context, location.first, location.second, radiusKm)
    }

    /**
     * 测试API Key是否有效
     */
    suspend fun testApiKey(key: String): Boolean = withContext(Dispatchers.IO) {
        OpenCellIdClient.testApiKey(key)
    }
}
