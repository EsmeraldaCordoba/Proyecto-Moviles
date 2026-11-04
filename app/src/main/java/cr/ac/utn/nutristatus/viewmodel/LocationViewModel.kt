package cr.ac.utn.nutristatus.viewmodel
import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.domain.model.Location
import cr.ac.utn.nutristatus.domain.repository.LocationRepository

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    fun loadLocation(): Location? {
        return repository.getClinicLocation()
    }
}
