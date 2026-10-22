package cr.ac.utn.nutristatus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CitasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas)

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

        // Search bar action
        val inputSearch = findViewById<EditText>(R.id.input_search)
        inputSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                if (query.isNotEmpty()) {
                    showToast("Buscando: $query")
                } else {
                    showToast("Ingrese un término de búsqueda")
                }
                true
            } else {
                false
            }
        }

        // Date filter button
        val btnFecha = findViewById<MaterialCardView>(R.id.btn_fecha)
        btnFecha.setOnClickListener {
            showToast(getString(R.string.msg_filtro_fecha))
        }

        // Filters button
        val btnFiltros = findViewById<MaterialCardView>(R.id.btn_filtros)
        btnFiltros.setOnClickListener {
            showToast(getString(R.string.msg_filtros))
        }

        // Agendar Nueva Cita button
        val btnAgendar = findViewById<MaterialCardView>(R.id.btn_agendar)
        btnAgendar.setOnClickListener {
            showToast(getString(R.string.msg_agendar))
        }

        // Appointment Cards click listeners -> Show detail dialog
        setupAppointmentCardClick(
            R.id.card_cita1,
            doctor = getString(R.string.cita1_doctor),
            dept = getString(R.string.cita1_dept),
            date = getString(R.string.cita1_date),
            clinic = getString(R.string.cita1_clinic),
            status = getString(R.string.status_confirmada),
            notes = "Traer registro alimentario semanal. Llegar 10 minutos antes.",
        )

        setupAppointmentCardClick(
            R.id.card_cita2,
            doctor = getString(R.string.cita2_doctor),
            dept = getString(R.string.cita2_dept),
            date = getString(R.string.cita2_date),
            clinic = getString(R.string.cita2_clinic),
            status = getString(R.string.status_confirmada),
            notes = "Evaluación de composición corporal y plan Deportivo.",
        )

        setupAppointmentCardClick(
            R.id.card_cita3,
            doctor = getString(R.string.cita3_doctor),
            dept = getString(R.string.cita3_dept),
            date = getString(R.string.cita3_date),
            clinic = getString(R.string.cita3_clinic),
            status = getString(R.string.status_completada),
            notes = "Consulta de seguimiento completada con éxito.",
        )

        setupAppointmentCardClick(
            R.id.card_cita4,
            doctor = getString(R.string.cita4_doctor),
            dept = getString(R.string.cita4_dept),
            date = getString(R.string.cita4_date),
            clinic = getString(R.string.cita4_clinic),
            status = getString(R.string.status_completada),
            notes = "Consulta inicial de nutrición funcional completada.",
        )

        // Footer Navigation Buttons
        val btnInicio = findViewById<LinearLayout>(R.id.btn_inicio)
        btnInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        val btnCitas = findViewById<LinearLayout>(R.id.btn_citas)
        btnCitas.setOnClickListener {
            showToast(getString(R.string.msg_ya_en_citas))
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

    private fun setupAppointmentCardClick(
        cardId: Int,
        doctor: String,
        dept: String,
        date: String,
        clinic: String,
        status: String,
        notes: String,
    ) {
        val card = findViewById<MaterialCardView>(cardId)
        card.setOnClickListener {
            showAppointmentDetailsDialog(doctor, dept, date, clinic, status, notes)
        }
    }

    private fun showAppointmentDetailsDialog(
        doctor: String,
        dept: String,
        date: String,
        clinic: String,
        status: String,
        notes: String,
    ) {
        val message = """
            🩺 Especialista: $doctor
            📌 Departamento: $dept
            📌 Estado: $status
            
            📅 Fecha y Hora:
            $date
            
            🏥 Ubicación:
            $clinic
            
            📝 Notas:
            $notes
        """.trimIndent()

        MaterialAlertDialogBuilder(this)
            .setTitle("Detalles de la Cita")
            .setMessage(message)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Ver Ubicación") { _, _ ->
                showToast("Yendo a ubicación de $clinic")
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
