package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import cr.ac.utn.nutristatus.util.AppConstants
import cr.ac.utn.nutristatus.util.DateTimeUtils
import cr.ac.utn.nutristatus.util.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for managing appointment CRUD, searching, filtering, and 24-hour cancellation rules.
 */
class AppointmentViewModel(
    private val repository: NutriStatusRepository
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<AppointmentStatus?>(null)
    val selectedStatusFilter: StateFlow<AppointmentStatus?> = _selectedStatusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _show24HourWarningDialog = MutableStateFlow<Pair<Boolean, String?>>(false to null)
    val show24HourWarningDialog: StateFlow<Pair<Boolean, String?>> = _show24HourWarningDialog.asStateFlow()

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        val all = repository.getAppointments()
        val filter = _selectedStatusFilter.value
        val query = _searchQuery.value

        var filtered = if (filter != null) {
            all.filter { it.status == filter }
        } else {
            all
        }

        if (query.isNotBlank()) {
            val lower = query.lowercase()
            filtered = filtered.filter {
                it.nutritionistName.lowercase().contains(lower) ||
                        it.specialty.lowercase().contains(lower) ||
                        it.notes.lowercase().contains(lower)
            }
        }

        _appointments.value = filtered
    }

    fun filterByStatus(status: AppointmentStatus?) {
        _selectedStatusFilter.value = status
        loadAppointments()
    }

    fun searchAppointments(query: String) {
        _searchQuery.value = query
        loadAppointments()
    }

    fun scheduleAppointment(
        nutritionistId: String,
        nutritionistName: String,
        specialty: String,
        dateTimeStr: String,
        timeSlot: String,
        clinicLocationName: String,
        notes: String
    ): Result<Unit> {
        val validation = ValidationUtils.validateAppointmentInput(nutritionistId, dateTimeStr, timeSlot)
        if (validation.isFailure) return validation

        val patient = repository.getPatientProfile()
        val newAppointment = Appointment(
            id = "",
            patientName = patient.name,
            nutritionistId = nutritionistId,
            nutritionistName = nutritionistName,
            specialty = specialty,
            dateTime = "$dateTimeStr $timeSlot",
            timeSlot = timeSlot,
            status = AppointmentStatus.CONFIRMED,
            clinicLocationName = clinicLocationName,
            notes = notes
        )

        val success = repository.addAppointment(newAppointment)
        return if (success) {
            loadAppointments()
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to schedule appointment."))
        }
    }

    fun prepareCancelAppointment(appointmentId: String) {
        val appointment = repository.getAppointmentById(appointmentId) ?: return
        val isWithin24Hours = DateTimeUtils.isWithinHours(
            dateTimeStr = appointment.dateTime,
            cutoffHours = AppConstants.CANCELLATION_WARNING_HOURS
        )
        if (isWithin24Hours) {
            _show24HourWarningDialog.value = true to appointmentId
        } else {
            confirmCancelAppointment(appointmentId, reason = null)
        }
    }

    fun confirmCancelAppointment(appointmentId: String, reason: String?) {
        repository.cancelAppointment(appointmentId, reason)
        _show24HourWarningDialog.value = false to null
        loadAppointments()
    }

    fun dismissWarningDialog() {
        _show24HourWarningDialog.value = false to null
    }

    fun deleteAppointment(appointmentId: String) {
        repository.deleteAppointment(appointmentId)
        loadAppointments()
    }
}
