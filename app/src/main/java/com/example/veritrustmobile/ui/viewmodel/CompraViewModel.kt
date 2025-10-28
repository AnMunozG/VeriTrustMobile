package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class CompraViewModel : ViewModel() {

    var cardName by mutableStateOf("")
        private set
    var cardNumber by mutableStateOf("")
        private set

    var expirationMonth by mutableStateOf("")
        private set

    var expirationYear by mutableStateOf("")
        private set

    var cvv by mutableStateOf("")
        private set

    var uiMessage by mutableStateOf<String?>(null)
        private set


    fun onCardNameChange(newName: String) {
        cardName = newName
    }

    fun onCardNumberChange(newNumber: String) {
        if (newNumber.length <= 16) {
            cardNumber = newNumber.filter { it.isDigit() }
        }
    }

    fun onExpirationMonthChange(newMonth: String) {
        if (newMonth.length <= 2) {
            expirationMonth = newMonth.filter { it.isDigit() }
        }
    }

    fun onExpirationYearChange(newYear: String) {
        if (newYear.length <= 4) {
            expirationYear = newYear.filter { it.isDigit() }
        }
    }

    fun onCvvChange(newCvv: String) {
        if (newCvv.length <= 4) {
            cvv = newCvv.filter { it.isDigit() }
        }
    }

    fun processPayment() {
        uiMessage = null

        when {
            cardName.isBlank() -> uiMessage = "El nombre en la tarjeta es obligatorio."
            !isCardNumberValid(cardNumber) -> uiMessage = "El número de tarjeta es inválido."
            !isExpirationMonthValid(expirationMonth) -> uiMessage = "El mes de expiración es inválido."
            !isExpirationYearValid(expirationYear) -> uiMessage = "El año de expiración es inválido."
            !isCvvValid(cvv) -> uiMessage = "El CVV es inválido."
            else -> {
                uiMessage = "Pago realizado con éxito"
                clearForm()
            }
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

    private fun isExpirationMonthValid(month: String): Boolean {
        return month.matches(Regex("^\\d{2}$")) && month.toIntOrNull() in 1..12
    }

    private fun isExpirationYearValid(year: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return year.matches(Regex("^\\d{4}$")) && (year.toIntOrNull() ?: 0) >= currentYear
    }

    private fun isCvvValid(cvv: String): Boolean {
        return cvv.matches(Regex("^\\d{3,4}$"))
    }

    private fun clearForm() {
        cardName = ""
        cardNumber = ""
        expirationMonth = ""
        expirationYear = ""
        cvv = ""
    }
}

