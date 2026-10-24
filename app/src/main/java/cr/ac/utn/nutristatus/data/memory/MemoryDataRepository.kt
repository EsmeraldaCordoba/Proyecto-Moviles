package cr.ac.utn.nutristatus.data.memory

import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.domain.model.DiaryEntry
import cr.ac.utn.nutristatus.domain.model.Doctor
import cr.ac.utn.nutristatus.domain.model.MonthlyWeight
import cr.ac.utn.nutristatus.domain.model.User
import cr.ac.utn.nutristatus.domain.model.WeightRecord
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MemoryDataRepository : NutriStatusRepository {

    private val userState = MutableStateFlow(
        User(
            id = "u_101",
            name = "María González",
            subtitle = "NutriStatus Patient",
        )
    )

    private val weightState = MutableStateFlow(
        WeightRecord(
            id = "w_101",
            currentWeightKg = 68.5f,
            targetWeightKg = 65.0f,
            weightDiffKg = -7.5f,
            lastUpdatedText = "2 days ago",
            monthlyHistory = listOf(
                MonthlyWeight("Jan", 76.0f),
                MonthlyWeight("Feb", 74.2f),
                MonthlyWeight("Mar", 72.5f),
                MonthlyWeight("Apr", 71.0f),
                MonthlyWeight("May", 69.8f),
                MonthlyWeight("Jun", 68.5f),
            ),
        )
    )

    private val doctorState = MutableStateFlow(
        Doctor(
            id = "doc_1",
            name = "Dr. Alejandro Morales",
            specialty = "Clinical Nutrition Specialist",
            operatingHours = "Mon - Fri: 8:00 AM - 5:00 PM",
            clinicName = "NutriSalud Central Clinic, San José",
        )
    )

    private val appointmentsList = MutableStateFlow(
        listOf(
            Appointment(
                id = "apt_1",
                doctorName = "Dr. Alejandro Morales",
                department = "Clinical Nutrition",
                dateTime = "15 Oct 2025 - 10:00 AM",
                clinicName = "NutriSalud Central Clinic, San José",
                status = AppointmentStatus.CONFIRMED,
                notes = "Bring weekly meal log. Arrive 10 minutes prior.",
            ),
            Appointment(
                id = "apt_2",
                doctorName = "Dra. Lucía Fernández",
                department = "Sports Nutrition",
                dateTime = "28 Oct 2025 - 02:30 PM",
                clinicName = "NutriSalud Escazú Clinic",
                status = AppointmentStatus.CONFIRMED,
                notes = "Body composition assessment and sports meal plan.",
            ),
            Appointment(
                id = "apt_3",
                doctorName = "Dr. Carlos Gutiérrez",
                department = "Weight Control & Metabolism",
                dateTime = "01 Sep 2025 - 09:00 AM",
                clinicName = "NutriSalud Central Clinic, San José",
                status = AppointmentStatus.COMPLETED,
                notes = "Follow-up consultation completed successfully.",
            ),
            Appointment(
                id = "apt_4",
                doctorName = "Dra. Sofía Vargas",
                department = "Functional Nutrition",
                dateTime = "15 Aug 2025 - 11:15 AM",
                clinicName = "NutriSalud Heredia Clinic",
                status = AppointmentStatus.COMPLETED,
                notes = "Initial functional nutrition assessment completed.",
            ),
        )
    )

    private val diaryEntriesList = MutableStateFlow(
        listOf(
            DiaryEntry(
                id = "d_1",
                title = "Healthy Breakfast",
                description = "Oatmeal cooked with almond milk, fresh strawberries, blueberries and chia.",
                timestamp = "Today, 08:30 AM",
                mealType = "Breakfast",
            ),
            DiaryEntry(
                id = "d_2",
                title = "Chicken & Veggie Lunch",
                description = "Grilled chicken breast with steamed broccoli, carrots and quinoa.",
                timestamp = "Yesterday, 01:15 PM",
                mealType = "Lunch",
            ),
            DiaryEntry(
                id = "d_3",
                title = "Protein Shake Snack",
                description = "Vanilla whey protein shake with banana, spinach and peanut butter.",
                timestamp = "12 Oct, 04:30 PM",
                mealType = "Snack",
            ),
            DiaryEntry(
                id = "d_4",
                title = "Tuna Salad Dinner",
                description = "Spinach salad, tuna in water, cherry tomatoes, avocado and olive oil.",
                timestamp = "11 Oct, 07:45 PM",
                mealType = "Dinner",
            ),
            DiaryEntry(
                id = "d_5",
                title = "Fruit Snack",
                description = "Green apple slices served with sliced almonds.",
                timestamp = "10 Oct, 10:00 AM",
                mealType = "Snack",
            ),
            DiaryEntry(
                id = "d_6",
                title = "Light Soup Dinner",
                description = "Vegetable soup with shredded chicken breast and whole grain toast.",
                timestamp = "09 Oct, 08:00 PM",
                mealType = "Dinner",
            ),
        )
    )

    override fun getUserProfile(): Flow<User> = userState.asStateFlow()

    override fun getWeightRecord(): Flow<WeightRecord> = weightState.asStateFlow()

    override fun getAssignedDoctor(): Flow<Doctor> = doctorState.asStateFlow()

    override fun getAppointments(): Flow<List<Appointment>> = appointmentsList.asStateFlow()

    override fun filterAppointments(query: String): Flow<List<Appointment>> {
        return appointmentsList.map { list ->
            if (query.isBlank()) list
            else list.filter {
                it.doctorName.contains(query, ignoreCase = true) ||
                    it.department.contains(query, ignoreCase = true) ||
                    it.clinicName.contains(query, ignoreCase = true)
            }
        }
    }

    override fun getDiaryEntries(): Flow<List<DiaryEntry>> = diaryEntriesList.asStateFlow()

    override fun filterDiaryEntries(query: String): Flow<List<DiaryEntry>> {
        return diaryEntriesList.map { list ->
            if (query.isBlank()) list
            else list.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.mealType.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun addDiaryEntry(entry: DiaryEntry) {
        val current = diaryEntriesList.value.toMutableList()
        current.add(0, entry)
        diaryEntriesList.value = current
    }

    override suspend fun scheduleAppointment(appointment: Appointment) {
        val current = appointmentsList.value.toMutableList()
        current.add(0, appointment)
        appointmentsList.value = current
    }
}
