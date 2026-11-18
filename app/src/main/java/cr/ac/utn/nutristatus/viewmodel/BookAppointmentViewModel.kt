package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository

class BookAppointmentViewModel(
    private val repository: AppointmentRepository = MemoryDataRepository.getInstance()
) : ViewModel() {

    fun saveAppointment(appointment: Appointment): Boolean {
        return repository.createAppointment(appointment)
    }
}
