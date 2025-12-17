package com.example.veritrustmobile.ui.viewmodel

import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class CompraViewModelTest {

    private lateinit var viewModel: CompraViewModel

    @Before
    fun setup() {
        viewModel = CompraViewModel()
    }

    // TEST 1: Validación de Tarjeta Corta
    @Test
    fun `si el numero de tarjeta es muy corto, muestra error inmediato`() {
        // GIVEN: Un número de tarjeta inválido (menos de 16 dígitos)
        viewModel.onCardNumberChange("12345")

        // Simulamos datos válidos en el resto para aislar el error de tarjeta
        viewModel.onCardNameChange("Juan")
        viewModel.onCvvChange("123")

        // WHEN: Intentamos pagar
        viewModel.processPayment()

        // THEN: El mensaje de error debe aparecer inmediatamente (sin delays)
        // Nota: Asume que tu ViewModel valida esto antes de lanzar la corrutina
        viewModel.uiMessage shouldBe "Número de tarjeta inválido."
    }

    // TEST 2: Validación de Campos Vacíos
    @Test
    fun `si falta el nombre del titular, bloquea el pago`() {
        // GIVEN: Nombre vacío
        viewModel.onCardNameChange("")
        viewModel.onCardNumberChange("1234567812345678")

        // WHEN
        viewModel.processPayment()

        // THEN
        viewModel.uiMessage shouldBe "El nombre es obligatorio."
    }

    // TEST 3: Validación de Fecha Vencida (Lógica de Negocio)
    @Test
    fun `si el mes de expiracion es invalido, muestra error`() {
        // GIVEN: Mes "99"
        viewModel.onCardNameChange("Juan")
        viewModel.onCardNumberChange("4242424242424242")
        viewModel.onExpirationMonthChange("99")

        // WHEN
        viewModel.processPayment()

        // THEN
        viewModel.uiMessage shouldBe "Mes inválido."
    }
}