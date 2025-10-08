package com.example.mynewapp

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mynewapp.data.Device
import com.example.mynewapp.data.MiBandActivity
import com.example.mynewapp.ui.CustomMarkerView
import com.example.mynewapp.ui.FileType
import com.example.mynewapp.ui.MainViewModel
import com.example.mynewapp.ui.MainViewModelFactory
import com.example.mynewapp.ui.TableContent
import com.example.mynewapp.ui.saveTableContentToDownloads
import com.example.mynewapp.ui.theme.Blue40
import com.example.mynewapp.ui.theme.Green40
import com.example.mynewapp.ui.theme.MynewappTheme
import com.example.mynewapp.ui.theme.Red40
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MynewappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplashScreen by remember { mutableStateOf(true) }

                    if (showSplashScreen) {
                        SplashScreen(onTimeout = { showSplashScreen = false })
                    } else {
                        MainContent(mainViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val context = LocalContext.current
    val animatedVectorDrawable = remember {
        context.getDrawable(R.drawable.avd_heartbeat) as AnimatedVectorDrawable
    }

    LaunchedEffect(Unit) {
        animatedVectorDrawable.start()
        delay(2000) // Total splash screen duration
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // White circle background
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberDrawablePainter(drawable = animatedVectorDrawable),
                    contentDescription = "Heartbeat Animation",
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "MyNewApp",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


sealed class Screen {
    object CsvTable : Screen()
    object DbTable : Screen()
    object DbChart : Screen()
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.DbTable) }
    val dbActivities by viewModel.activities.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val (csvTableContentForDisplay, csvTableContentForChart) = remember {
        readCsvFromAssets(context, "MI_BAND_ACTIVITY_SAMPLE.csv")
    }

    when (currentScreen) {
        is Screen.CsvTable -> CsvDataScreen(
            tableContentForDisplay = csvTableContentForDisplay,
            tableContentForChart = csvTableContentForChart,
            onShowDbTable = { currentScreen = Screen.DbTable }
        )
        is Screen.DbTable -> DatabaseTableScreen(
            onShowChart = { currentScreen = Screen.DbChart },
            onShowCsvTable = { currentScreen = Screen.CsvTable }
        )
        is Screen.DbChart -> DatabaseChartScreen(
            activities = dbActivities,
            onBack = { currentScreen = Screen.DbTable }
        )
    }
}

@Composable
fun HeartRateChartScreen(
    title: String,
    entries: List<Entry>,
    onBack: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        Spacer(Modifier.width(8.dp))
                        Text("Back to Table")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Adjusted padding for this screen
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            if (entries.isNotEmpty()) {
                AndroidView(
                    factory = { ctx ->
                        LineChart(ctx).apply {
                            description.isEnabled = false
                            isDragEnabled = true
                            setScaleEnabled(true)
                            setPinchZoom(true)
                            legend.textColor = colorScheme.onSurface.toArgb()
                            setNoDataText("Loading chart data...")
                            setNoDataTextColor(colorScheme.onSurface.toArgb())

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                textColor = colorScheme.onSurface.toArgb()
                                valueFormatter = object : ValueFormatter() {
                                    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    override fun getFormattedValue(value: Float): String {
                                        return sdf.format(Date(value.toLong()))
                                    }
                                }
                            }

                            axisRight.isEnabled = false
                            axisLeft.apply {
                                setDrawGridLines(true)
                                gridColor = colorScheme.onSurface.copy(alpha = 0.2f).toArgb()
                                textColor = colorScheme.onSurface.toArgb()
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return "${value.toInt()} BPM"
                                    }
                                }
                            }
                        }
                    },
                    update = { chart ->
                        val dataSet = LineDataSet(entries, "Heart Rate").apply {
                            mode = LineDataSet.Mode.CUBIC_BEZIER
                            cubicIntensity = 0.4f
                            color = Red40.toArgb()
                            valueTextColor = colorScheme.onSurface.toArgb()
                            setDrawValues(false)
                            setDrawCircles(false)
                            lineWidth = 3f

                            // Fill gradient
                            setDrawFilled(true)
                            val gradient = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(Red40.copy(alpha = 0.4f).toArgb(), Red40.copy(alpha = 0.0f).toArgb())
                            )
                            fillDrawable = gradient

                            // Highlight
                            highLightColor = Red40.toArgb()
                            setDrawHorizontalHighlightIndicator(false)
                            setDrawVerticalHighlightIndicator(true)
                            highlightLineWidth = 2f
                        }
                        chart.data = LineData(dataSet)

                        // After setting data, invalidate to let the chart know its bounds
                        chart.invalidate()

                        // Ensure chart is scrollable and starts at the beginning
                        if (entries.size > 40) {
                            val visibleCount = 40f
                            val totalCount = entries.size.toFloat()
                            val scaleX = totalCount / visibleCount

                            // Zoom in on the X-axis
                            chart.zoom(scaleX, 1f, 0f, 0f)

                            // Move the view to the beginning of the chart
                            chart.moveViewToX(0f)
                        }

                        chart.animateX(1000)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No Heart Rate data available to display.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun InfoAndCreditScreen(
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        Spacer(Modifier.width(8.dp))
                        Text("Back to Table")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Text(
                    text = "Info and Credit",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Section: Application Info
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Application Info", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Name: MyNewApp", style = MaterialTheme.typography.bodyLarge)
                        Text("Version: 1.0.0", style = MaterialTheme.typography.bodyLarge)
                        Text("Description: This application helps you visualize your Mi Band fitness data with beautiful graphs, making it easier to understand your heart rate patterns.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Section: Data Source & Credits
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Data Source & Credits", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Data Source: Activity data is imported from the Gadgetbridge application database.", style = MaterialTheme.typography.bodyLarge)
                        Text("Device: Displaying data from Xiaomi Mi Smart Band devices.", style = MaterialTheme.typography.bodyLarge)
                        Text("Acknowledgement: Special thanks to the Gadgetbridge team and the open-source community for making this project possible.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Section: Developer
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Developer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Developed By: Witthawat Ch.", style = MaterialTheme.typography.bodyLarge)
                        Text("Contact: For feedback or inquiries, please contact 664230029@webmail.npru.ac.th.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Section: Open Source Libraries
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Open Source Libraries", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Jetpack Compose", style = MaterialTheme.typography.bodyLarge)
                        Text("• MPAndroidChart by PhilJay", style = MaterialTheme.typography.bodyLarge)
                        Text("• Room Persistence Library", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun DatabaseChartScreen(
    activities: List<MiBandActivity>,
    onBack: () -> Unit
) {
    InfoAndCreditScreen(onBack = onBack)
}

@Composable
fun DatabaseTableScreen(
    onShowChart: () -> Unit,
    onShowCsvTable: () -> Unit
) {
    val fakeDevices = listOf(
        Device(id = 1, name = "Xiaomi Smart Band 7", alias = "My Smart Band", manufacturer = "Xiaomi", identifier = "A4:05:6E:C8:48:86")
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onShowChart,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Show Info")
                        Spacer(Modifier.width(8.dp))
                        Text("Info")
                    }
                    Button(
                        onClick = onShowCsvTable,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.Description, contentDescription = "Show CSV Data")
                        Spacer(Modifier.width(8.dp))
                        Text("CSV Data")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Devices from Database",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            if (fakeDevices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No devices found in database.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(fakeDevices) { device ->
                        DeviceCard(device = device)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: Device) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Watch,
                contentDescription = "Device Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = device.identifier ?: "N/A",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CsvDataScreen(
    tableContentForDisplay: TableContent,
    tableContentForChart: TableContent,
    onShowDbTable: () -> Unit
) {
    var showChart by remember { mutableStateOf(false) }

    if (showChart) {
        CsvChartScreen(
            tableContent = tableContentForChart,
            onBack = { showChart = false }
        )
    } else {
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        fun handleDownload(fileType: FileType) {
            val (success, errorMessage) = saveTableContentToDownloads(context, tableContentForDisplay, "mi_band_activity_sample_export", fileType)
            scope.launch {
                val message = if (success) {
                    "Successfully saved as ${fileType.extension.uppercase()}"
                } else {
                    "Failed to save file: $errorMessage"
                }
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onShowDbTable,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(Icons.Filled.Watch, contentDescription = "Show Devices")
                            Spacer(Modifier.width(8.dp))
                            Text("Devices")
                        }
                        Button(
                            onClick = { showChart = true },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Icon(Icons.Filled.Equalizer, contentDescription = "Show Chart")
                            Spacer(Modifier.width(8.dp))
                            Text("Chart")
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(it).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Data from CSV: MI_BAND_ACTIVITY_SAMPLE.csv",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // CSV Button - Grey
                    Button(
                        onClick = { handleDownload(FileType.CSV) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = "Download CSV")
                        Spacer(Modifier.width(8.dp))
                        Text("CSV")
                    }
                    // XLS Button - Green
                    Button(
                        onClick = { handleDownload(FileType.XLS) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = "Download XLS")
                        Spacer(Modifier.width(8.dp))
                        Text("XLS")
                    }
                    // TXT Button - Blue
                    Button(
                        onClick = { handleDownload(FileType.TXT) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = "Download TXT")
                        Spacer(Modifier.width(8.dp))
                        Text("TXT")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (tableContentForDisplay.rows.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No data found in CSV file.")
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            // Table Header
                            stickyHeader {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(12.dp)
                                ) {
                                    tableContentForDisplay.columns.forEach { columnName ->
                                        Text(
                                            text = columnName,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.titleSmall,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            // Table Rows
                            itemsIndexed(tableContentForDisplay.rows) { index, rowData ->
                                val backgroundColor = if (index % 2 == 0) {
                                    MaterialTheme.colorScheme.surface
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.05f)
                                }
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .background(backgroundColor)
                                        .padding(vertical = 8.dp, horizontal = 12.dp)
                                ) {
                                    rowData.forEach { cellData ->
                                        Text(
                                            text = cellData,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.bodyMedium,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CsvChartScreen(tableContent: TableContent, onBack: () -> Unit) {
    val timestampIndex = tableContent.columns.indexOf("TIMESTAMP")
    val heartRateIndex = tableContent.columns.indexOf("HEART_RATE")

    val entries = if (timestampIndex != -1 && heartRateIndex != -1) {
        tableContent.rows.mapNotNull { row ->
            try {
                val timestampString = row[timestampIndex]
                val heartRateString = row[heartRateIndex]
                Entry(timestampString.toFloat(), heartRateString.toFloat())
            } catch (e: Exception) {
                null // Ignore rows with parsing errors
            }
        }
    } else {
        emptyList()
    }

    HeartRateChartScreen(
        title = "Heart Rate Over Time (CSV)",
        entries = entries,
        onBack = onBack
    )
}

fun readCsvFromAssets(context: Context, fileName: String): Pair<TableContent, TableContent> {
    try {
        val inputStream = context.assets.open(fileName)
        val reader = inputStream.bufferedReader()
        val originalHeader = reader.readLine().split(',')
        val timestampIndex = originalHeader.indexOf("TIMESTAMP")

        // Identify indices of columns to remove
        val columnsToRemove = setOf("RAW_INTENSITY", "STEPS", "RAW_KIND")
        val indicesToRemove = originalHeader.mapIndexedNotNull { index, header ->
            if (header in columnsToRemove) index else null
        }.toSet()

        // Create the new header
        val newHeader = originalHeader.filterIndexed { index, _ -> index !in indicesToRemove }

        val rowsForDisplay = mutableListOf<List<String>>()
        val rowsForChart = mutableListOf<List<String>>()

        reader.readLines().forEach { line ->
            val originalRow = line.split(',').toMutableList()
            
            // Create filtered row for chart data (raw values)
            val chartRow = originalRow.filterIndexed { index, _ -> index !in indicesToRemove }
            rowsForChart.add(chartRow)

            // Create filtered row for display data (formatted timestamp)
            if (timestampIndex != -1 && originalRow.size > timestampIndex) {
                originalRow[timestampIndex] = formatTimestamp(originalRow[timestampIndex])
            }
            val displayRow = originalRow.filterIndexed { index, _ -> index !in indicesToRemove }
            rowsForDisplay.add(displayRow)
        }

        val tableForDisplay = TableContent(columns = newHeader, rows = rowsForDisplay)
        // The chart data needs its own header, which is the same newHeader
        val tableForChart = TableContent(columns = newHeader, rows = rowsForChart)

        return Pair(tableForDisplay, tableForChart)
    } catch (e: Exception) {
        e.printStackTrace()
        return Pair(TableContent(), TableContent())
    }
}

fun formatTimestamp(timestamp: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val netDate = Date(timestamp.toLong())
        sdf.format(netDate)
    } catch (e: Exception) {
        timestamp
    }
}