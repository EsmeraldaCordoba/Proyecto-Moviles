package cr.ac.utn.nutristatus.viewmodel
import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() {

    fun scheduleAppointment(appointment: Appointment) {
        repository.createAppointment(appointment)
    }

    fun loadAppointment(): Appointment? {
        return repository.getAppointment()
    }
}