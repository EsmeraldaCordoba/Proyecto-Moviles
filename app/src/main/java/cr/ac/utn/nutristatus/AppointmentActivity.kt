package cr.ac.utn.nutristatus

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AppointmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_appointment)

        val btnSave = findViewById<Button>(R.id.btnSaveAppointment)
        btnSave.setOnClickListener {
            Toast.makeText(this, getString(R.string.toast_appointment_saved), Toast.LENGTH_SHORT).show()
        }
    }
}