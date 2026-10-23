package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for map locations and clinic details screen.
 */
class ClinicViewModel(
    private val repository: NutriStatusRepository
) : ViewModel() {

    private val _clinics = MutableStateFlow<List<ClinicLocation>>(emptyList())
    val clinics: StateFlow<List<ClinicLocation>> = _clinics.asStateFlow()

    private val _selectedClinic = MutableStateFlow<ClinicLocation?>(null)
    val selectedClinic: StateFlow<ClinicLocation?> = _selectedClinic.asStateFlow()

    init {
        loadClinics()
    }

    fun loadClinics() {
        val list = repository.getClinicLocations()
        _clinics.value = list
        if (list.isNotEmpty() && _selectedClinic.value == null) {
            _selectedClinic.value = list.first()
        }
    }

    fun selectClinic(clinicId: String) {
        _selectedClinic.value = repository.getClinicLocationById(clinicId)
    }
}
