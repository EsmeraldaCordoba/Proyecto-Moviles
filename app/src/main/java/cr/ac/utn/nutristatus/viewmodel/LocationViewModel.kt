package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.data.memory.InMemoryNutriStatusRepository
import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationUiState(
    val locations: List<ClinicLocation> = emptyList(),
    val selectedLocation: ClinicLocation? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LocationViewModel(
    private val repository: InMemoryNutriStatusRepository = InMemoryNutriStatusRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState(isLoading = true))
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            val locations = repository.getClinicLocations()
            _uiState.update { state ->
                val filtered = locations.filter { loc ->
                    state.searchQuery.isBlank() ||
                            loc.name.contains(state.searchQuery, ignoreCase = true) ||
                            loc.city.contains(state.searchQuery, ignoreCase = true) ||
                            loc.address.contains(state.searchQuery, ignoreCase = true)
                }
                val selected = state.selectedLocation ?: filtered.firstOrNull()
                state.copy(
                    locations = filtered,
                    selectedLocation = selected,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Failed to load locations.") }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadLocations()
    }

    fun selectLocation(locationId: String) {
        val location = repository.getLocationById(locationId)
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
