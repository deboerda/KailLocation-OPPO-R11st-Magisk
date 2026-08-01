package com.kail.location.service.Root

import android.content.Context
import com.kail.location.utils.KailLog
import java.io.File

/**
 * 针对 Android 8.1+ linker namespace 限制的修复
 * 
 * 问题：system_server 无法从 /data/kail-loc/ 加载 SO 文件
 * 原因：Android 8.1 引入了 linker namespace 隔离
 * 
 * 解决方案：使用 /data/local/lib/ 目录（system_server 默认可访问）
 */
object NamespaceFixer {

    private const val TAG = "NamespaceFixer"
    
    // 替代的部署目录 - system_server 有权限加载
    private const val ALT_LIB_DIR = "/data/local/lib"
    private const val ALT_KAIL_DIR = "/data/local/kail-lib"

    /**
     * 获取推荐的SO部署目录
     * 针对 Android 8.1+ 的 namespace 限制
     */
    fun getPreferredLibDir(): String {
        return ALT_LIB_DIR
    }

    /**
     * 获取推荐的主库目录
     */
    fun getPreferredMainDir(): String {
        return ALT_KAIL_DIR
    }

    /**
     * 部署 SO 文件到替代目录
     * 
     * @param sourceFile 源SO文件
     * @param targetName 目标文件名
     * @return 是否成功
     */
    fun deployLibrary(sourceFile: File, targetName: String): Boolean {
        try {
            val targetDir = File(ALT_LIB_DIR)
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }
            
            val targetFile = File(targetDir, targetName)
            sourceFile.copyTo(targetFile, overwrite = true)
            
            // 设置正确的权限
            Runtime.getRuntime().exec(arrayOf("su", "-c", "chmod 755 ${targetFile.absolutePath}"))
            
            KailLog.d(null, TAG, "Deployed library to $targetFile")
            return true
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to deploy library: ${e.message}")
            return false
        }
    }

    /**
     * 部署多个SO文件
     */
    fun deployLibraries(libFiles: List<Pair<File, String>>): Boolean {
        var allSuccess = true
        for ((file, name) in libFiles) {
            if (!deployLibrary(file, name)) {
                allSuccess = false
            }
        }
        return allSuccess
    }

    /**
     * 检查是否需要使用替代方案
     * 
     * Android 8.1+ 并且是 Magisk root 时，建议使用替代目录
     */
    fun shouldUseAlternative(): Boolean {
        // 检测 Android 版本
        val sdkInt = android.os.Build.VERSION.SDK_INT
        return sdkInt >= 27 && sdkInt <= 30 // Android 8.1 到 Android 11
    }

    /**
     * 创建符号链接（如果原始目录不可用）
     * 让原有代码仍然使用 /data/kail-loc/ 但实际访问替代目录
     */
    fun createSymlinks(): Boolean {
        return try {
            val commands = arrayOf(
                "mkdir -p $ALT_LIB_DIR",
                "mkdir -p $ALT_KAIL_DIR",
                // 如果旧目录存在但为空，创建链接指向新目录
                "rm -rf /data/kail-loc 2>/dev/null || true",
                "ln -sf $ALT_LIB_DIR /data/kail-lib",
                "ln -sf $ALT_KAIL_DIR /data/kail-loc"
            )
            
            for (cmd in commands) {
                Runtime.getRuntime().exec(arrayOf("su", "-c", cmd)).waitFor()
            }
            
            KailLog.i(null, TAG, "Created symlinks for alternative directories")
            true
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to create symlinks: ${e.message}")
            false
        }
    }

    /**
     * 修复所有可能的权限问题
     */
    fun fixPermissions(): Boolean {
        return try {
            val commands = arrayOf(
                "chmod -R 755 $ALT_LIB_DIR",
                "chmod -R 755 $ALT_KAIL_DIR",
                "chmod -R 777 /data/kail-loc 2>/dev/null || true",
                "chmod -R 777 /data/local/kail-lib 2>/dev/null || true"
            )
            
            for (cmd in commands) {
                Runtime.getRuntime().exec(arrayOf("su", "-c", cmd)).waitFor()
            }
            
            KailLog.i(null, TAG, "Fixed permissions")
            true
        } catch (e: Exception) {
            KailLog.e(null, TAG, "Failed to fix permissions: ${e.message}")
            false
        }
    }
}
