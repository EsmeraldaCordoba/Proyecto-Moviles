package cr.ac.utn.nutristatus

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.nutristatus.data.local.database.NutriStatusDatabase
import cr.ac.utn.nutristatus.data.repository.RoomAppointmentRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.util.DateFormatterUtil
import cr.ac.utn.nutristatus.viewmodel.AppointmentViewModel
import java.util.Date

class AppointmentActivity : AppCompatActivity() {

    private val viewModel: AppointmentViewModel by viewModels {
        val database = NutriStatusDatabase.getDatabase(application)
        val repository = RoomAppointmentRepository(database.appointmentDao())
        AppointmentViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val spinnerSpecialty = findViewById<Spinner>(R.id.spinnerSpecialty)
        val spinnerDoctor = findViewById<Spinner>(R.id.spinnerDoctor)
        val etDate = findViewById<EditText>(R.id.etDate)
        val etReason = findViewById<EditText>(R.id.etReason)
        val btnSave = findViewById<Button>(R.id.btnSaveAppointment)

        val specialties = arrayOf("Nutrición", "Cardiología", "Pediatría", "Medicina General")
        val doctors = arrayOf("Dr. Juan Pérez", "Dra. María López", "Dr. Carlos Ruiz")

        spinnerSpecialty.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, specialties)
        spinnerDoctor.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, doctors)

        btnSave.setOnClickListener {
            val doctor = spinnerDoctor.selectedItem?.toString() ?: "Dr. Juan Pérez"
            val specialty = spinnerSpecialty.selectedItem?.toString() ?: "Nutrición"
            val dateStr = etDate.text.toString().trim()
            val reason = etReason.text.toString().trim()

            val appointmentDate = if (dateStr.isNotEmpty()) {
                DateFormatterUtil.parseDate(dateStr)
            } else {
                Date()
            }

            val appointment = Appointment(
                id = System.currentTimeMillis().toString(),
                patientId = "P001",
                doctorName = doctor,
                department = specialty,
                date = appointmentDate,
                clinicName = "Clínica Principal",
                status = AppointmentStatus.CONFIRMED,
                reason = reason
            )

            viewModel.scheduleAppointment(appointment) {
                Toast.makeText(this, getString(R.string.toast_appointment_saved), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
