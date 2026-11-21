package cr.ac.utn.nutristatus.data.repository

import cr.ac.utn.nutristatus.data.local.dao.AppointmentDao
import cr.ac.utn.nutristatus.data.local.entity.AppointmentEntity
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAppointmentRepository(
    private val appointmentDao: AppointmentDao
) : AppointmentRepository {

    override fun observeAppointments(): Flow<List<Appointment>> {
        return appointmentDao.observeAppointments().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAppointments(): List<Appointment> {
        return appointmentDao.getAllAppointments().map { it.toDomain() }
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        return appointmentDao.getAppointmentById(id)?.toDomain()
    }

    override suspend fun insertAppointment(appointment: Appointment) {
        appointmentDao.insertAppointment(AppointmentEntity.fromDomain(appointment))
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        appointmentDao.updateAppointment(AppointmentEntity.fromDomain(appointment))
    }

    override suspend fun deleteAppointment(id: String) {
        appointmentDao.deleteAppointment(id)
    }
}
