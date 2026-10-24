package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cr.ac.utn.nutristatus.data.memory.InMemoryNutriStatusRepository
import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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

    private val _searchQuery = MutableStateFlow("")
    private val _selectedLocationId = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private data class LocationsAndQuery(
        val locations: List<ClinicLocation>,
        val query: String,
        val selectedId: String?
    )

    private data class StatusPair(
        val isLoading: Boolean,
        val error: String?
    )

    val uiState: StateFlow<LocationUiState> = combine(
        repository.getClinicLocations(),
        _searchQuery,
        _selectedLocationId
    ) { locations, query, selectedId ->
        LocationsAndQuery(locations, query, selectedId)
    }.combine(
        combine(_isLoading, _errorMessage) { loading, error ->
            StatusPair(loading, error)
        }
    ) { data, status ->
        val filteredLocations = data.locations.filter { location ->
            data.query.isBlank() ||
                    location.name.contains(data.query, ignoreCase = true) ||
                    location.city.contains(data.query, ignoreCase = true) ||
                    location.address.contains(data.query, ignoreCase = true)
        }

        val selected = data.selectedId?.let { id ->
            data.locations.find { it.id == id }
        } ?: data.locations.firstOrNull()

        LocationUiState(
            locations = filteredLocations,
            selectedLocation = selected,
            searchQuery = data.query,
            isLoading = status.isLoading,
            errorMessage = status.error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocationUiState(isLoading = true)
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectLocation(locationId: String) {
        _selectedLocationId.value = locationId
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
