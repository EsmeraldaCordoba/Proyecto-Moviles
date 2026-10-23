package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.*

/**
 * Interface defining the repository operations for NutriStatus.
 */
interface NutriStatusRepository {
    // Patient Profile & Weight
    fun getPatientProfile(): PatientProfile
    fun getWeightProgress(): WeightProgress

    // Appointments CRUD
    fun getAppointments(): List<Appointment>
    fun getAppointmentById(id: String): Appointment?
    fun getAppointmentsByStatus(status: AppointmentStatus?): List<Appointment>
    fun searchAppointments(query: String): List<Appointment>
    fun addAppointment(appointment: Appointment): Boolean
    fun updateAppointment(appointment: Appointment): Boolean
    fun cancelAppointment(id: String, reason: String? = null): Boolean
    fun deleteAppointment(id: String): Boolean

    // Nutritionists
    fun getNutritionists(): List<Nutritionist>
    fun getNutritionistById(id: String): Nutritionist?
    fun getNutritionistsByApproach(approach: NutritionistApproach?): List<Nutritionist>

    // Meal / Photo Diary
    fun getMealProgressEntries(): List<MealProgress>
    fun searchMealProgressEntries(query: String): List<MealProgress>
    fun addMealProgress(entry: MealProgress): Boolean
    fun deleteMealProgress(id: String): Boolean

    // Clinic Locations
    fun getClinicLocations(): List<ClinicLocation>
    fun getClinicLocationById(id: String): ClinicLocation?
}
