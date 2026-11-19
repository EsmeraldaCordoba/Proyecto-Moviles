package cr.ac.utn.nutristatus.data.memory

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.Location
import cr.ac.utn.nutristatus.domain.model.NutritionalEntry
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import cr.ac.utn.nutristatus.domain.repository.LocationRepository
import cr.ac.utn.nutristatus.domain.repository.NutritionalDiaryRepository

class MemoryDataRepository private constructor() : AppointmentRepository, LocationRepository, NutritionalDiaryRepository {

    private val appointments = mutableListOf<Appointment>()
    private val diaryEntries = mutableListOf<NutritionalEntry>()
    private var clinicLocation: Location? = Location("1", "Clínica Principal", "San José, Costa Rica")

    override fun createAppointment(appointment: Appointment): Boolean {
        return appointments.add(appointment)
    }

    override fun getAppointment(): Appointment? {
        return appointments.lastOrNull()
    }

    override fun getAppointments(): List<Appointment> {
        return appointments.toList()
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
