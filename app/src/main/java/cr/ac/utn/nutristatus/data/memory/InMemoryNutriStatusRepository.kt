package cr.ac.utn.nutristatus.data.memory

import android.content.Context
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import cr.ac.utn.nutristatus.domain.model.Specialist
import cr.ac.utn.nutristatus.domain.repository.AppointmentRepository
import cr.ac.utn.nutristatus.domain.repository.LocationRepository
import org.json.JSONArray

class InMemoryNutriStatusRepository(private val context: Context? = null) : AppointmentRepository, LocationRepository {

    private val specialistsList: List<Specialist> = loadSpecialists()
    private val appointmentsList: MutableList<Appointment> = loadAppointments().toMutableList()
    private val locationsList: List<ClinicLocation> = loadLocations()

    private fun loadSpecialists(): List<Specialist> {
        return try {
            val jsonString = context?.assets?.open("specialists.json")?.bufferedReader().use { it?.readText() }
            if (jsonString != null) {
                val jsonArray = JSONArray(jsonString)
                val list = mutableListOf<Specialist>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val daysArray = obj.getJSONArray("availableDays")
                    val days = mutableListOf<String>()
                    for (j in 0 until daysArray.length()) {
                        days.add(daysArray.getString(j))
                    }
                    list.add(
                        Specialist(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            specialty = obj.getString("specialty"),
                            rating = obj.getDouble("rating").toFloat(),
                            imageUrl = obj.getString("imageUrl"),
                            bio = obj.getString("bio"),
                            consultationFee = obj.getDouble("consultationFee"),
                            availableDays = days
                        )
                    )
                }
                list
            } else {
                getDefaultSpecialists()
            }
        } catch (_: Exception) {
            getDefaultSpecialists()
        }
    }

    private fun loadAppointments(): List<Appointment> {
        return try {
            val jsonString = context?.assets?.open("appointments.json")?.bufferedReader().use { it?.readText() }
            if (jsonString != null) {
                val jsonArray = JSONArray(jsonString)
                val list = mutableListOf<Appointment>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        Appointment(
                            id = obj.getString("id"),
                            patientId = obj.getString("patientId"),
                            patientName = obj.getString("patientName"),
                            specialistId = obj.getString("specialistId"),
                            specialistName = obj.getString("specialistName"),
                            specialistSpecialty = obj.getString("specialistSpecialty"),
                            date = obj.getString("date"),
                            time = obj.getString("time"),
                            status = AppointmentStatus.valueOf(obj.getString("status")),
                            notes = obj.getString("notes"),
                            cancelReason = if (obj.has("cancelReason") && !obj.isNull("cancelReason")) obj.getString("cancelReason") else null
                        )
                    )
                }
                list
            } else {
                getDefaultAppointments()
            }
        } catch (_: Exception) {
            getDefaultAppointments()
        }
    }

    private fun loadLocations(): List<ClinicLocation> {
        return try {
            val jsonString = context?.assets?.open("locations.json")?.bufferedReader().use { it?.readText() }
            if (jsonString != null) {
                val jsonArray = JSONArray(jsonString)
                val list = mutableListOf<ClinicLocation>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        ClinicLocation(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            address = obj.getString("address"),
                            city = obj.getString("city"),
                            latitude = obj.getDouble("latitude"),
                            longitude = obj.getDouble("longitude"),
                            phoneNumber = obj.getString("phoneNumber"),
                            openingHours = obj.getString("openingHours"),
                            imageUrl = obj.getString("imageUrl")
                        )
                    )
                }
                list
            } else {
                getDefaultLocations()
            }
        } catch (_: Exception) {
            getDefaultLocations()
        }
    }

    private fun getDefaultSpecialists() = listOf(
        Specialist(
            id = "spec_1",
            name = "Dr. Alejandro Morales",
            specialty = "Sports Nutrition",
            rating = 4.9f,
            imageUrl = "https://example.com/alejandro.jpg",
            bio = "Specialist in high-performance sports nutrition and body recomposition.",
            consultationFee = 50.0,
            availableDays = listOf("Monday", "Wednesday", "Friday")
        )
    )

    private fun getDefaultAppointments() = listOf(
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
        )
    )

    private fun getDefaultLocations() = listOf(
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
        )
    )

    override fun getAppointments(): List<Appointment> = appointmentsList

    override fun getAppointmentById(id: String): Appointment? {
        return appointmentsList.find { it.id == id }
    }

    override fun scheduleAppointment(appointment: Appointment): Result<Unit> {
        return try {
            appointmentsList.add(appointment)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateAppointment(appointment: Appointment): Result<Unit> {
        return try {
            val index = appointmentsList.indexOfFirst { it.id == appointment.id }
            if (index != -1) {
                appointmentsList[index] = appointment
                Result.success(Unit)
            } else {
                Result.failure(Exception("Appointment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun cancelAppointment(id: String, reason: String): Result<Unit> {
        return try {
            val index = appointmentsList.indexOfFirst { it.id == id }
            if (index != -1) {
                val app = appointmentsList[index]
                appointmentsList[index] = app.copy(status = AppointmentStatus.CANCELLED, cancelReason = reason)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Appointment not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSpecialists(): List<Specialist> = specialistsList

    override fun getSpecialistById(id: String): Specialist? {
        return specialistsList.find { it.id == id }
    }

    override fun getClinicLocations(): List<ClinicLocation> = locationsList

    override fun getLocationById(id: String): ClinicLocation? {
        return locationsList.find { it.id == id }
    }
}
