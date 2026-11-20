package cr.ac.utn.nutristatus

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import cr.ac.utn.nutristatus.domain.model.NutritionalEntry
import cr.ac.utn.nutristatus.util.DateFormatterUtil
import cr.ac.utn.nutristatus.viewmodel.NutritionalDiaryViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class NutritionalDiaryActivity : AppCompatActivity() {

    private val viewModel = NutritionalDiaryViewModel()
    private lateinit var container: LinearLayout
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: EditText

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                val savedUri = saveBitmapToCache(bitmap)
                showAddEntryDialog(savedUri)
            }
        }
    }

    private val pickGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            showAddEntryDialog(uri.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutritional_diary)

        etSearch = findViewById(R.id.etSearchDiary)
        val btnUpload = findViewById<Button>(R.id.btnUploadPhoto)
        container = findViewById(R.id.layoutDiaryContainer)
        tvEmpty = findViewById(R.id.tvEmptyDiary)

        btnUpload.setOnClickListener {
            showImageSourceOptions()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun showImageSourceOptions() {
        val options = arrayOf(
            getString(R.string.option_camera),
            getString(R.string.option_gallery)
        )
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_select_image_source))
            .setItems(options) { _, which ->
                if (which == 0) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePhotoLauncher.launch(intent)
                } else {
                    pickGalleryLauncher.launch("image/*")
                }
            }
            .show()
    }

    private fun showAddEntryDialog(imageUri: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_add_entry_title))

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (16 * resources.displayMetrics.density).toInt()
            setPadding(pad, pad, pad, pad)
        }

        val etTitle = EditText(this).apply {
            hint = getString(R.string.hint_entry_title)
        }
        val etDescription = EditText(this).apply {
            hint = getString(R.string.hint_entry_description)
        }

        layout.addView(etTitle)
        layout.addView(etDescription)
        builder.setView(layout)

        builder.setPositiveButton(getString(R.string.btn_save)) { _, _ ->
            val titleText = etTitle.text.toString().trim()
            val descText = etDescription.text.toString().trim()
            if (titleText.isNotEmpty()) {
                val entry = NutritionalEntry(
                    id = System.currentTimeMillis().toString(),
                    title = titleText,
                    description = descText,
                    imageUri = imageUri,
                    timestamp = Date()
                )
                viewModel.addEntry(entry)
                loadData()
            }
        }
        builder.setNegativeButton(getString(R.string.btn_cancel), null)
        builder.show()
    }

    private fun showEditEntryDialog(entry: NutritionalEntry) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_edit_entry_title))

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (16 * resources.displayMetrics.density).toInt()
            setPadding(pad, pad, pad, pad)
        }

        val etTitle = EditText(this).apply {
            hint = getString(R.string.hint_entry_title)
            setText(entry.title)
        }
        val etDescription = EditText(this).apply {
            hint = getString(R.string.hint_entry_description)
            setText(entry.description)
        }

        layout.addView(etTitle)
        layout.addView(etDescription)
        builder.setView(layout)

        builder.setPositiveButton(getString(R.string.btn_save)) { _, _ ->
            val titleText = etTitle.text.toString().trim()
            val descText = etDescription.text.toString().trim()
            if (titleText.isNotEmpty()) {
                val updatedEntry = entry.copy(
                    title = titleText,
                    description = descText
                )
                showUpdateConfirmationDialog(updatedEntry)
            }
        }
        builder.setNegativeButton(getString(R.string.btn_cancel), null)
        builder.show()
    }

    private fun showUpdateConfirmationDialog(entry: NutritionalEntry) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_update_confirm_title))
            .setMessage(getString(R.string.dialog_update_confirm_message))
            .setPositiveButton(getString(R.string.btn_confirm)) { _, _ ->
                viewModel.updateEntry(entry)
                loadData()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun showDeleteConfirmationDialog(entry: NutritionalEntry) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_delete_confirm_title))
            .setMessage(getString(R.string.dialog_delete_confirm_message))
            .setPositiveButton(getString(R.string.btn_confirm)) { _, _ ->
                viewModel.deleteEntry(entry.id)
                loadData()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    private fun saveBitmapToCache(bitmap: Bitmap): String {
        val file = File(cacheDir, "diary_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        return Uri.fromFile(file).toString()
    }

    private fun loadData() {
        val query = if (::etSearch.isInitialized) etSearch.text.toString() else ""
        val entries = viewModel.filterEntries(query)
        displayEntries(entries)
    }

    private fun filterList(query: String) {
        val filtered = viewModel.filterEntries(query)
        displayEntries(filtered)
    }

    private fun displayEntries(list: List<NutritionalEntry>) {
        container.removeAllViews()

        if (list.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            return
        }

        tvEmpty.visibility = View.GONE

        val density = resources.displayMetrics.density
        val paddingPx = (16 * density).toInt()
        val marginPx = (12 * density).toInt()

        for (entry in list) {
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

            if (!entry.imageUri.isNullOrEmpty()) {
                val ivPhoto = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (200 * density).toInt()
                    ).apply {
                        setMargins(0, 0, 0, (8 * density).toInt())
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setImageURI(Uri.parse(entry.imageUri))
                }
                cardContent.addView(ivPhoto)
            }

            val tvTitle = TextView(this).apply {
                text = entry.title
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.parseColor("#212121"))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (4 * density).toInt())
                }
            }

            val tvDesc = TextView(this).apply {
                text = entry.description
                textSize = 14f
                setTextColor(Color.parseColor("#666666"))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, (8 * density).toInt())
                }
            }

            val tvTime = TextView(this).apply {
                text = DateFormatterUtil.formatToDisplayDateTime(entry.timestamp)
                textSize = 12f
                setTextColor(Color.parseColor("#999999"))
            }

            val actionRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, (8 * density).toInt(), 0, 0)
                }
            }

            val btnEdit = Button(this).apply {
                text = getString(R.string.btn_edit)
                textSize = 12f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    setMargins(0, 0, (4 * density).toInt(), 0)
                }
                setOnClickListener {
                    showEditEntryDialog(entry)
                }
            }

            val btnDelete = Button(this).apply {
                text = getString(R.string.btn_delete)
                textSize = 12f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    setMargins((4 * density).toInt(), 0, 0, 0)
                }
                setOnClickListener {
                    showDeleteConfirmationDialog(entry)
                }
            }

            actionRow.addView(btnEdit)
            actionRow.addView(btnDelete)

            cardContent.addView(tvTitle)
            cardContent.addView(tvDesc)
            cardContent.addView(tvTime)
            cardContent.addView(actionRow)

            cardView.addView(cardContent)
            container.addView(cardView)
        }
    }
}
