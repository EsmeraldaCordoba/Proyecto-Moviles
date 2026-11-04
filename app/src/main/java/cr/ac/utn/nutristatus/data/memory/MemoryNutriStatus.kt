package cr.ac.utn.nutristatus.data.memory
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.Location
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import cr.ac.utn.nutristatus.domain.repository.LocationRepository

class MemoryNutriStatus : AppointmentRepository, LocationRepository {

    private var currentAppointment: Appointment? = null
    private var clinicLocation: Location? = null

    override fun createAppointment(appointment: Appointment): Boolean {
        currentAppointment = appointment
        return true
    }

    override fun getAppointment(): Appointment? {
        return currentAppointment
    }

    override fun getClinicLocation(): Location? {
        return clinicLocation
    }
}