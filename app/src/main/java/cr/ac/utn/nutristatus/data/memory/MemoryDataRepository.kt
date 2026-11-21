package cr.ac.utn.nutristatus.data.memory

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.Location
import cr.ac.utn.nutristatus.domain.model.NutritionalEntry
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import cr.ac.utn.nutristatus.domain.repository.LocationRepository
import cr.ac.utn.nutristatus.domain.repository.NutritionalDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemoryDataRepository private constructor() : AppointmentRepository, LocationRepository, NutritionalDiaryRepository {

    private val appointmentsFlow = MutableStateFlow<List<Appointment>>(emptyList())
    private val diaryEntries = mutableListOf<NutritionalEntry>()
    private var clinicLocation: Location? = Location("1", "Clínica Principal", "San José, Costa Rica")

    override fun observeAppointments(): Flow<List<Appointment>> {
        return appointmentsFlow.asStateFlow()
    }

    override suspend fun getAppointments(): List<Appointment> {
        return appointmentsFlow.value
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        return appointmentsFlow.value.find { it.id == id }
    }

    override suspend fun insertAppointment(appointment: Appointment) {
        val current = appointmentsFlow.value.toMutableList()
        current.removeAll { it.id == appointment.id }
        current.add(appointment)
        appointmentsFlow.value = current
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        val current = appointmentsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == appointment.id }
        if (index != -1) {
            current[index] = appointment
            appointmentsFlow.value = current
        }
    }

    override suspend fun deleteAppointment(id: String) {
        val current = appointmentsFlow.value.toMutableList()
        current.removeAll { it.id == id }
        appointmentsFlow.value = current
    }

    override fun getClinicLocation(): Location? {
        return clinicLocation
    }

    override fun addEntry(entry: NutritionalEntry): Boolean {
        return diaryEntries.add(entry)
    }

    override fun getEntries(): List<NutritionalEntry> {
        return diaryEntries.toList()
    }

    override fun updateEntry(entry: NutritionalEntry): Boolean {
        val index = diaryEntries.indexOfFirst { it.id == entry.id }
        return if (index != -1) {
            diaryEntries[index] = entry
            true
        } else {
            false
        }
    }

    override fun deleteEntry(id: String): Boolean {
        return diaryEntries.removeIf { it.id == id }
    }

    companion object {
        @Volatile
        private var instance: MemoryDataRepository? = null

        fun getInstance(): MemoryDataRepository {
            return instance ?: synchronized(this) {
                instance ?: MemoryDataRepository().also { instance = it }
            }
        }
    }
}
