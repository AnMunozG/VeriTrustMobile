package com.example.veritrustmobile.repository

import com.example.veritrustmobile.data.ApiService
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.data.local.ServicioDao
import com.example.veritrustmobile.model.Servicio
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ServicesRepositoryTest {

    private lateinit var repository: ServicesRepository
    private val apiService = mockk<ApiService>()
    private val servicioDao = mockk<ServicioDao>(relaxed = true)

    @Before
    fun setup() {
        repository = ServicesRepository(servicioDao)
        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns apiService
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
