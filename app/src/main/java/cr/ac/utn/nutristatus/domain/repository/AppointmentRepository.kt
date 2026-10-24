package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.Specialist
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getAppointments(): Flow<List<Appointment>>
    suspend fun getAppointmentById(id: String): Appointment?
    suspend fun scheduleAppointment(appointment: Appointment): Result<Unit>
    suspend fun updateAppointment(appointment: Appointment): Result<Unit>
    suspend fun cancelAppointment(id: String, reason: String): Result<Unit>
    
    fun getSpecialists(): Flow<List<Specialist>>
    suspend fun getSpecialistById(id: String): Specialist?
}
