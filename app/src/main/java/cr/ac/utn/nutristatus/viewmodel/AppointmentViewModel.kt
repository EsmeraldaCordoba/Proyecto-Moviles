package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository

class AppointmentViewModel(
    private val repository: AppointmentRepository = MemoryDataRepository.getInstance()
) : ViewModel() {

    fun scheduleAppointment(appointment: Appointment): Boolean {
        return repository.createAppointment(appointment)
    }

    fun loadAppointments(): List<Appointment> {
        return repository.getAppointments()
    }

    fun loadAppointment(): Appointment? {
        return repository.getAppointment()
    }

    fun filterAppointments(query: String): List<Appointment> {
        val all = repository.getAppointments()
        if (query.isBlank()) return all
        return all.filter {
            it.doctorName.contains(query, ignoreCase = true) ||
            it.department.contains(query, ignoreCase = true) ||
            it.clinicName.contains(query, ignoreCase = true)
        }
    }
}
