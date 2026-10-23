package cr.ac.utn.nutristatus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.viewmodel.ClinicViewModel

class ClinicLocationActivity : AppCompatActivity() {

    private val repository = MemoryDataRepository()
    private val clinicViewModel = ClinicViewModel(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_location)

        val tvClinicName: TextView = findViewById(R.id.tv_clinic_name)
        val tvClinicOffice: TextView = findViewById(R.id.tv_clinic_office)
        val tvClinicAddress: TextView = findViewById(R.id.tv_clinic_address)
        val tvClinicHours: TextView = findViewById(R.id.tv_clinic_hours)
        val btnGetDirections: Button = findViewById(R.id.btn_get_directions)

        val currentClinic = clinicViewModel.selectedClinic.value
        if (currentClinic != null) {
            tvClinicName.text = currentClinic.name
            tvClinicOffice.text = "${getString(R.string.office)} ${currentClinic.officeNumber}"
            tvClinicAddress.text = currentClinic.address
            tvClinicHours.text = "${getString(R.string.operating_hours)}: ${currentClinic.operatingHours}"
        }


        btnGetDirections.setOnClickListener {
            val clinic = clinicViewModel.selectedClinic.value
            if (clinic != null) {
                val gmmIntentUri = Uri.parse("geo:${clinic.latitude},${clinic.longitude}?q=${Uri.encode(clinic.name)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${clinic.latitude},${clinic.longitude}"))
                    startActivity(browserIntent)
                }
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<LinearLayout>(R.id.nav_item_appointments).setOnClickListener {
            startActivity(Intent(this, ScheduleAppointmentActivity::class.java))
            finish()
        }
    }
}
