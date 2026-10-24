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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import cr.ac.utn.nutristatus.ui.viewmodel.DiaryViewModel

class DiarioActivity : AppCompatActivity() {

    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diario)

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

        // Search Bar Listener
        val inputSearch = findViewById<EditText>(R.id.input_search)
        inputSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                viewModel.onSearchQueryChanged(query)
                if (query.isNotEmpty()) {
                    showToast("Buscando en diario: $query")
                } else {
                    showToast("Ingrese un término de búsqueda")
                }
                true
            } else {
                false
            }
        }

        // Botón Azul para subir foto
        val btnSubirFoto = findViewById<MaterialCardView>(R.id.btn_subir_foto)
        btnSubirFoto.setOnClickListener {
            showToast(getString(R.string.msg_subir_foto))
        }

        // Setup Diary Entry Card Clicks -> Pop-up with details
        setupDiaryCardClick(
            cardId = R.id.card_diario1,
            title = getString(R.string.diario1_title),
            time = getString(R.string.diario1_time),
            description = getString(R.string.diario1_desc),
        )

        setupDiaryCardClick(
            cardId = R.id.card_diario2,
            title = getString(R.string.diario2_title),
            time = getString(R.string.diario2_time),
            description = getString(R.string.diario2_desc),
        )

        setupDiaryCardClick(
            cardId = R.id.card_diario3,
            title = getString(R.string.diario3_title),
            time = getString(R.string.diario3_time),
            description = getString(R.string.diario3_desc),
        )

        setupDiaryCardClick(
            cardId = R.id.card_diario4,
            title = getString(R.string.diario4_title),
            time = getString(R.string.diario4_time),
            description = getString(R.string.diario4_desc),
        )

        setupDiaryCardClick(
            cardId = R.id.card_diario5,
            title = getString(R.string.diario5_title),
            time = getString(R.string.diario5_time),
            description = getString(R.string.diario5_desc),
        )

        setupDiaryCardClick(
            cardId = R.id.card_diario6,
            title = getString(R.string.diario6_title),
            time = getString(R.string.diario6_time),
            description = getString(R.string.diario6_desc),
        )

        // Footer Navigation
        val btnInicio = findViewById<LinearLayout>(R.id.btn_inicio)
        btnInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        val btnCitas = findViewById<LinearLayout>(R.id.btn_citas)
        btnCitas.setOnClickListener {
            val intent = Intent(this, CitasActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnDiario = findViewById<LinearLayout>(R.id.btn_diario)
        btnDiario.setOnClickListener {
            showToast(getString(R.string.msg_ya_en_diario))
        }

        val btnClinicas = findViewById<LinearLayout>(R.id.btn_clinicas)
        btnClinicas.setOnClickListener {
            showToast(getString(R.string.msg_clinicas))
        }
    }

    private fun setupDiaryCardClick(
        cardId: Int,
        title: String,
        time: String,
        description: String,
    ) {
        val card = findViewById<MaterialCardView>(cardId)
        card.setOnClickListener {
            showDiaryDetailsDialog(title, time, description)
        }
    }

    private fun showDiaryDetailsDialog(
        title: String,
        time: String,
        description: String,
    ) {
        val message = """
            🕒 $time
            
            🥗 $description
        """.trimIndent()

        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.dialog_close)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_add_note)) { _, _ ->
                showToast("Añadiendo nota a $title...")
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
