package com.example.budgetapp.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.budgetapp.view.charts.DonutPie
import com.example.budgetapp.view.randomColor.getRandomColor
import com.example.budgetapp.viewmodel.ReportViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
//Test Screen buglar bulunuyor
@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel()
) {
    val incomeText by viewModel.totalIncome
    val expenseText by viewModel.totalExpense
    val netIncomeText by viewModel.totalAmount
    val categoryList by viewModel.categorySumsTotalList
    var showDialog by remember { mutableStateOf(false) }
    var filterOptionZone1 by remember { mutableStateOf("Tüm Zamanlar") }
    var startDate by remember { mutableStateOf<TextFieldValue>(TextFieldValue("")) }
    var endDate by remember { mutableStateOf<TextFieldValue>(TextFieldValue("")) }

    val highestSpendingCategory = viewModel.getHighestSpendingCategory(categoryList)
    val lowestSpendingCategory = viewModel.getLowestSpendingCategory(categoryList)
    val highestIncomeCategory = viewModel.getHighestIncomeCategory(categoryList)
    val lowestIncomeCategory = viewModel.getLowestIncomeCategory(categoryList)

    LaunchedEffect(filterOptionZone1, startDate.text, endDate.text) {
        viewModel.loadSumAmount(startDate.text, endDate.text)
    }

    if (showDialog) {
        FilterDialog(
            onDismiss = { showDialog = false },
            onFilterOptionSelected = { option ->
                filterOptionZone1 = option
                if (option == "Tarihe Göre Filtrele") {
                    //Reset date
                    startDate = TextFieldValue("")
                    endDate = TextFieldValue("")
                }
                else if(option == "Tüm Zamanlar"){

                }
            },
            startDate = startDate,
            onStartDateChange = { startDate = it },
            endDate = endDate,
            onEndDateChange = { endDate = it },
            defaultSelected = "Tüm Zamanlar"
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        item {
            ExpandableCardIncomeExpenses(
                onFilterClick = { showDialog = true },
                title = "Income-Expense Comparison",
                netIncomeText = netIncomeText.toString(),
                expenseText = expenseText.toString(),
                incomeText = incomeText.toString(),
                donutChartData = PieChartData(
                    plotType = PlotType.Donut,
                    slices = categoryList.map { category ->
                        PieChartData.Slice(
                            label = category.categoryName ?: "UNCAT",
                            color = getRandomColor(),
                            value = category.totalAmount.toFloat()
                        )
                    }
                ),
                sumUnit = ""
            )
        }
        item {
            ExpandableCardCategoryDetails(
                title = "Category Details",
                highestSpendingCategory = highestSpendingCategory?.categoryName ?: "Uncat",
                lowestSpendingCategory = lowestSpendingCategory?.categoryName ?: "Uncat",
                highestIncomeCategory = highestIncomeCategory?.categoryName ?: "Uncat",
                lowestIncomeCategory = lowestIncomeCategory?.categoryName ?: "Uncat",
                pieChartData = PieChartData(
                    plotType = PlotType.Donut,
                    slices = categoryList.map { category ->
                        PieChartData.Slice(
                            label = category.categoryName ?: "UNCAT",
                            color = getRandomColor(),
                            value = category.totalAmount.toFloat()
                        )
                    }
                )
            )
        }
        /*
        İsteğe bağlı yapılıcak
        item {
            PdfButton()
        }

         */
    }

}

@Composable
fun PdfButton(modifier: Modifier = Modifier) {
    var isPressed by remember { mutableStateOf(false) }

    // Animasyon değerleri
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 6.dp,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(animatedElevation, RoundedCornerShape(50))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00C6A0),
                        Color(0xFF007A6D),
                        Color(0xFF004D40)
                    )
                ),
                shape = RoundedCornerShape(50)
            )
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale
            )
            .clickable {
                isPressed = true
                println("PDF olarak çıkartılıyor...")
            }
    ) {
        Text(
            text = "PDF Olarak Çıkart",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}



@Composable
fun ExpandableCardIncomeExpenses(
    onFilterClick: () -> Unit,
    title: String,
    incomeText: String,
    expenseText: String,
    netIncomeText: String,
    donutChartData: PieChartData,
    sumUnit : String
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(shape = ShapeDefaults.Medium)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color(0xFF4CAF50),
            containerColor = Color(0xFFB2DFDB),
            disabledContainerColor = Color(0xFF81C784),
            disabledContentColor = Color(0xFFBDBDBD)
        ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(0.7f)
                        .rotate(rotationState)
                        .weight(1f),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop Down Arrow",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (expandedState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { onFilterClick() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Filtreleme ikonu",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        text = "Income : $incomeText",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Expense : $expenseText",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Net : $netIncomeText",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(8.dp)
                    )

                    DonutPie(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color(0xFFB2DFDB))
                            .padding(top = 16.dp),
                        donutChartData = donutChartData,
                        sumUnit = sumUnit
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDialog(
    defaultSelected: String, // Varsayılan olarak seçili olan filtre seçeneği
    onDismiss: () -> Unit, // Dialog kapandığında çağrılacak fonksiyon
    onFilterOptionSelected: (String) -> Unit, // Filtre seçeneği seçildiğinde çağrılacak fonksiyon
    startDate: TextFieldValue, // Başlangıç tarihi için TextFieldValue
    onStartDateChange: (TextFieldValue) -> Unit, // Başlangıç tarihi değiştiğinde çağrılacak fonksiyon
    endDate: TextFieldValue, // Bitiş tarihi için TextFieldValue
    onEndDateChange: (TextFieldValue) -> Unit // Bitiş tarihi değiştiğinde çağrılacak fonksiyon
) {

    // Şimdi filtre seçeneğini ve tarihleri saklayacak değişkenler tanımlıyoruz
    var selectedOption by remember { mutableStateOf(defaultSelected) } // Şu anda hangi filtre seçili
    var showStartDatePicker by remember { mutableStateOf(false) } // Başlangıç tarihi seçici gösterilecek mi
    var showEndDatePicker by remember { mutableStateOf(false) } // Bitiş tarihi seçici gösterilecek mi
    var selectedStartDate by remember { mutableStateOf<Long?>(null) } // Seçilen başlangıç tarihi
    var selectedEndDate by remember { mutableStateOf<Long?>(null) } // Seçilen bitiş tarihi
    var controlStartDate by remember { mutableStateOf<Long?>(null) } // Başlangıç tarihini kontrol için
    var controlEndDate by remember { mutableStateOf<Long?>(null) } // Bitiş tarihini kontrol için
    var dateError by remember { mutableStateOf<String?>(null) } // Tarih hatalarını saklamak için

    // Eğer tarih hatası varsa, kullanıcıya hata mesajını gösterir
    LaunchedEffect(dateError) {

    }

    // Eğer başlangıç tarihi seçici gösteriliyorsa
        if (showStartDatePicker) {
            val startDatePickerState
                    = rememberDatePickerState(initialSelectedDateMillis = selectedStartDate ?: System.currentTimeMillis())

            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false }, // Seçici kapatıldığında
                confirmButton = {
                    TextButton(onClick = {
                        selectedStartDate = startDatePickerState.selectedDateMillis // Seçilen tarihi güncelle
                        selectedStartDate?.let { millis ->
                            val formattedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE) // Tarihi formatla
                            controlStartDate = millis
                            onStartDateChange(TextFieldValue(formattedDate)) // Tarih değişimini bildir
                        }
                        showStartDatePicker = false // Seçiciyi kapat
                    }) {
                        Text("Tamam") // Tamam butonunun metni
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) {
                        Text("İptal") // İptal butonunun metni
                    }
                },
                content = {
                    DatePicker(state = startDatePickerState) // Tarih seçici bileşeni
                }
            )
        }

    // Eğer bitiş tarihi seçici gösteriliyorsa ve başlangıç tarihi seçilmişse
    if (showEndDatePicker && selectedStartDate != null) {
        val endDatePickerState =
            rememberDatePickerState(initialSelectedDateMillis = selectedEndDate ?: System.currentTimeMillis())

        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false }, // Seçici kapatıldığında
            confirmButton = {
                TextButton(onClick = {
                    selectedEndDate = endDatePickerState.selectedDateMillis // Seçilen tarihi güncelle
                    selectedEndDate?.let { millis ->
                        val formattedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ISO_LOCAL_DATE) // Tarihi formatla
                        controlEndDate = millis
                        onEndDateChange(TextFieldValue(formattedDate)) // Tarih değişimini bildir
                    }
                    showEndDatePicker = false // Seçiciyi kapat
                })
                {
                    Text("Tamam") // Tamam butonunun metni
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("İptal") // İptal butonunun metni
                }
            },
            content = {
                DatePicker(state = endDatePickerState) // Tarih seçici bileşeni
            }
        )
    }

    // Filtreleme seçeneklerinin bulunduğu ana dialog
    AlertDialog(
        onDismissRequest = onDismiss, // Dialog kapandığında çağrılacak fonksiyon
        title = { Text("Filtreleme Seçenekleri") },
        text = {
            Column{
                // "Tüm Zamanlar" seçeneği için
                Row (verticalAlignment = Alignment.CenterVertically){
                    RadioButton(
                        selected = selectedOption == "Tüm Zamanlar",
                        onClick = {
                            selectedOption = "Tüm Zamanlar" // Seçimi güncelle
                            onFilterOptionSelected("Tüm Zamanlar") // Seçimi bildir
                        }
                    )
                    Text(
                        text = "Tüm Zamanlar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .clickable {
                                selectedOption = "Tüm Zamanlar" // Seçimi güncelle
                                onFilterOptionSelected("Tüm Zamanlar") // Seçimi bildir
                            }
                    )
                }
                // "Tarihe Göre Filtrele" seçeneği için
                Row (verticalAlignment = Alignment.CenterVertically){
                    RadioButton(
                        selected = selectedOption == "Tarihe Göre Filtrele",
                        onClick = {
                            selectedOption = "Tarihe Göre Filtrele" // Seçimi güncelle
                            onFilterOptionSelected("Tarihe Göre Filtrele") // Seçimi bildir
                        }
                    )
                    Text(
                        text = "Tarihe Göre Filtrele",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .clickable {
                                selectedOption = "Tarihe Göre Filtrele" // Seçimi güncelle
                                onFilterOptionSelected("Tarihe Göre Filtrele") // Seçimi bildir
                            }
                    )
                }

                // "Tarihe Göre Filtrele" seçeneği seçilmişse tarih alanlarını gösterir
                if (selectedOption == "Tarihe Göre Filtrele") {
                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        // Başlangıç tarihi seçici
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { value -> onStartDateChange(value) }, // Tarih değişimini bildir
                            label = { Text("Başlangıç Tarihi") }, // Etiket
                            readOnly = true, // Kullanıcı tarafından değiştirilemez
                            trailingIcon = {
                                IconButton(onClick = { showStartDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Başlangıç Tarihi Seç"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Bitiş tarihi seçici
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { value -> onEndDateChange(value) }, // Tarih değişimini bildir
                            label = { Text("Bitiş Tarihi") },
                            readOnly = true, // Kullanıcı tarafından değiştirilemez
                            trailingIcon = {
                                IconButton(onClick = { showEndDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Bitiş Tarihi Seç"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Tarih hatası varsa hata mesajını gösterir
                        dateError?.let { errorText ->
                            Text(
                                text = errorText,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                // "Tarihe Göre Filtrele" seçeneği seçilmişse tarihleri doğrular
                if (selectedOption == "Tarihe Göre Filtrele") {
                    if (startDate.text.isEmpty() || endDate.text.isEmpty()) {
                        dateError = "Tüm tarihler doldurulmalıdır!" // Tarihler boşsa hata mesajı
                    } else if (controlEndDate != null && controlStartDate != null) {
                        if (controlEndDate!! < controlStartDate!!) {
                            dateError = "Bitiş tarihi, başlangıç tarihinden önce olamaz." // Bitiş tarihi, başlangıç tarihinden önceyse hata mesajı
                        } else {
                            onDismiss() // Hata yoksa dialog'u kapat
                        }
                    }
                } else if (selectedOption == "Tüm Zamanlar") {
                    onDismiss() // "Tüm Zamanlar" seçiliyse dialog'u kapat
                }
            }) {
                Text("Tamam")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}


@Composable
fun ExpandableCardCategoryDetails(
    title: String,
    highestSpendingCategory: String,
    lowestSpendingCategory: String,
    highestIncomeCategory: String,
    lowestIncomeCategory: String,
    pieChartData: PieChartData
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(shape = ShapeDefaults.Medium)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(16.dp), // Daha yuvarlak köşeler
        colors = CardDefaults.cardColors(
            contentColor = Color(0xFF4CAF50),
            containerColor = Color(0xFFB2DFDB),
            disabledContainerColor = Color(0xFF81C784),
            disabledContentColor = Color(0xFFBDBDBD)
        ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(0.7f)
                        .rotate(rotationState)
                        .weight(1f),
                    onClick = {
                        expandedState = !expandedState
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop Down Arrow",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (expandedState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "En Çok Harcama Yapılan Kategori: $highestSpendingCategory",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "En Az Harcama Yapılan Kategori: $lowestSpendingCategory",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "En Çok Gelir Getiren Kategori: $highestIncomeCategory",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "En Az Gelir Getiren Kategori: $lowestIncomeCategory",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                    DonutPie(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color(0xFFB2DFDB))
                            .padding(top = 16.dp),
                        donutChartData = pieChartData,
                        sumUnit = ""
                    )
                }
            }
        }
    }
}