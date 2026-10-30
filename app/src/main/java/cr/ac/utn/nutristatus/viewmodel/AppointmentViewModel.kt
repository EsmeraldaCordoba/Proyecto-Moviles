package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.data.memory.InMemoryNutriStatusRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.model.Specialist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(AppointmentUiState(isLoading = true))
    val uiState: StateFlow<AppointmentUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            val appointments = repository.getAppointments()
            val specialists = repository.getSpecialists()
            _uiState.update { state ->
                val specialtyFilter = state.selectedSpecialtyFilter
                val filteredAppointments = appointments.filter { app ->
                    state.selectedStatusFilter == null || app.status == state.selectedStatusFilter
                }
                val filteredSpecialists = specialists.filter { spec ->
                    specialtyFilter.isNullOrBlank() || spec.specialty.contains(specialtyFilter, ignoreCase = true)
                }
                state.copy(
                    appointments = filteredAppointments,
                    specialists = filteredSpecialists,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Failed to load data.") }
        }
    }

    fun setStatusFilter(status: AppointmentStatus?) {
        _uiState.update { it.copy(selectedStatusFilter = status) }
        loadData()
    }

    fun setSpecialtyFilter(specialty: String?) {
        _uiState.update { it.copy(selectedSpecialtyFilter = specialty) }
        loadData()
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
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

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
        result.fold(
            onSuccess = {
                _uiState.update { it.copy(successMessage = "Appointment scheduled successfully!") }
                loadData()
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Failed to schedule appointment.") }
            }
        )
    }

    fun rescheduleAppointment(appointmentId: String, newDate: String, newTime: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        val existing = repository.getAppointmentById(appointmentId)
        if (existing != null) {
            val updated = existing.copy(date = newDate, time = newTime)
            val result = repository.updateAppointment(updated)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(successMessage = "Appointment rescheduled successfully!") }
                    loadData()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Failed to reschedule appointment.") }
                }
            )
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Appointment not found.") }
        }
    }

    fun cancelAppointment(appointmentId: String, reason: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        val result = repository.cancelAppointment(appointmentId, reason)
        result.fold(
            onSuccess = {
                _uiState.update { it.copy(successMessage = "Appointment cancelled successfully.") }
                loadData()
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Failed to cancel appointment.") }
            }
        )
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
