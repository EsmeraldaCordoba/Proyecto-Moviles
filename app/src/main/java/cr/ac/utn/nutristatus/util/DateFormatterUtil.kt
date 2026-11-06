package cr.ac.utn.nutristatus.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatterUtil {
    fun formatToDisplayDate(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}