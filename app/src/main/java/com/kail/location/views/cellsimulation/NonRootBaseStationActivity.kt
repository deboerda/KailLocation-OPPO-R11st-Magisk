package com.kail.location.views.cellsimulation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.kail.location.models.CellInfo
import com.kail.location.network.OpenCellIdClient
import com.kail.location.shizuku.BaseStationScanner
import com.kail.location.shizuku.BaseStationSearcher
import com.kail.location.shizuku.BaseStationSimulatorManager
import com.kail.location.shizuku.ShizukuManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 非Root模式 - 具体基站模拟界面
 * 增加了扫描和搜索功能
 * 
 * 用户API Token: pk.efbca2b88ac29cdeddd53e557214b40d
 */
class NonRootBaseStationActivity : ComponentActivity() {

    companion object {
        // 用户提供的OpenCellID API Key
        const val USER_API_KEY = "pk.efbca2b88ac29cdeddd53e557214b40d"
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                Toast.makeText(this, "已获得精确定位权限", Toast.LENGTH_SHORT).show()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                Toast.makeText(this, "已获得粗略定位权限", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "需要定位权限才能扫描基站", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 配置用户API Key
        BaseStationSearcher.setApiKey(USER_API_KEY)
        
        setContent {
            NonRootBaseStationScreen(
                onRequestPermission = {
                    requestLocationPermission()
                },
                onScanCells = { context ->
                    scanNearbyCells(context)
                },
                onSearchCells = { lat, lon, radius ->
                    searchCellsFromNetwork(lat, lon, radius)
                },
                onAddCell = { cell -> 
                    BaseStationSimulatorManager.addSimulatedCell(cell)
                },
                onRemoveCell = { index ->
                    BaseStationSimulatorManager.removeSimulatedCell(index)
                },
                onStartSimulation = {
                    BaseStationSimulatorManager.startSimulation()
                },
                onStopSimulation = {
                    BaseStationSimulatorManager.stopSimulation()
                },
                onClearAll = {
                    BaseStationSimulatorManager.clearSimulatedCells()
                },
                getSimulatedCells = {
                    BaseStationSimulatorManager.getSimulatedCells()
                },
                isSimulating = {
                    BaseStationSimulatorManager.isSimulating()
                },
                isShizukuReady = {
                    ShizukuManager.isShizukuAvailable() && ShizukuManager.hasPermission()
                },
                hasLocationPermission = {
                    hasLocationPermission()
                },
                apiKeyConfigured = {
                    BaseStationSearcher.hasApiKey()
                }
            )
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun scanNearbyCells(context: android.content.Context): List<CellInfo> {
        return BaseStationScanner.scanCurrentCells(context)
    }

    private suspend fun searchCellsFromNetwork(lat: Double, lon: Double, radius: Double): List<CellInfo> {
        return BaseStationSearcher.searchNearbyCells(this, lat, lon, radius)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonRootBaseStationScreen(
    onRequestPermission: () -> Unit,
    onScanCells: (android.content.Context) -> List<CellInfo>,
    onSearchCells: suspend (Double, Double, Double) -> List<CellInfo>,
    onAddCell: (CellInfo) -> Unit,
    onRemoveCell: (Int) -> Unit,
    onStartSimulation: () -> Unit,
    onStopSimulation: () -> Unit,
    onClearAll: () -> Unit,
    getSimulatedCells: () -> List<CellInfo>,
    isSimulating: () -> Boolean,
    isShizukuReady: () -> Boolean,
    hasLocationPermission: () -> Boolean,
    apiKeyConfigured: () -> Boolean
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var showSampleDialog by remember { mutableStateOf(false) }
    var showScanDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var scannedCells by remember { mutableStateOf<List<CellInfo>>(emptyList()) }
    var isScanning by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    
    val cells = remember { mutableStateListOf<CellInfo>() }
    
    // 刷新列表
    LaunchedEffect(Unit) {
        cells.clear()
        cells.addAll(getSimulatedCells())
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("基站模拟（非Root）") },
                actions = {
                    // 扫描按钮
                    IconButton(
                        onClick = { 
                            if (hasLocationPermission()) {
                                showScanDialog = true
                            } else {
                                onRequestPermission()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "扫描基站")
                    }
                    // 搜索按钮
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(Icons.Default.CloudDownload, contentDescription = "网络搜索")
                    }
                    // 示例按钮
                    IconButton(onClick = { showSampleDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "添加示例")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加基站")
                }
                
                if (isSimulating()) {
                    FloatingActionButton(
                        onClick = onStopSimulation,
                        containerColor = MaterialTheme.colorScheme.error
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "停止")
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            if (isShizukuReady()) {
                                onStartSimulation()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "开始")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 状态卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isShizukuReady()) 
                        MaterialTheme.colorScheme.primaryContainer
                    else 
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isShizukuReady()) "✓ Shizuku 已就绪" else "⚠ 需要 Shizuku 授权",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        
                        if (apiKeyConfigured()) {
                            Text(
                                text = "✓ OpenCellID 已配置",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = "✗ OpenCellID 未配置",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        
                        Text(
                            text = if (isSimulating()) "▶ 模拟运行中 - ${cells.size}个基站" else "⏹ 模拟已停止",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSimulating()) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (!hasLocationPermission()) {
                            Text(
                                text = "⚠ 需要定位权限以扫描基站",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    
                    if (isSimulating()) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 操作按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { 
                        if (hasLocationPermission()) {
                            showScanDialog = true
                        } else {
                            onRequestPermission()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("扫描 (${if(hasLocationPermission()) "就绪" else "需要权限"})")
                }
                
                OutlinedButton(
                    onClick = { showSearchDialog = true },
                    modifier = Modifier.weight(1f),
                    enabled = apiKeyConfigured()
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("搜索")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 基站列表
            if (cells.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SignalCellularOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "暂无基站",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "点击上方按钮扫描或搜索",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    text = "已添加 ${cells.size} 个基站",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cells.size) { index ->
                        val cell = cells[index]
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${cell.networkType} - LAC:${cell.lac} CID:${cell.cid}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "MCC:${cell.mcc} MNC:${cell.mnc}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    if (cell.latitude != 0.0 || cell.longitude != 0.0) {
                                        Text(
                                            text = "位置: ${String.format("%.4f", cell.latitude)}, ${String.format("%.4f", cell.longitude)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                IconButton(onClick = { 
                                    onRemoveCell(index)
                                    cells.removeAt(index)
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "删除",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 底部操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClearAll,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ClearAll, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("清空")
                }
            }
        }
    }
    
    // 扫描对话框
    if (showScanDialog) {
        ScanCellsDialog(
            onDismiss = { showScanDialog = false },
            onScan = { context ->
                isScanning = true
                val result = onScanCells(context)
                scannedCells = result
                isScanning = false
            },
            scannedCells = scannedCells,
            isScanning = isScanning,
            onAddAll = { cellsList ->
                cellsList.forEach { onAddCell(it) }
                cellsList.forEach { cells.add(it) }
                showScanDialog = false
            }
        )
    }
    
    // 搜索对话框
    if (showSearchDialog) {
        SearchCellsDialog(
            onDismiss = { showSearchDialog = false },
            onSearch = { lat, lon, radius ->
                isSearching = true
                // 使用协程调用搜索
                GlobalScope.launch {
                    val result = onSearchCells(lat, lon, radius)
                    scannedCells = result
                    result.forEach { cells.add(it) }
                    isSearching = false
                }
                showSearchDialog = false
            },
            isSearching = isSearching
        )
    }
    
    // 添加基站对话框
    if (showAddDialog) {
        AddBaseStationDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { cell ->
                onAddCell(cell)
                cells.add(cell)
                showAddDialog = false
            }
        )
    }
    
    // 添加示例对话框
    if (showSampleDialog) {
        AddSampleBaseStationDialog(
            onDismiss = { showSampleDialog = false },
            onSelect = { sample ->
                onAddCell(sample)
                cells.add(sample)
                showSampleDialog = false
            }
        )
    }
}

@Composable
fun ScanCellsDialog(
    onDismiss: () -> Unit,
    onScan: (android.content.Context) -> Unit,
    scannedCells: List<CellInfo>,
    isScanning: Boolean,
    onAddAll: (List<CellInfo>) -> Unit
) {
    val context = LocalContext.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("扫描附近基站") },
        text = {
            Column {
                if (isScanning) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("正在扫描手机当前连接的基站...")
                } else if (scannedCells.isEmpty()) {
                    Text("点击下方按钮扫描当前连接的基站")
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { onScan(context) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Wifi, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("扫描基站")
                    }
                } else {
                    Text("找到 ${scannedCells.size} 个基站:")
                    Spacer(Modifier.height(8.dp))
                    
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(scannedCells.size) { index ->
                            val cell = scannedCells[index]
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "${cell.networkType} - LAC:${cell.lac}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "CID:${cell.cid} MCC:${cell.mcc} MNC:${cell.mnc}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    if (cell.latitude != 0.0 || cell.longitude != 0.0) {
                                        Text(
                                            text = "位置: ${String.format("%.4f", cell.latitude)}, ${String.format("%.4f", cell.longitude)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (scannedCells.isNotEmpty()) {
                TextButton(
                    onClick = { onAddAll(scannedCells) }
                ) {
                    Text("添加全部 (${scannedCells.size})")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (scannedCells.isEmpty()) "关闭" else "取消")
            }
        }
    )
}

@Composable
fun SearchCellsDialog(
    onDismiss: () -> Unit,
    onSearch: (Double, Double, Double) -> Unit,
    isSearching: Boolean
) {
    var latitude by remember { mutableStateOf("30.5456") }
    var longitude by remember { mutableStateOf("114.3544") }
    var radius by remember { mutableStateOf("0.5") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("从网络搜索基站") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("正在从OpenCellID搜索基站...")
                } else {
                    Text(
                        text = "通过OpenCellID API搜索指定位置周边的基站",
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it },
                            label = { Text("纬度") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("30.5456") }
                        )
                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it },
                            label = { Text("经度") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("114.3544") }
                        )
                    }
                    
                    OutlinedTextField(
                        value = radius,
                        onValueChange = { radius = it },
                        label = { Text("搜索半径(公里)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("0.5") }
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💡 提示",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• 默认搜索武汉东站区域\n• 可输入其他坐标进行搜索\n• 搜索半径建议0.5-2公里",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!isSearching) {
                TextButton(
                    onClick = {
                        val lat = latitude.toDoubleOrNull() ?: return@TextButton
                        val lon = longitude.toDoubleOrNull() ?: return@TextButton
                        val rad = radius.toDoubleOrNull() ?: 0.5
                        if (lat != 0.0 && lon != 0.0) {
                            onSearch(lat, lon, rad)
                        }
                    }
                ) {
                    Text("搜索")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun AddBaseStationDialog(
    onDismiss: () -> Unit,
    onConfirm: (CellInfo) -> Unit
) {
    var networkType by remember { mutableStateOf("LTE") }
    var mcc by remember { mutableStateOf("460") }
    var mnc by remember { mutableStateOf("11") }
    var lac by remember { mutableStateOf("24365") }
    var cid by remember { mutableStateOf("16777215") }
    var psc by remember { mutableStateOf("0") }
    var lat by remember { mutableStateOf("30.5456") }
    var lng by remember { mutableStateOf("114.3544") }
    var radius by remember { mutableStateOf("500") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加基站") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 网络类型
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("GSM", "LTE", "WCDMA", "CDMA").forEach { type ->
                        FilterChip(
                            selected = networkType == type,
                            onClick = { networkType = type },
                            label = { Text(type) }
                        )
                    }
                }
                
                // MCC/MNC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = mcc,
                        onValueChange = { mcc = it },
                        label = { Text("MCC") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = mnc,
                        onValueChange = { mnc = it },
                        label = { Text("MNC") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                // LAC/CID
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = lac,
                        onValueChange = { lac = it },
                        label = { Text("LAC") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cid,
                        onValueChange = { cid = it },
                        label = { Text("CID") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                // PSC
                OutlinedTextField(
                    value = psc,
                    onValueChange = { psc = it },
                    label = { Text("PSC") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // 位置
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = lat,
                        onValueChange = { lat = it },
                        label = { Text("纬度") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = lng,
                        onValueChange = { lng = it },
                        label = { Text("经度") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                // 半径
                OutlinedTextField(
                    value = radius,
                    onValueChange = { radius = it },
                    label = { Text("半径(米)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cell = CellInfo(
                        id = UUID.randomUUID().toString(),
                        networkType = networkType,
                        mcc = mcc.toIntOrNull() ?: 460,
                        mnc = mnc.toIntOrNull() ?: 0,
                        lac = lac.toIntOrNull() ?: 0,
                        cid = cid.toLongOrNull() ?: 0L,
                        psc = psc.toIntOrNull() ?: 0,
                        latitude = lat.toDoubleOrNull() ?: 0.0,
                        longitude = lng.toDoubleOrNull() ?: 0.0,
                        radius = radius.toFloatOrNull() ?: 500f
                    )
                    onConfirm(cell)
                }
            ) {
                Text("添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun AddSampleBaseStationDialog(
    onDismiss: () -> Unit,
    onSelect: (CellInfo) -> Unit
) {
    val samples = BaseStationSimulatorManager.SampleBaseStations.getSamples()
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择示例基站") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(samples.size) { index ->
                    val sample = samples[index]
                    Card(
                        onClick = { onSelect(sample) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "${sample.networkType} - LAC:${sample.lac} CID:${sample.cid}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "MCC:${sample.mcc} MNC:${sample.mnc}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "位置: ${String.format("%.4f", sample.latitude)}, ${String.format("%.4f", sample.longitude)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}
