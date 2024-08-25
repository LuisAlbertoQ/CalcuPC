package com.example.calcupc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcupc.ui.theme.CalcuPCTheme
import com.example.calcupc.ui.theme.Purple200
import com.example.calcupc.ui.theme.textColor

fun isNumeric(toCheck: String): Boolean {
    return toCheck.toDoubleOrNull() != null
}

fun calculatePower(base: Double, exponent: Double): Double {
    return Math.pow(base, exponent)
}

fun calculateSquareRoot(value: Double): Double {
    return Math.sqrt(value)
}

fun calculateInverse(value: Double): Double {
    return 1 / value
}

@Composable
fun ButtonX(
    modifier: Modifier,
    valuex: String,
    onValueChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    textState: String,
    isNewOp: Boolean,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    oldTextState: String,
    op: String
) {
    Column(modifier = modifier.wrapContentSize(Alignment.Center)) {
        Box(
            modifier = modifier
                .weight(1f)
                .background(Color(0xFF00BCD4))
                .border(width = .5.dp, Color(0xFF2C2F32))
                .clickable(
                    enabled = true,
                    onClick = {
                        if (isNumeric(valuex)) {
                            var valor = textState
                            if (isNewOp) {
                                valor = ""
                                onValueChange.invoke(valor)
                            }
                            onIsNewOpChange.invoke(false)
                            valor += valuex
                            onValueChange.invoke(valor)
                        }
                        when (valuex) {
                            "+", "-", "*", "/", "%", "^" -> {
                                onOpChange.invoke(valuex)
                                onOldValueChange.invoke(textState)
                                onIsNewOpChange.invoke(true)
                            }
                            "AC" -> {
                                onValueChange.invoke("0")
                                onIsNewOpChange.invoke(true)
                            }
                            "." -> {
                                var dot = textState
                                if (isNewOp) {
                                    dot = ""
                                    onValueChange.invoke(dot)
                                }
                                onIsNewOpChange.invoke(false)
                                if (!dot.contains(".")) {
                                    dot += "."
                                    onValueChange.invoke(dot)
                                }
                            }
                            "=" -> {
                                if (oldTextState.isNotEmpty()) {
                                    var finalNumber = 0.0
                                    when (op) {
                                        "*" -> finalNumber = oldTextState.toDouble() * textState.toDouble()
                                        "/" -> finalNumber = oldTextState.toDouble() / textState.toDouble()
                                        "+" -> finalNumber = oldTextState.toDouble() + textState.toDouble()
                                        "-" -> finalNumber = oldTextState.toDouble() - textState.toDouble()
                                        "^" -> finalNumber = calculatePower(oldTextState.toDouble(), textState.toDouble())
                                    }
                                    onValueChange.invoke(finalNumber.toString())
                                    onIsNewOpChange.invoke(true)
                                }
                            }
                            "√" -> {
                                val result = calculateSquareRoot(textState.toDouble())
                                onValueChange.invoke(result.toString())
                                onIsNewOpChange.invoke(true)
                            }
                            "1/x" -> {
                                val result = calculateInverse(textState.toDouble())
                                onValueChange.invoke(result.toString())
                                onIsNewOpChange.invoke(true)
                            }
                            "π" -> {
                                onValueChange.invoke(Math.PI.toString())
                                onIsNewOpChange.invoke(true)
                            }
                        }
                    }
                )
        ) {
            Text(
                text = valuex,
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.End,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTextField(
    textState: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .background(Purple200)
            .wrapContentSize(Alignment.BottomEnd)
            .fillMaxSize()
    ) {
        TextField(
            value = textState,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.BottomEnd)
                .fillMaxSize(),
            textStyle = TextStyle(fontSize = 36.sp, textAlign = TextAlign.End, color = textColor),
            maxLines = 2,
            readOnly = true
        )
    }
}

@Composable
fun CalculatorFirstRow(
    textState: String,
    isNewOp: Boolean,
    onValueChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    modifier: Modifier,
    op: String,
    oldTextState: String,
    data: List<String>
) {
    Row(modifier = modifier.fillMaxSize()) {
        data.forEach {
            ButtonX(
                modifier = modifier,
                valuex = it,
                onValueChange = onValueChange,
                onIsNewOpChange = onIsNewOpChange,
                textState = textState, isNewOp = isNewOp,
                onOpChange = onOpChange,
                onOldValueChange = onOldValueChange,
                op = op,
                oldTextState = oldTextState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalcUPeU() {
    CalcuPCTheme {
        Column {
            var op by remember { mutableStateOf("") }
            var isNewOp by remember { mutableStateOf(true) }
            var oldTextState by remember { mutableStateOf("") }
            var textState by remember { mutableStateOf("0") }

            CalculatorTextField(
                textState = textState,
                modifier = Modifier.height(100.dp),
                onValueChange = { textState = it }
            )

            Column(modifier = Modifier.fillMaxSize()) {
                val listA = listOf("AC", ".", "%", "/")
                val listB = listOf("7", "8", "9", "*")
                val listC = listOf("4", "5", "6", "-")
                val listD = listOf("1", "2", "3", "+")
                val listE = listOf("0", "π", "1/x", "√", "=")
                val listF = listOf("x^y")

                val listaCompleta = listOf(listA, listB, listC, listD, listE, listF)
                listaCompleta.forEach {
                    CalculatorFirstRow(
                        isNewOp = isNewOp,
                        textState = textState,
                        onValueChange = { textState = it },
                        onIsNewOpChange = { isNewOp = it },
                        onOpChange = { op = it },
                        onOldValueChange = { oldTextState = it },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        op = op,
                        oldTextState = oldTextState,
                        data = it
                    )
                }
            }
        }
    }
}
