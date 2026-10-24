package cr.ac.utn.nutristatus.data.memory

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import cr.ac.utn.nutristatus.domain.model.Specialist
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import cr.ac.utn.nutristatus.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryNutriStatusRepository : AppointmentRepository, LocationRepository {

    private val _specialists = MutableStateFlow(
        listOf(
            Specialist(
                id = "spec_1",
                name = "Dr. Alejandro Morales",
                specialty = "Sports Nutrition",
                rating = 4.9f,
                imageUrl = "https://example.com/alejandro.jpg",
                bio = "Specialist in high-performance sports nutrition and body recomposition.",
                consultationFee = 50.0,
                availableDays = listOf("Monday", "Wednesday", "Friday")
            ),
            Specialist(
                id = "spec_2",
                name = "Dra. Sofía Vargas",
                specialty = "Weight Control",
                rating = 4.8f,
                imageUrl = "https://example.com/sofia.jpg",
                bio = "Expert in sustainable weight loss, clinical nutrition, and metabolic health.",
                consultationFee = 45.0,
                availableDays = listOf("Tuesday", "Thursday", "Saturday")
            ),
            Specialist(
                id = "spec_3",
                name = "Dr. Carlos Jiménez",
                specialty = "Functional Nutrition for Hormonal Balance",
                rating = 4.95f,
                imageUrl = "https://example.com/carlos.jpg",
                bio = "Focused on hormonal balance, insulin resistance, and gut health optimization.",
                consultationFee = 60.0,
                availableDays = listOf("Monday", "Tuesday", "Thursday")
            )
        )
    )

    private val _appointments = MutableStateFlow(
        listOf(
            Appointment(
                id = "app_1",
                patientId = "pat_1",
                patientName = "Maria Gomez",
                specialistId = "spec_1",
                specialistName = "Dr. Alejandro Morales",
                specialistSpecialty = "Sports Nutrition",
                date = "2025-06-10",
                time = "10:00",
                status = AppointmentStatus.PENDING,
                notes = "Initial sports assessment and macro calculation."
            ),
            Appointment(
                id = "app_2",
                patientId = "pat_1",
                patientName = "Maria Gomez",
                specialistId = "spec_2",
                specialistName = "Dra. Sofía Vargas",
                specialistSpecialty = "Weight Control",
                date = "2025-05-15",
                time = "14:30",
                status = AppointmentStatus.COMPLETED,
                notes = "Monthly weight check-in and diet adjustment."
            )
        )
    )

    private val _clinicLocations = MutableStateFlow(
        listOf(
            ClinicLocation(
                id = "loc_1",
                name = "NutriStatus San José Central",
                address = "Avenida Central, Calle 4, Edificio Médico Sabana, Piso 3",
                city = "San José",
                latitude = 9.9328,
                longitude = -84.0807,
                phoneNumber = "+506 2222-3333",
                openingHours = "Mon - Fri: 8:00 AM - 6:00 PM",
                imageUrl = "https://example.com/sanjose_clinic.jpg"
            ),
            ClinicLocation(
                id = "loc_2",
                name = "NutriStatus Alajuela Mall",
                address = "Costado norte de City Mall, Plaza Ejecutiva Local 12",
                city = "Alajuela",
                latitude = 10.0162,
                longitude = -84.2145,
                phoneNumber = "+506 2441-5555",
                openingHours = "Mon - Sat: 9:00 AM - 7:00 PM",
                imageUrl = "https://example.com/alajuela_clinic.jpg"
            ),
            ClinicLocation(
                id = "loc_3",
                name = "NutriStatus Heredia Plaza",
                address = "Frente a Universidad Nacional, Centro Comercial Paseo de las Flores",
                city = "Heredia",
                latitude = 9.9988,
                longitude = -84.1165,
                phoneNumber = "+506 2261-7777",
                openingHours = "Mon - Fri: 8:30 AM - 5:30 PM",
                imageUrl = "https://example.com/heredia_clinic.jpg"
            )
        )
    )

    override fun getAppointments(): Flow<List<Appointment>> = _appointments.asStateFlow()

    override suspend fun getAppointmentById(id: String): Appointment? {
        return _appointments.value.find { it.id == id }
    }

    override suspend fun scheduleAppointment(appointment: Appointment): Result<Unit> {
        return try {
            _appointments.update { currentList ->
                currentList + appointment
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAppointment(appointment: Appointment): Result<Unit> {
        return try {
            _appointments.update { currentList ->
                currentList.map { if (it.id == appointment.id) appointment else it }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelAppointment(id: String, reason: String): Result<Unit> {
        return try {
            _appointments.update { currentList ->
                currentList.map {
                    if (it.id == id) {
                        it.copy(status = AppointmentStatus.CANCELLED, cancelReason = reason)
                    } else {
                        it
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSpecialists(): Flow<List<Specialist>> = _specialists.asStateFlow()

    override suspend fun getSpecialistById(id: String): Specialist? {
        return _specialists.value.find { it.id == id }
    }

    override fun getClinicLocations(): Flow<List<ClinicLocation>> = _clinicLocations.asStateFlow()

    override suspend fun getLocationById(id: String): ClinicLocation? {
        return _clinicLocations.value.find { it.id == id }
    }
}
