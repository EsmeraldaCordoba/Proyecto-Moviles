package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Appointment

interface AppointmentRepository {
    fun createAppointment(appointment: Appointment): Boolean
    fun getAppointment(): Appointment?
}