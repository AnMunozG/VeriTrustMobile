package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class CompraViewModel : ViewModel() {
    var cardName by mutableStateOf(""); private set
    var cardNumber by mutableStateOf(""); private set
    var expirationMonth by mutableStateOf(""); private set
    var expirationYear by mutableStateOf(""); private set
    var cvv by mutableStateOf(""); private set

    var uiMessage by mutableStateOf<String?>(null); private set
    var isLoading by mutableStateOf(false); private set

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onCardNameChange(v: String) { cardName = v }
    fun onCardNumberChange(v: String) { if (v.length <= 16) cardNumber = v.filter { it.isDigit() } }
    fun onExpirationMonthChange(v: String) { if (v.length <= 2) expirationMonth = v.filter { it.isDigit() } }
    fun onExpirationYearChange(v: String) { if (v.length <= 4) expirationYear = v.filter { it.isDigit() } }
    fun onCvvChange(v: String) { if (v.length <= 4) cvv = v.filter { it.isDigit() } }

    fun processPayment() {
        uiMessage = null
        if (cardName.isBlank()) { uiMessage = "El nombre es obligatorio."; return }
        if (!isCardNumberValid(cardNumber)) { uiMessage = "Número de tarjeta inválido."; return }
        if (!isExpirationMonthValid(expirationMonth)) { uiMessage = "Mes inválido."; return }
        if (!isExpirationYearValid(expirationYear)) { uiMessage = "Año inválido."; return }
        if (!isCvvValid(cvv)) { uiMessage = "CVV inválido."; return }

        viewModelScope.launch {
            isLoading = true
            delay(2000)
            uiMessage = "Pago realizado con éxito"
            isLoading = false
            _navigationEvent.emit(true)
            clearForm()
        }
    }

    private fun isCardNumberValid(number: String): Boolean {
        if (!number.matches(Regex("^\\d{16}$"))) return false
        var sum = 0
        var isSecondDigit = false
        for (i in number.length - 1 downTo 0) {
            var digit = number[i].toString().toInt()
            if (isSecondDigit) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            isSecondDigit = !isSecondDigit
        }
        return sum % 10 == 0
    }
    private fun isExpirationMonthValid(month: String): Boolean { return month.matches(Regex("^\\d{2}$")) && month.toIntOrNull() in 1..12 }
    private fun isExpirationYearValid(year: String): Boolean { val currentYear = Calendar.getInstance().get(Calendar.YEAR); return year.matches(Regex("^\\d{4}$")) && (year.toIntOrNull() ?: 0) >= currentYear }
    private fun isCvvValid(cvv: String): Boolean { return cvv.matches(Regex("^\\d{3,4}$")) }
    private fun clearForm() { cardName = ""; cardNumber = ""; expirationMonth = ""; expirationYear = ""; cvv = "" }
}