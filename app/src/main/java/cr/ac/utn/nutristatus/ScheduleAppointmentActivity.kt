package cr.ac.utn.nutristatus

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.util.DateTimeUtils
import cr.ac.utn.nutristatus.viewmodel.AppointmentViewModel
import cr.ac.utn.nutristatus.viewmodel.NutritionistViewModel
import java.util.Calendar

class ScheduleAppointmentActivity : AppCompatActivity() {

    private val repository = MemoryDataRepository()
    private val nutritionistViewModel = NutritionistViewModel(repository)
    private val appointmentViewModel = AppointmentViewModel(repository)

    private var selectedTimeSlot = "09:00 AM"
    private var selectedDateFormatted = "2024-10-24"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_appointment)

        val spinnerSpecialty: Spinner = findViewById(R.id.spinner_specialty)
        val spinnerDoctor: Spinner = findViewById(R.id.spinner_doctor)
        val layoutDatePicker: LinearLayout = findViewById(R.id.layout_date_picker)
        val tvSelectedDate: TextView = findViewById(R.id.tv_selected_date)
        val etNotes: EditText = findViewById(R.id.et_notes)
        val btnSaveAppointment: Button = findViewById(R.id.btn_save_appointment)


        val slot9am: TextView = findViewById(R.id.slot_9am)
        val slot1030am: TextView = findViewById(R.id.slot_1030am)
        val slot2pm: TextView = findViewById(R.id.slot_2pm)
        val slot430pm: TextView = findViewById(R.id.slot_430pm)
        val timeSlots = listOf(slot9am, slot1030am, slot2pm, slot430pm)


        val specialties = listOf(
            getString(R.string.approach_clinical_sports),
            getString(R.string.approach_weight_management),
            getString(R.string.approach_functional_hormonal),
            getString(R.string.approach_endocrinology)
        )
        val specialtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specialties)
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpecialty.adapter = specialtyAdapter


        val doctors = nutritionistViewModel.nutritionists.value.map { it.name }
        val doctorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, doctors)
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDoctor.adapter = doctorAdapter


        layoutDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDateFormatted = DateTimeUtils.formatDateToDisplay(calendar.time)
                    tvSelectedDate.text = selectedDateFormatted
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        timeSlots.forEach { slotView ->
            slotView.setOnClickListener {
                timeSlots.forEach { it.setBackgroundResource(R.drawable.bg_chip_unselected) }
                it.setBackgroundResource(R.drawable.bg_chip_selected)
                selectedTimeSlot = (it as TextView).text.toString()
            }
        }


        btnSaveAppointment.setOnClickListener {
            val doctorName = spinnerDoctor.selectedItem?.toString() ?: "Dr. Carlos Mendoza"
            val selectedDoctor = nutritionistViewModel.nutritionists.value.find { it.name == doctorName }
            val doctorId = selectedDoctor?.id ?: "nutri_1"
            val specialty = spinnerSpecialty.selectedItem?.toString() ?: "Nutrición Clínica y Deportiva"
            val notes = etNotes.text.toString()

            val result = appointmentViewModel.scheduleAppointment(
                nutritionistId = doctorId,
                nutritionistName = doctorName,
                specialty = specialty,
                dateTimeStr = selectedDateFormatted,
                timeSlot = selectedTimeSlot,
                clinicLocationName = selectedDoctor?.clinicAddress ?: "Clínica NutriHealth, Cons. 302",
                notes = notes
            )

            if (result.isSuccess) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_confirmation_title)
                    .setMessage(R.string.dialog_appointment_scheduled_success)
                    .setPositiveButton(R.string.dismiss) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message ?: getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<LinearLayout>(R.id.nav_item_clinics).setOnClickListener {
            startActivity(Intent(this, ClinicLocationActivity::class.java))
            finish()
        }
    }
}
