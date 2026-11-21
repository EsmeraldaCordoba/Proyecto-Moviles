package cr.ac.utn.nutristatus

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cr.ac.utn.nutristatus.data.local.database.NutriStatusDatabase
import cr.ac.utn.nutristatus.data.repository.RoomAppointmentRepository
import cr.ac.utn.nutristatus.domain.model.Appointment
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import cr.ac.utn.nutristatus.util.DateFormatterUtil
import cr.ac.utn.nutristatus.viewmodel.AppointmentViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisteredAppointmentsActivity : AppCompatActivity() {

    private val viewModel: AppointmentViewModel by viewModels {
        val database = NutriStatusDatabase.getDatabase(application)
        val repository = RoomAppointmentRepository(database.appointmentDao())
        AppointmentViewModel.Factory(repository)
    }

    private lateinit var container: LinearLayout
    private lateinit var tvEmptyAppointments: TextView
    private var allAppointments: List<Appointment> = emptyList()
    private var currentSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_appointments)

        val etSearch = findViewById<EditText>(R.id.etSearchAppointments)
        val btnBook = findViewById<Button>(R.id.btnBookAppointment)
        container = findViewById(R.id.layoutAppointmentsContainer)
        tvEmptyAppointments = findViewById(R.id.tvEmptyAppointments)

        btnBook.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString()
                filterAndDisplay()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appointments.collectLatest { list ->
                    allAppointments = list
                    filterAndDisplay()
                }
            }
        }
    }

    private fun filterAndDisplay() {
        val filtered = viewModel.filterAppointments(currentSearchQuery, allAppointments)
        displayAppointments(filtered)
    }

    private fun displayAppointments(list: List<Appointment>) {
        container.removeAllViews()

        if (list.isEmpty()) {
            tvEmptyAppointments.visibility = View.VISIBLE
            return
        }

        tvEmptyAppointments.visibility = View.GONE

        val density = resources.displayMetrics.density
        val paddingPx = (16 * density).toInt()
        val marginPx = (12 * density).toInt()

        for (appointment in list) {
            val cardView = CardView(this).apply {
                radius = 12 * density
                cardElevation = 3 * density
                useCompatPadding = true
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, marginPx)
                }
            }

            val cardContent = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
            }

            val topRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (4 * density).toInt())
                }
            }

            val tvDoctor = TextView(this).apply {
                text = appointment.doctorName
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val statusText = when (appointment.status) {
                AppointmentStatus.CONFIRMED -> getString(R.string.status_confirmed)
                AppointmentStatus.COMPLETED -> getString(R.string.status_completed)
                AppointmentStatus.PENDING -> getString(R.string.status_pending)
                AppointmentStatus.CANCELED -> getString(R.string.status_canceled)
            }

            val tvStatus = TextView(this).apply {
                text = statusText
                textSize = 14f
                setTypeface(null, Typeface.BOLD)
            }

            topRow.addView(tvDoctor)
            topRow.addView(tvStatus)

            val tvDepartment = TextView(this).apply {
                text = appointment.department
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (8 * density).toInt())
                }
            }

            val tvDate = TextView(this).apply {
                text = getString(R.string.label_date_prefix) + " " + DateFormatterUtil.formatToDisplayDate(appointment.date)
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (4 * density).toInt())
                }
            }

            val tvClinic = TextView(this).apply {
                text = getString(R.string.label_clinic_prefix) + " " + appointment.clinicName
                textSize = 14f
            }

            if (appointment.status == AppointmentStatus.COMPLETED) {
                val grayColor = Color.parseColor("#757575")
                tvDoctor.setTextColor(grayColor)
                tvDepartment.setTextColor(grayColor)
                tvDate.setTextColor(grayColor)
                tvClinic.setTextColor(grayColor)
                tvStatus.setTextColor(grayColor)
            } else if (appointment.status == AppointmentStatus.CONFIRMED) {
                val blueColor = Color.parseColor("#1976D2")
                val darkColor = Color.parseColor("#212121")
                val subtextColor = Color.parseColor("#666666")
                tvDoctor.setTextColor(darkColor)
                tvDepartment.setTextColor(subtextColor)
                tvClinic.setTextColor(subtextColor)
                tvDate.setTextColor(blueColor)
                tvStatus.setTextColor(blueColor)
            } else {
                val darkColor = Color.parseColor("#212121")
                val subtextColor = Color.parseColor("#666666")
                tvDoctor.setTextColor(darkColor)
                tvDepartment.setTextColor(subtextColor)
                tvClinic.setTextColor(subtextColor)
                tvDate.setTextColor(subtextColor)
                tvStatus.setTextColor(darkColor)
            }

            cardContent.addView(topRow)
            cardContent.addView(tvDepartment)
            cardContent.addView(tvDate)
            cardContent.addView(tvClinic)

            cardView.addView(cardContent)
            container.addView(cardView)
        }
    }
}
