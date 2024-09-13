package com.example.budgetapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetapp.model.Category
import com.example.budgetapp.viewmodel.AddBudgeViewModel

@Composable
fun AddBudgetScreen(
    viewModel: AddBudgeViewModel = hiltViewModel()
) {
    var selectedOptionMoney by remember { mutableStateOf("$") }
    var selectedOptionIncomeAndExpenses by remember { mutableStateOf("Income") }
    var amountTextFieldText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category("Choose a Category")) }

    Column {
        BudgetInputCard(
            selectedOptionMoney = selectedOptionMoney,
            onMoneyOptionSelected = { selectedOptionMoney = it },
            selectedOptionIncomeAndExpenses = selectedOptionIncomeAndExpenses,
            onIncomeOrExpenseSelected = { selectedOptionIncomeAndExpenses = it },
            amountTextFieldText = amountTextFieldText,
            onAmountChange = { amountTextFieldText = it }
        )

        CategorySelectionCard(
            viewModel = viewModel,
            selectedCategory = selectedCategory.categoryName,
            onCategorySelected = { selectedCategory = it }
        )

        SaveButton {
            if (amountTextFieldText.isNotBlank()){
                viewModel.addNewBudget(
                    amount = if(selectedOptionIncomeAndExpenses == "Income") amountTextFieldText.toDouble() else amountTextFieldText.toDouble() * -1,
                    amountOfDate = viewModel.getCurrentDate(),
                    moneyType = selectedOptionMoney,
                    categoryId = if (selectedCategory.categoryId==0) null else selectedCategory.categoryId
                )
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetInputCard(
    selectedOptionMoney: String,
    onMoneyOptionSelected: (String) -> Unit,
    selectedOptionIncomeAndExpenses: String,
    onIncomeOrExpenseSelected: (String) -> Unit,
    amountTextFieldText: String,
    onAmountChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Select Currency")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RadioButtonWithLabelMoney(
                    label = "$",
                    selected = selectedOptionMoney == "$",
                    onClick = { onMoneyOptionSelected("$") }
                )
                RadioButtonWithLabelMoney(
                    label = "€",
                    selected = selectedOptionMoney == "€",
                    onClick = { onMoneyOptionSelected("€") }
                )
                RadioButtonWithLabelMoney(
                    label = "₺",
                    selected = selectedOptionMoney == "₺",
                    onClick = { onMoneyOptionSelected("₺") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Income or Expense")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RadioButtonWithLabelIncExp(
                    label = "Income",
                    selected = selectedOptionIncomeAndExpenses == "Income",
                    onClick = { onIncomeOrExpenseSelected("Income") }
                )
                RadioButtonWithLabelIncExp(
                    label = "Expense",
                    selected = selectedOptionIncomeAndExpenses == "Expense",
                    onClick = { onIncomeOrExpenseSelected("Expense") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = amountTextFieldText,
                onValueChange = { newText ->
                    if (newText.all { char -> char.isDigit() }) {
                        onAmountChange(newText)
                    }
                },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}



@Composable
private fun RadioButtonWithLabelMoney(
    label: String,
    selected: Boolean,
    onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFBDBDBD))
                .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF4CAF50),
                    unselectedColor = Color.Transparent
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp)
            )
        }
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RadioButtonWithLabelIncExp(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFBDBDBD))
                .border(2.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF4CAF50),
                    unselectedColor = Color.Transparent
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp)
            )
        }
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
private fun CategorySelectionCard(
    viewModel: AddBudgeViewModel,
    selectedCategory: String ,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialogAdd by remember { mutableStateOf(false) }
    var showDialogEdit by remember { mutableStateOf(false) }

    val categories by viewModel.allCategory

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(text = "Choose a Category", Modifier.weight(1f))

                IconButton(onClick = { showDialogAdd = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add category"
                    )
                }

                IconButton(onClick = { showDialogEdit = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Category"
                    )
                }

                if (showDialogAdd) {
                    DialogAddCategory(
                        onDismiss = { showDialogAdd = false },
                        onSave = { categoryName ->
                            viewModel.addNewCategory(categoryName)
                            showDialogAdd = false
                        }
                    )
                }
                if (showDialogEdit) {
                    DialogEditCategory(
                        onDismiss = { showDialogEdit = false },
                        viewModel = viewModel
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = selectedCategory)
                }

                StyledDropdownMenu(
                    categories = categories,
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    onCategorySelected = { category ->
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
private fun StyledDropdownMenu(
    categories: List<Category>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onCategorySelected: (Category) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        categories.forEach { category ->
            DropdownMenuItem(
                onClick = {
                    onCategorySelected(category)
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSurface,
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                ),
            text = {
                Text(
                    text = category.categoryName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            })
        }
    }
}

@Composable
private fun DialogEditCategory(
    onDismiss: () -> Unit,
    viewModel: AddBudgeViewModel
) {
    val categories by viewModel.allCategory
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Delete Category", style = MaterialTheme.typography.labelMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(categories) { category ->
                            CategoryItem(
                                category,
                                onDelete = { viewModel.deleteCategory(category.categoryId) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Back")
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category.categoryName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}




@Composable
private fun DialogAddCategory(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit) {
    var categoryName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Enter Category Name")
                TextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text(text = "Category Name") }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Button(
                        onClick = {
                            if (categoryName.isNotBlank()) {
                                onSave(categoryName)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Save")
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Butonlar arasında boşluk

                    Button(
                        onClick = { onDismiss() }, // Cancel işlemi sadece kapatıyor
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}


@Composable
private fun SaveButton(
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFBDBDBD),
                disabledContentColor = Color.Gray),
            modifier = Modifier
                .height(85.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),

        ) {
            Text(
                text = "Add Budget",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
