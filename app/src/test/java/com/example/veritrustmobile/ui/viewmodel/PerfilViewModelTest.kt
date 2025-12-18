package com.example.veritrustmobile.ui.viewmodel

import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.data.ApiService
import com.example.veritrustmobile.data.RetrofitClient
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModelTest {

    private lateinit var viewModel: PerfilViewModel
    private val apiService = mockk<ApiService>()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        
        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns apiService
        
        mockkObject(SessionManager)
        every { SessionManager.getToken() } returns "test@test.com"
        
        // Mockeamos la llamada a getUsuario que ocurre en el init
        coEvery { apiService.getUsuario(any()) } returns Response.success(mockk(relaxed = true))
        
        viewModel = PerfilViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `al guardarCambios exitosamente debe mostrar mensaje de exito`() {
        coEvery { apiService.actualizarUsuario(any()) } returns Response.success(mockk())
        
        viewModel.email = "test@test.com"
        viewModel.guardarCambios()

        viewModel.mensaje shouldBe "Datos actualizados correctamente"
        viewModel.isEditing shouldBe false
    }
}
