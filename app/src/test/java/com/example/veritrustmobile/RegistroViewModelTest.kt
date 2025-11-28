package com.example.veritrustmobile

import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel
import com.example.veritrustmobile.repository.AuthRepository

// 3. HERRAMIENTAS DE TESTING (JUnit, Kotest, Mockito)
import org.junit.Before
import org.junit.Test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.mockito.Mockito.mock

class RegistroViewModelTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setup() {
        // MOCKITO
        mockRepository = mock(AuthRepository::class.java)

        viewModel = RegistroViewModel(mockRepository)
    }

    //TEST 1
    @Test
    fun `si ingreso un RUT invalido, validarFormulario debe devolver false y marcar error`() {
        viewModel.onRutChange("1-1")

        // WHEN: Haces tus momos en Android Studio
        val esValido = viewModel.validarFormulario()

        esValido shouldBe false
        viewModel.errorRut shouldBe "RUT inválido"
    }

    //TEST 2
    @Test
    fun `si el usuario es menor de edad, debe generar error en fecha nacimiento`() {
        viewModel.onFechaNacimientoChange("01/01/2022")

        val esValido = viewModel.validarFormulario()

        esValido shouldBe false
        viewModel.errorFechaNacimiento shouldNotBe null
    }

    //TEST 3
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

        val esValido = viewModel.validarFormulario()

        esValido shouldBe true
        viewModel.errorRut shouldBe null
        viewModel.errorNombre shouldBe null
    }
}