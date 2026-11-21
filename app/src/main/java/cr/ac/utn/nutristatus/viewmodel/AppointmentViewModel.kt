package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    val appointments: StateFlow<List<Appointment>> = repository.observeAppointments()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun scheduleAppointment(appointment: Appointment, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.insertAppointment(appointment)
            onComplete?.invoke()
        }
    }

    fun updateAppointment(appointment: Appointment, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.updateAppointment(appointment)
            onComplete?.invoke()
        }
    }

    fun deleteAppointment(id: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteAppointment(id)
            onComplete?.invoke()
        }
    }

    fun filterAppointments(query: String, currentList: List<Appointment>): List<Appointment> {
        if (query.isBlank()) return currentList
        return currentList.filter {
            it.doctorName.contains(query, ignoreCase = true) ||
            it.department.contains(query, ignoreCase = true) ||
            it.clinicName.contains(query, ignoreCase = true)
        }
    }

    class Factory(
        private val repository: AppointmentRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
                return AppointmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
