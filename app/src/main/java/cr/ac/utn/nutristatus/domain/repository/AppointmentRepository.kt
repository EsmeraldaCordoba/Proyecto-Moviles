package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.Specialist

interface AppointmentRepository {
    fun getAppointments(): List<Appointment>
    fun getAppointmentById(id: String): Appointment?
    fun scheduleAppointment(appointment: Appointment): Result<Unit>
    fun updateAppointment(appointment: Appointment): Result<Unit>
    fun cancelAppointment(id: String, reason: String): Result<Unit>
    
    fun getSpecialists(): List<Specialist>
    fun getSpecialistById(id: String): Specialist?
}
