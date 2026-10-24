package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.DiaryEntry
import cr.ac.utn.nutristatus.domain.model.Doctor
import cr.ac.utn.nutristatus.domain.model.User
import cr.ac.utn.nutristatus.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow

interface NutriStatusRepository {
    fun getUserProfile(): Flow<User>
    fun getWeightRecord(): Flow<WeightRecord>
    fun getAssignedDoctor(): Flow<Doctor>
    fun getAppointments(): Flow<List<Appointment>>
    fun filterAppointments(query: String): Flow<List<Appointment>>
    fun getDiaryEntries(): Flow<List<DiaryEntry>>
    fun filterDiaryEntries(query: String): Flow<List<DiaryEntry>>
    suspend fun addDiaryEntry(entry: DiaryEntry)
    suspend fun scheduleAppointment(appointment: Appointment)
}
