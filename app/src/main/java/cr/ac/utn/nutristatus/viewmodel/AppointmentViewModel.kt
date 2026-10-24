package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cr.ac.utn.nutristatus.data.memory.InMemoryNutriStatusRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.model.Specialist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class AppointmentUiState(
    val appointments: List<Appointment> = emptyList(),
    val specialists: List<Specialist> = emptyList(),
    val selectedStatusFilter: AppointmentStatus? = null,
    val selectedSpecialtyFilter: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AppointmentViewModel(
    private val repository: InMemoryNutriStatusRepository = InMemoryNutriStatusRepository()
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<AppointmentStatus?>(null)
    private val _specialtyFilter = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _successMessage = MutableStateFlow<String?>(null)

    private data class AppointmentsAndSpecialists(
        val appointments: List<Appointment>,
        val specialists: List<Specialist>
    )

    private data class FilterPair(
        val status: AppointmentStatus?,
        val specialty: String?
    )

    private data class CombinedData(
        val appointments: List<Appointment>,
        val specialists: List<Specialist>,
        val statusFilter: AppointmentStatus?,
        val specialtyFilter: String?
    )

    private data class StatusTriple(
        val isLoading: Boolean,
        val error: String?,
        val success: String?
    )

    val uiState: StateFlow<AppointmentUiState> = combine(
        repository.getAppointments(),
        repository.getSpecialists()
    ) { appointments, specialists ->
        AppointmentsAndSpecialists(appointments, specialists)
    }.combine(
        combine(_statusFilter, _specialtyFilter) { status, specialty ->
            FilterPair(status, specialty)
        }
    ) { data, filters ->
        CombinedData(data.appointments, data.specialists, filters.status, filters.specialty)
    }.combine(
        combine(_isLoading, _errorMessage, _successMessage) { loading, error, success ->
            StatusTriple(loading, error, success)
        }
    ) { combined, status ->
        val filteredAppointments = combined.appointments.filter { appointment ->
            combined.statusFilter == null || appointment.status == combined.statusFilter
        }

        val filteredSpecialists = combined.specialists.filter { specialist ->
            combined.specialtyFilter.isNullOrBlank() || specialist.specialty.contains(combined.specialtyFilter, ignoreCase = true)
        }

        AppointmentUiState(
            appointments = filteredAppointments,
            specialists = filteredSpecialists,
            selectedStatusFilter = combined.statusFilter,
            selectedSpecialtyFilter = combined.specialtyFilter,
            isLoading = status.isLoading,
            errorMessage = status.error,
            successMessage = status.success
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppointmentUiState(isLoading = true)
    )

    fun setStatusFilter(status: AppointmentStatus?) {
        _statusFilter.value = status
    }

    fun setSpecialtyFilter(specialty: String?) {
        _specialtyFilter.value = specialty
    }

    fun scheduleAppointment(
        patientId: String,
        patientName: String,
        specialistId: String,
        specialistName: String,
        specialistSpecialty: String,
        date: String,
        time: String,
        notes: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val newAppointment = Appointment(
                id = "app_" + UUID.randomUUID().toString().take(8),
                patientId = patientId,
                patientName = patientName,
                specialistId = specialistId,
                specialistName = specialistName,
                specialistSpecialty = specialistSpecialty,
                date = date,
                time = time,
                status = AppointmentStatus.PENDING,
                notes = notes
            )

            val result = repository.scheduleAppointment(newAppointment)
            _isLoading.value = false
            
            result.fold(
                onSuccess = {
                    _successMessage.value = "Appointment scheduled successfully!"
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to schedule appointment."
                }
            )
        }
    }

    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val existing = repository.getAppointmentById(appointmentId)
            if (existing != null) {
                val updated = existing.copy(date = newDate, time = newTime)
                val result = repository.updateAppointment(updated)
                _isLoading.value = false
                result.fold(
                    onSuccess = {
                        _successMessage.value = "Appointment rescheduled successfully!"
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.localizedMessage ?: "Failed to reschedule appointment."
                    }
                )
            } else {
                _isLoading.value = false
                _errorMessage.value = "Appointment not found."
            }
        }
    }

    fun cancelAppointment(appointmentId: String, reason: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val result = repository.cancelAppointment(appointmentId, reason)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    _successMessage.value = "Appointment cancelled successfully."
                },
                onFailure = { error ->
                    _errorMessage.value = error.localizedMessage ?: "Failed to cancel appointment."
                }
            )
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
