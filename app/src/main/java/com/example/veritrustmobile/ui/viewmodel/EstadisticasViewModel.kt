package com.example.veritrustmobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// ⭐ 1. Importar los repositorios necesarios
import com.example.veritrustmobile.repository.ComprasRepository
import com.example.veritrustmobile.repository.ServicesRepository // Asegúrate de que la ruta sea correcta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EstadisticaServicio(
    val servicioId: Int,
    val nombreServicio: String, // ⭐ 2. Añadir el nombre del servicio
    val cantidadVendida: Int,
    val totalVentas: Double
)

sealed class EstadisticasUiState {
    object Loading : EstadisticasUiState()
    data class Success(
        val ventasTotales: Double,
        val cantidadOrdenes: Int,
        val serviciosMasVendidos: List<EstadisticaServicio>
    ) : EstadisticasUiState()
    data class Error(val message: String) : EstadisticasUiState()
}

class EstadisticasViewModel(
    private val comprasRepository: ComprasRepository = ComprasRepository(),
    // ⭐ 3. Inyectar el repositorio de servicios (necesitarás ajustar el Dao)
    // Como ServicesRepository necesita un Dao, y no podemos instanciarlo aquí directamente,
    // vamos a crear una instancia simple por ahora.
    // En una app más grande, usarías Inyección de Dependencias (Hilt, Koin).
    private val servicesRepository: ServicesRepository = ServicesRepository(null) // Pasamos null temporalmente
) : ViewModel() {

    private val _uiState = MutableStateFlow<EstadisticasUiState>(EstadisticasUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarEstadisticas()
    }

    fun cargarEstadisticas() {
        viewModelScope.launch {
            _uiState.value = EstadisticasUiState.Loading
            try {
                // ⭐ 4. Obtener todas las compras Y todos los servicios
                val todasLasCompras = comprasRepository.getAllCompras()
                // AVISO: getAllServices() puede necesitar que le pases el Contexto
                // si depende de la base de datos local. Por simplicidad, asumimos que puede funcionar así.
                // Si no, necesitarás un AndroidViewModel y pasar el `application` al repo.
                val todosLosServicios = servicesRepository.getServicios()
                val mapaServicios = todosLosServicios.associateBy { it.id }

                if (todasLasCompras.isEmpty()) {
                    _uiState.value = EstadisticasUiState.Success(
                        ventasTotales = 0.0,
                        cantidadOrdenes = 0,
                        serviciosMasVendidos = emptyList()
                    )
                    return@launch
                }

                val ventasTotales = todasLasCompras.sumOf { it.montoTotal }
                val cantidadOrdenes = todasLasCompras.size
                val serviciosAgrupados = todasLasCompras.groupBy { it.servicioId }

                val serviciosMasVendidos = serviciosAgrupados.map { (servicioId, comprasDelServicio) ->
                    EstadisticaServicio(
                        servicioId = servicioId,
                        // ⭐ 5. Buscar el nombre del servicio usando el mapa
                        nombreServicio = mapaServicios[servicioId]?.nombre ?: "Servicio Desconocido",
                        cantidadVendida = comprasDelServicio.size,
                        totalVentas = comprasDelServicio.sumOf { it.montoTotal }
                    )
                }.sortedByDescending { it.totalVentas }
                    .take(5)

                _uiState.value = EstadisticasUiState.Success(
                    ventasTotales = ventasTotales,
                    cantidadOrdenes = cantidadOrdenes,
                    serviciosMasVendidos = serviciosMasVendidos
                )

            } catch (e: Exception) {
                _uiState.value = EstadisticasUiState.Error(
                    e.message ?: "Error al cargar estadísticas"
                )
            }
        }
    }
}
