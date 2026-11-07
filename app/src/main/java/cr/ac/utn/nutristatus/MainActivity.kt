package cr.ac.utn.nutristatus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAppointment = findViewById<Button>(R.id.btnGoToAppointment)
        val btnLocation = findViewById<Button>(R.id.btnGoToLocation)

        btnAppointment.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }

        btnLocation.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }
    }
}