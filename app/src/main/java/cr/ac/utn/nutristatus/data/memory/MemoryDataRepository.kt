package cr.ac.utn.nutristatus.data.memory

import cr.ac.utn.nutristatus.domain.model.*
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import java.util.UUID

/**
 * In-memory implementation of [NutriStatusRepository] for local testing and offline execution.
 */
class MemoryDataRepository : NutriStatusRepository {

    private val patientProfile = PatientProfile(
        id = "patient_1",
        name = "Ana García",
        activePlan = "Plan Nutricional Activo",
        assignedNutritionistId = "nutri_1"
    )

    private val weightProgress = WeightProgress(
        currentWeightKg = 68.5,
        goalWeightKg = 65.0,
        monthlyChangeKg = -2.1,
        weeklyEntries = listOf(
            "Lun" to 70.6,
            "Mar" to 70.1,
            "Mié" to 69.5,
            "Jue" to 69.1,
            "Vie" to 68.9,
            "Sáb" to 68.7,
            "Dom" to 68.5
        )
    )

    private val nutritionists = mutableListOf(
        Nutritionist(
            id = "nutri_1",
            name = "Dr. Carlos Mendoza",
            specialty = "Nutrición Clínica y Deportiva",
            approach = NutritionistApproach.CLINICAL_AND_SPORTS,
            bio = "Especialista en nutrición deportiva y recomposición corporal.",
            rating = 4.9,
            reviewCount = 124,
            clinicAddress = "Clínica NutriHealth, Cons. 302",
            operatingHours = "Lun – Vie: 09:00 – 17:00 hrs"
        ),
        Nutritionist(
            id = "nutri_2",
            name = "Dra. Laura Silva",
            specialty = "Endocrinología y Metabolismo",
            approach = NutritionistApproach.ENDOCRINOLOGY_METABOLISM,
            bio = "Especialista en metabolismo y salud hormonal.",
            rating = 4.8,
            reviewCount = 98,
            clinicAddress = "Centro Médico San José, Cons. 104",
            operatingHours = "Lun – Jue: 08:00 – 16:00 hrs"
        ),
        Nutritionist(
            id = "nutri_3",
            name = "Lic. Mariana Rojas",
            specialty = "Control de Peso y Hábitos",
            approach = NutritionistApproach.WEIGHT_MANAGEMENT,
            bio = "Nutricionista enfocada en control de peso y educación nutricional.",
            rating = 4.9,
            reviewCount = 86,
            clinicAddress = "Clínica NutriHealth, Cons. 305",
            operatingHours = "Lun – Sáb: 09:00 – 15:00 hrs"
        ),
        Nutritionist(
            id = "nutri_4",
            name = "Dra. Sofía Vargas",
            specialty = "Nutrición Funcional Hormonal",
            approach = NutritionistApproach.FUNCTIONAL_HORMONAL_BALANCE,
            bio = "Nutrición funcional para el equilibrio hormonal y salud femenina.",
            rating = 5.0,
            reviewCount = 110,
            clinicAddress = "Clínica Vitalia, Cons. 201",
            operatingHours = "Mar – Vie: 10:00 – 18:00 hrs"
        )
    )

    private val appointments = mutableListOf(
        Appointment(
            id = "apt_1",
            patientName = "Ana García",
            nutritionistId = "nutri_1",
            nutritionistName = "Dr. Carlos Mendoza",
            specialty = "Nutrición Clínica y Deportiva",
            dateTime = "2024-10-15 10:00 AM",
            timeSlot = "10:00 AM",
            status = AppointmentStatus.CONFIRMED,
            clinicLocationName = "Clínica NutriHealth, Consultorio 302",
            notes = "Consulta de seguimiento de dieta keto y medición corporal"
        ),
        Appointment(
            id = "apt_2",
            patientName = "Ana García",
            nutritionistId = "nutri_2",
            nutritionistName = "Dra. Laura Silva",
            specialty = "Endocrinología y Metabolismo",
            dateTime = "2024-09-28 04:30 PM",
            timeSlot = "04:30 PM",
            status = AppointmentStatus.COMPLETED,
            clinicLocationName = "Centro Médico San José, Consultorio 104",
            notes = "Evaluación metabólica inicial"
        ),
        Appointment(
            id = "apt_3",
            patientName = "Ana García",
            nutritionistId = "nutri_3",
            nutritionistName = "Lic. Mariana Rojas",
            specialty = "Control de Peso y Hábitos",
            dateTime = "2024-11-02 09:00 AM",
            timeSlot = "09:00 AM",
            status = AppointmentStatus.PENDING,
            clinicLocationName = "Clínica NutriHealth, Consultorio 305",
            notes = "Revisión de plan de alimentación semanal"
        )
    )

    private val mealProgressEntries = mutableListOf(
        MealProgress(
            id = "meal_1",
            title = "Desayuno Balanceado",
            description = "Avena con bayas, chía y proteína en polvo",
            mealTime = "08:30 AM",
            mealCategory = "Desayuno",
            date = "2024-10-14"
        ),
        MealProgress(
            id = "meal_2",
            title = "Almuerzo Proteico",
            description = "Pechuga a la plancha, quinoa y aguacate",
            mealTime = "02:00 PM",
            mealCategory = "Almuerzo",
            date = "2024-10-14"
        ),
        MealProgress(
            id = "meal_3",
            title = "Merienda Nutritiva",
            description = "Yogur griego con almendras y miel",
            mealTime = "05:30 PM",
            mealCategory = "Merienda",
            date = "2024-10-14"
        ),
        MealProgress(
            id = "meal_4",
            title = "Cena Ligera",
            description = "Salmón al horno con vegetales salteados",
            mealTime = "08:30 PM",
            mealCategory = "Cena",
            date = "2024-10-14"
        )
    )

    private val clinicLocations = mutableListOf(
        ClinicLocation(
            id = "clinic_1",
            name = "Clínica NutriHealth",
            address = "Av. Central, Consultorio 302, San José",
            officeNumber = "302",
            latitude = 9.9333,
            longitude = -84.0833,
            phone = "+506 2222-3333",
            operatingHours = "08:00 – 18:00"
        ),
        ClinicLocation(
            id = "clinic_2",
            name = "Centro Médico San José",
            address = "Calle 5, Paseo Colón, San José",
            officeNumber = "104",
            latitude = 9.9350,
            longitude = -84.0900,
            phone = "+506 2255-4444",
            operatingHours = "08:00 – 17:00"
        ),
        ClinicLocation(
            id = "clinic_3",
            name = "Clínica Vitalia",
            address = "Barrio Escalante, San José",
            officeNumber = "201",
            latitude = 9.9370,
            longitude = -84.0650,
            phone = "+506 2288-5555",
            operatingHours = "09:00 – 18:00"
        )
    )

    // Patient & Weight
    override fun getPatientProfile(): PatientProfile = patientProfile
    override fun getWeightProgress(): WeightProgress = weightProgress

    // Appointments
    override fun getAppointments(): List<Appointment> = appointments.toList()

    override fun getAppointmentById(id: String): Appointment? =
        appointments.find { it.id == id }

    override fun getAppointmentsByStatus(status: AppointmentStatus?): List<Appointment> {
        if (status == null) return getAppointments()
        return appointments.filter { it.status == status }
    }

    override fun searchAppointments(query: String): List<Appointment> {
        if (query.isBlank()) return getAppointments()
        val lower = query.lowercase()
        return appointments.filter {
            it.nutritionistName.lowercase().contains(lower) ||
                    it.specialty.lowercase().contains(lower) ||
                    it.notes.lowercase().contains(lower)
        }
    }

    override fun addAppointment(appointment: Appointment): Boolean {
        val newAppointment = if (appointment.id.isBlank()) {
            appointment.copy(id = UUID.randomUUID().toString())
        } else {
            appointment
        }
        return appointments.add(newAppointment)
    }

    override fun updateAppointment(appointment: Appointment): Boolean {
        val index = appointments.indexOfFirst { it.id == appointment.id }
        if (index != -1) {
            appointments[index] = appointment
            return true
        }
        return false
    }

    override fun cancelAppointment(id: String, reason: String?): Boolean {
        val index = appointments.indexOfFirst { it.id == id }
        if (index != -1) {
            val existing = appointments[index]
            appointments[index] = existing.copy(
                status = AppointmentStatus.CANCELLED,
                cancellationReason = reason
            )
            return true
        }
        return false
    }

    override fun deleteAppointment(id: String): Boolean {
        return appointments.removeIf { it.id == id }
    }

    // Nutritionists
    override fun getNutritionists(): List<Nutritionist> = nutritionists.toList()

    override fun getNutritionistById(id: String): Nutritionist? =
        nutritionists.find { it.id == id }

    override fun getNutritionistsByApproach(approach: NutritionistApproach?): List<Nutritionist> {
        if (approach == null) return getNutritionists()
        return nutritionists.filter { it.approach == approach }
    }

    // Meal / Photo Diary
    override fun getMealProgressEntries(): List<MealProgress> = mealProgressEntries.toList()

    override fun searchMealProgressEntries(query: String): List<MealProgress> {
        if (query.isBlank()) return getMealProgressEntries()
        val lower = query.lowercase()
        return mealProgressEntries.filter {
            it.title.lowercase().contains(lower) ||
                    it.description.lowercase().contains(lower) ||
                    it.mealCategory.lowercase().contains(lower)
        }
    }

    override fun addMealProgress(entry: MealProgress): Boolean {
        val newEntry = if (entry.id.isBlank()) {
            entry.copy(id = UUID.randomUUID().toString())
        } else {
            entry
        }
        return mealProgressEntries.add(newEntry)
    }

    override fun deleteMealProgress(id: String): Boolean {
        return mealProgressEntries.removeIf { it.id == id }
    }

    // Clinics
    override fun getClinicLocations(): List<ClinicLocation> = clinicLocations.toList()

    override fun getClinicLocationById(id: String): ClinicLocation? =
        clinicLocations.find { it.id == id }
}
