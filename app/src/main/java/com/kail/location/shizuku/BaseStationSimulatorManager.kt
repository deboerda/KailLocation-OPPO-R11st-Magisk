package com.kail.location.shizuku

import android.content.Context
import android.os.Build
import com.kail.location.models.CellInfo
import com.kail.location.utils.KailLog
import rikka.shizuku.Shizuku

/**
 * 基站模拟管理器 - 非Root模式
 * 
 * 核心原理：
 * 通过Shizuku调用系统API修改基站信息，而非真正修改硬件
 * 
 * 技术方案：
 * 1. CarrierConfigManager - 修改运营商国家码/名称（已实现）
 * 2. 尝试通过反射/HiddenAPI访问TelephonyRegistry
 * 
 * 注意：完全伪造基站位置(LAC/CID)需要系统签名或Root权限
 * 本模块提供UI入口和配置存储，实际生效需要进一步集成
 */
object BaseStationSimulatorManager {

    private const val TAG = "BaseStationSimulator"

    /**
     * 当前模拟的基站列表
     */
    private val simulatedCells = mutableListOf<CellInfo>()

    /**
     * 是否正在模拟
     */
    private var isSimulating = false

    /**
     * 添加基站到模拟列表
     */
    fun addSimulatedCell(cell: CellInfo) {
        simulatedCells.add(cell)
        KailLog.i(null, TAG, "Added cell: ${cell.lac}/${cell.cid} at ${cell.latitude},${cell.longitude}")
    }

    /**
     * 移除基站
     */
    fun removeSimulatedCell(index: Int) {
        if (index in simulatedCells.indices) {
            simulatedCells.removeAt(index)
        }
    }

    /**
     * 获取所有模拟的基站
     */
    fun getSimulatedCells(): List<CellInfo> = simulatedCells.toList()

    /**
     * 清除所有模拟基站
     */
    fun clearSimulatedCells() {
        simulatedCells.clear()
        KailLog.i(null, TAG, "Cleared all simulated cells")
    }

    /**
     * 启动基站模拟
     * 
     * 非Root模式下的工作原理：
     * 1. 通过Shizuku获得临时系统权限
     * 2. 尝试修改Telephony服务中的基站数据
     * 3. 利用现有的CellInfoFactory生成伪造的CellInfo对象
     * 
     * 注意：实际生效程度取决于设备和系统版本
     */
    fun startSimulation(): Boolean {
        if (simulatedCells.isEmpty()) {
            KailLog.w(null, TAG, "No cells to simulate")
            return false
        }

        if (!ShizukuManager.isShizukuAvailable() || !ShizukuManager.hasPermission()) {
            KailLog.w(null, TAG, "Shizuku not available or no permission")
            return false
        }

        isSimulating = true
        
        // TODO: 这里可以添加通过Shizuku调用系统服务的代码
        // 例如：通过反射调用ITelephonyRegistry的某些方法
        
        KailLog.i(null, TAG, "Starting base station simulation with ${simulatedCells.size} cells")
        
        // 通知系统基站数据已更新
        notifyCellUpdate()
        
        return true
    }

    /**
     * 停止基站模拟
     */
    fun stopSimulation() {
        isSimulating = false
        simulatedCells.clear()
        notifyCellUpdate()
        KailLog.i(null, TAG, "Stopped base station simulation")
    }

    /**
     * 检查是否正在模拟
     */
    fun isSimulating(): Boolean = isSimulating

    /**
     * 通知系统基站数据已更新
     * 通过广播或直接调用系统服务
     */
    private fun notifyCellUpdate() {
        // 尝试通过Shizuku发送基站更新广播
        try {
            if (ShizukuManager.isShizukuAvailable()) {
                // 发送ACTION_PHONE_STATE_CHANGED广播
                val command = """
                    am broadcast -a android.intent.action.PHONE_STATE 
                    --ef android_extra_phone_state 1
                """.trimIndent()
                ShizukuManager.executeShellCommand(command)
            }
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to notify cell update: ${e.message}")
        }
    }

    /**
     * 获取模拟状态摘要
     */
    fun getSimulationSummary(): Map<String, Any> {
        return mapOf(
            "simulating" to isSimulating,
            "cellCount" to simulatedCells.size,
            "shizukuAvailable" to ShizukuManager.isShizukuAvailable(),
            "hasPermission" to ShizukuManager.hasPermission(),
            "cells" to simulatedCells.map { 
                mapOf(
                    "lac" to it.lac,
                    "cid" to it.cid,
                    "mcc" to it.mcc,
                    "mnc" to it.mnc,
                    "networkType" to it.networkType,
                    "lat" to it.latitude,
                    "lng" to it.longitude
                )
            }
        )
    }

    /**
     * 预定义的示例基站数据
     * 用于快速测试
     */
    object SampleBaseStations {
        // 武汉东站附近基站示例
        val wuhanEastStation = CellInfo(
            id = "wuhan_east_1",
            networkType = "LTE",
            mcc = 460,
            mnc = 11,
            lac = 24365,
            cid = 16777215,
            psc = 0,
            latitude = 30.5456,
            longitude = 114.3544,
            radius = 500f
        )

        // 武汉站示例
        val wuhanStation = CellInfo(
            id = "wuhan_station_1",
            networkType = "LTE",
            mcc = 460,
            mnc = 11,
            lac = 24365,
            cid = 16777216,
            psc = 0,
            latitude = 30.5986,
            longitude = 114.3058,
            radius = 800f
        )

        // 更多示例
        fun getSamples(): List<CellInfo> = listOf(wuhanEastStation, wuhanStation)
    }
}
