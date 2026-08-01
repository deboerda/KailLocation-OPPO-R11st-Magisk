package com.kail.location.views.cellsimulation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kail.location.shizuku.CarrierConfigManager
import com.kail.location.shizuku.ShizukuManager
import com.kail.location.views.theme.locationTheme

/**
 * 非Root模式基站模拟Activity
 * 使用Shizuku实现基站信息伪造
 */
class NonRootCellSimulationActivity : ComponentActivity() {
    
    private var isShizukuAvailable by mutableStateOf(false)
    private var hasPermission by mutableStateOf(false)
    private var selectedCountryCode by mutableStateOf<String?>(null)
    private var selectedCarrierName by mutableStateOf<String?>(null)
    private var isLoading by mutableStateOf(false)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 检查Shizuku状态
        checkShizukuStatus()
        
        // 添加监听器
        setupListeners()
        
        setContent {
            locationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NonRootCellSimulationScreen(
                        isShizukuAvailable = isShizukuAvailable,
                        hasPermission = hasPermission,
                        selectedCountryCode = selectedCountryCode,
                        selectedCarrierName = selectedCarrierName,
                        isLoading = isLoading,
                        onRequestPermission = { requestShizukuPermission() },
                        onCountrySelected = { selectedCountryCode = it },
                        onCarrierSelected = { selectedCarrierName = it },
                        onApplyConfig = { applyCarrierConfig() },
                        onResetConfig = { resetCarrierConfig() },
                        onRefresh = { refreshStatus() }
                    )
                }
            }
        }
    }
    
    private fun checkShizukuStatus() {
        isShizukuAvailable = ShizukuManager.isShizukuAvailable()
        hasPermission = ShizukuManager.hasPermission()
        
        if (!isShizukuAvailable) {
            Toast.makeText(this, "请安装并启用Shizuku", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun setupListeners() {
        ShizukuManager.addBinderReceivedListener {
            runOnUiThread {
                checkShizukuStatus()
            }
        }
        
        ShizukuManager.addPermissionResultListener { _, grantResult ->
            runOnUiThread {
                hasPermission = grantResult == PackageManager.PERMISSION_GRANTED
                if (hasPermission) {
                    Toast.makeText(this, "Shizuku权限已授予", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "需要Shizuku权限才能使用此功能", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun requestShizukuPermission() {
        if (isShizukuAvailable) {
            ShizukuManager.requestPermission(this, 0)
        } else {
            Toast.makeText(this, "Shizuku未就绪", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun applyCarrierConfig() {
        if (!hasPermission) {
            Toast.makeText(this, "需要Shizuku权限", Toast.LENGTH_SHORT).show()
            return
        }
        
        isLoading = true
        try {
            // 获取第一个SIM卡的subId
            val simCards = CarrierConfigManager.getSimCards(this)
            if (simCards.isEmpty()) {
                Toast.makeText(this, "未检测到SIM卡", Toast.LENGTH_SHORT).show()
                return
            }
            
            val subId = simCards.first().subId
            
            // 设置运营商配置
            CarrierConfigManager.setCarrierConfig(
                subId = subId,
                countryCode = selectedCountryCode,
                carrierName = selectedCarrierName
            )
            
            Toast.makeText(this, "基站模拟已启用", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "设置失败: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }
    
    private fun resetCarrierConfig() {
        if (!hasPermission) {
            Toast.makeText(this, "需要Shizuku权限", Toast.LENGTH_SHORT).show()
            return
        }
        
        isLoading = true
        try {
            val simCards = CarrierConfigManager.getSimCards(this)
            if (simCards.isEmpty()) {
                Toast.makeText(this, "未检测到SIM卡", Toast.LENGTH_SHORT).show()
                return
            }
            
            val subId = simCards.first().subId
            CarrierConfigManager.resetCarrierConfig(subId)
            
            Toast.makeText(this, "基站模拟已重置", Toast.LENGTH_SHORT).show()
            selectedCountryCode = null
            selectedCarrierName = null
        } catch (e: Exception) {
            Toast.makeText(this, "重置失败: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }
    
    private fun refreshStatus() {
        checkShizukuStatus()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        ShizukuManager.cleanup()
    }
}

@Composable
fun NonRootCellSimulationScreen(
    isShizukuAvailable: Boolean,
    hasPermission: Boolean,
    selectedCountryCode: String?,
    selectedCarrierName: String?,
    isLoading: Boolean,
    onRequestPermission: () -> Unit,
    onCountrySelected: (String?) -> Unit,
    onCarrierSelected: (String?) -> Unit,
    onApplyConfig: () -> Unit,
    onResetConfig: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "非Root模式基站模拟",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Shizuku状态卡片
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Shizuku状态",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("可用:")
                    Text(
                        text = if (isShizukuAvailable) "✓" else "✗",
                        color = if (isShizukuAvailable) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("权限:")
                    Text(
                        text = if (hasPermission) "已授权" else "未授权",
                        color = if (hasPermission) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
                }
                
                if (!hasPermission) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onRequestPermission,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("请求Shizuku权限")
                    }
                }
                
                if (hasPermission) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onRefresh,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("刷新状态")
                    }
                }
            }
        }
        
        if (hasPermission) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // 配置选项
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "基站配置",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 国家码输入
                    OutlinedTextField(
                        value = selectedCountryCode ?: "",
                        onValueChange = { 
                            if (it.length <= 2) {
                                onCountrySelected(it.uppercase())
                            }
                        },
                        label = { Text("国家码 (如CN, US)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 运营商名称输入
                    OutlinedTextField(
                        value = selectedCarrierName ?: "",
                        onValueChange = { onCarrierSelected(it) },
                        label = { Text("运营商名称") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 操作按钮
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onApplyConfig,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading && !selectedCountryCode.isNullOrEmpty()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("应用")
                            }
                        }
                        
                        OutlinedButton(
                            onClick = onResetConfig,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            Text("重置")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 说明文字
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "说明",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "• 此功能需要Shizuku并授予权限\n" +
                               "• 通过修改运营商配置实现基站模拟\n" +
                               "• 仅影响部分应用的基站检测\n" +
                               "• 部分应用可能检测真实基站",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            
            // 提示信息
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "使用说明",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "1. 安装Shizuku应用\n" +
                               "2. 在Shizuku中启用开发者模式\n" +
                               "3. 返回此页面并授予权限\n" +
                               "4. 即可使用基站模拟功能",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
