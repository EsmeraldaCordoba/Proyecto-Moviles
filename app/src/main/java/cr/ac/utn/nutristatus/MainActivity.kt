package cr.ac.utn.nutristatus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val headerBar = findViewById<View>(R.id.header_bar)
        val footerBar = findViewById<View>(R.id.footer_bar)

        // Adjust for system status bar & navigation bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            headerBar.setPadding(
                headerBar.paddingLeft,
                systemBars.top + 16,
                headerBar.paddingRight,
                headerBar.paddingBottom,
            )

            footerBar.setPadding(
                footerBar.paddingLeft,
                footerBar.paddingTop,
                footerBar.paddingRight,
                systemBars.bottom + 12,
            )

            insets
        }

        // Notification Button Listener
        val btnNotifications = findViewById<ImageButton>(R.id.btn_notifications)
        btnNotifications.setOnClickListener {
            showToast(getString(R.string.msg_notificaciones))
        }

        // Footer Navigation Buttons Listeners
        val btnInicio = findViewById<LinearLayout>(R.id.btn_inicio)
        btnInicio.setOnClickListener {
            showToast(getString(R.string.msg_inicio))
        }

        val btnCitas = findViewById<LinearLayout>(R.id.btn_citas)
        btnCitas.setOnClickListener {
            val intent = Intent(this, CitasActivity::class.java)
            startActivity(intent)
        }

        val btnDiario = findViewById<LinearLayout>(R.id.btn_diario)
        btnDiario.setOnClickListener {
            showToast(getString(R.string.msg_diario))
        }

        val btnClinicas = findViewById<LinearLayout>(R.id.btn_clinicas)
        btnClinicas.setOnClickListener {
            showToast(getString(R.string.msg_clinicas))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
