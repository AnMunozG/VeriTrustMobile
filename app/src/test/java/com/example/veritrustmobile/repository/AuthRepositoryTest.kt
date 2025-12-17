package com.example.veritrustmobile.repository

import com.example.veritrustmobile.data.ApiService
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.model.User
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private val apiService = mockk<ApiService>()

    @Before
    fun setup() {
        authRepository = AuthRepository()
        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns apiService
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login exitoso debe retornar el usuario`() {
        runBlocking {
            val mockUser = User(user = "test@test.com", password = "123", nombre = "Test", rut = "1-9", telefono = "123", fechaNacimiento = "01/01/2000", region = "Metropolitana", genero = "M")
            coEvery { apiService.login(any()) } returns Response.success(mockUser)

            val result = authRepository.login("test@test.com", "123")

            result shouldBe mockUser
        }
    }

    @Test
    fun `login con error 401 debe retornar null`() {
        runBlocking {
            coEvery { apiService.login(any()) } returns Response.error(401, "".toResponseBody())

            val result = authRepository.login("wrong@test.com", "wrong")

            result shouldBe null
        }
    }

    @Test
    fun `registrarUsuario exitoso debe retornar true`() {
        runBlocking {
            coEvery { apiService.registrar(any()) } returns Response.success(mockk())

            val result = authRepository.registrarUsuario(
                "12345678-5", "Juan", "01/01/1990", "987654321",
                "juan@test.com", "pass123", "Metropolitana", "Masculino"
            )

            result shouldBe true
        }
    }
}
