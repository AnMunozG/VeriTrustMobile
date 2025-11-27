// 1. PAQUETE: Ajustado a la carpeta raíz donde dejaste el archivo
package com.example.veritrustmobile

// 2. IMPORTS: Necesarios para encontrar tus clases que están en otras carpetas
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel
import com.example.veritrustmobile.repository.AuthRepository

// 3. HERRAMIENTAS DE TESTING (JUnit, Kotest, Mockito)
import org.junit.Before
import org.junit.Test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.mockito.Mockito.mock

class RegistroViewModelTest {

    // Dependencias
    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setup() {
        // MOCKITO: Creamos el repositorio falso para que el ViewModel no reclame
        mockRepository = mock(AuthRepository::class.java)

        // Inicializamos el ViewModel inyectándole el mock
        viewModel = RegistroViewModel(mockRepository)
    }

    // --- TEST 1: RUT Inválido ---
    @Test
    fun `si ingreso un RUT invalido, validarFormulario debe devolver false y marcar error`() {
        // GIVEN: Un RUT que sabemos que está malo
        viewModel.onRutChange("1-1")

        // WHEN: Ejecutamos la validación
        val esValido = viewModel.validarFormulario()

        // THEN: Verificamos con Kotest (leemos fácil el resultado)
        esValido shouldBe false
        viewModel.errorRut shouldBe "RUT inválido"
    }

    // --- TEST 2: Menor de Edad ---
    @Test
    fun `si el usuario es menor de edad, debe generar error en fecha nacimiento`() {
        // GIVEN: Una fecha reciente (menor de 18 años)
        viewModel.onFechaNacimientoChange("01/01/2022")

        // WHEN
        val esValido = viewModel.validarFormulario()

        // THEN
        esValido shouldBe false
        viewModel.errorFechaNacimiento shouldNotBe null
    }

    // --- TEST 3: Formulario Válido (Camino Feliz) ---
    @Test
    fun `si todos los datos son correctos, el formulario es valido`() {
        // GIVEN: Llenamos todo con datos válidos
        // (Asegúrate de usar un RUT que tu algoritmo considere válido, ej: 11.111.111-1)
        viewModel.onRutChange("11.111.111-1")
        viewModel.onNombreChange("Juan Pérez")
        viewModel.onFechaNacimientoChange("01/01/2000") // Mayor de 18
        viewModel.onTelefonoChange("912345678")
        viewModel.onEmailChange("juan@correo.com")
        viewModel.onConfirmarEmailChange("juan@correo.com")
        viewModel.onContrasenaChange("123456")
        viewModel.onConfirmarContrasenaChange("123456")
        viewModel.onTerminosChange(true)

        // WHEN
        val esValido = viewModel.validarFormulario()

        // THEN
        esValido shouldBe true
        viewModel.errorRut shouldBe null
        viewModel.errorNombre shouldBe null
    }
}