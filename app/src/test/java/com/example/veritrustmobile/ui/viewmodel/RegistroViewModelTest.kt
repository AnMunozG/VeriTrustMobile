package com.example.veritrustmobile.ui.viewmodel

import com.example.veritrustmobile.repository.AuthRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class RegistroViewModelTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setup() {
        // MOCKK: La sintaxis es más limpia para Kotlin
        // 'relaxed = true' significa que si llamamos a algo no configurado, no explota
        mockRepository = mockk(relaxed = true)

        viewModel = RegistroViewModel(mockRepository)
    }

    // TEST 1
    @Test
    fun `si ingreso un RUT invalido, validarFormulario debe devolver false`() {
        viewModel.onRutChange("1-1")
        val esValido = viewModel.validarFormulario()

        esValido shouldBe false
        viewModel.errorRut shouldBe "RUT inválido"
    }

    // TEST 2
    @Test
    fun `si el usuario es menor de edad, marca error en fecha`() {
        viewModel.onFechaNacimientoChange("01/01/2022")
        val esValido = viewModel.validarFormulario()

        esValido shouldBe false
        viewModel.errorFechaNacimiento shouldNotBe null
    }

    // TEST 3
    @Test
    fun `si todos los datos son correctos, el formulario es valido`() {
        viewModel.onRutChange("11.111.111-1")
        viewModel.onNombreChange("Juan Pérez")
        viewModel.onFechaNacimientoChange("01/01/2000")
        viewModel.onTelefonoChange("912345678")
        viewModel.onEmailChange("juan@correo.com")
        viewModel.onConfirmarEmailChange("juan@correo.com")
        viewModel.onContrasenaChange("123456")
        viewModel.onConfirmarContrasenaChange("123456")
        viewModel.onTerminosChange(true)

        // Campos nuevos
        viewModel.onRegionChange("Metropolitana")
        viewModel.onGeneroChange("Masculino")

        val esValido = viewModel.validarFormulario()

        esValido shouldBe true
        viewModel.errorRut shouldBe null
    }
}