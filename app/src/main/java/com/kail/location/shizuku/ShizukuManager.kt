package com.kail.location.shizuku

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PersistableBundle
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.kail.location.models.SimCardInfo
import com.kail.location.utils.KailLog
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper
import java.io.File

/**
 * Shizuku管理器 - 非Root模式核心组件
 * 负责Shizuku连接、权限验证和Binder交互
 */
object ShizukuManager {

    private const val TAG = "ShizukuManager"

    /**
     * 检查Shizuku是否可用
     */
    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.getBinder() != null
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Shizuku not available: ${e.message}")
            false
        }
    }

    /**
     * 检查是否有Shizuku权限
     */
    fun hasPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Permission check failed: ${e.message}")
            false
        }
    }

    /**
     * 请求Shizuku权限
     * @param context 上下文
     * @param requestCode 请求码
     */
    fun requestPermission(context: Context, requestCode: Int) {
        try {
            Shizuku.requestPermission(requestCode)
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Request permission failed: ${e.message}")
        }
    }

    /**
     * 获取Shizuku版本
     */
    fun getShizukuVersion(): String {
        return try {
            Shizuku.getBinder()?.interfaceDescriptor ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    /**
     * 检查Shizuku是否正在运行
     */
    fun isShizukuRunning(): Boolean {
        return try {
            val binder = Shizuku.getBinder()
            binder != null && binder.isBinderAlive
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 添加Binder接收监听器
     */
    fun addBinderReceivedListener(listener: () -> Unit) {
        Shizuku.addBinderReceivedListenerSticky {
            listener()
        }
    }

    /**
     * 添加权限请求结果监听器
     */
    fun addPermissionResultListener(listener: (Int, Int) -> Unit) {
        Shizuku.addRequestPermissionResultListener { requestCode, grantResult ->
            listener(requestCode, grantResult)
        }
    }

    /**
     * 移除所有监听器
     */
    fun cleanup() {
        try {
            Shizuku.removeBinderReceivedListener { }
            Shizuku.removeRequestPermissionResultListener { _, _ -> }
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Cleanup failed: ${e.message}")
        }
    }

    /**
     * 通过Shizuku执行shell命令
     */
    fun executeShellCommand(command: String): String? {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val reader = process.inputStream.bufferedReader()
            val result = reader.readText()
            reader.close()
            process.destroy()
            result
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Execute shell failed: ${e.message}")
            null
        }
    }

    /**
     * 获取Shizuku信息摘要
     */
    fun getSummary(): Map<String, Any> {
        return mapOf(
            "available" to isShizukuAvailable(),
            "running" to isShizukuRunning(),
            "permission" to hasPermission(),
            "version" to getShizukuVersion()
        )
    }
}
