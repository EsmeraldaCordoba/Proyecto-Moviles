package cr.ac.utn.nutristatus.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility object for formatting dates, times, and checking appointment rules.
 */
object DateTimeUtils {

    private const val DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm a"
    private const val DISPLAY_DATE_FORMAT = "EEEE, d 'de' MMMM yyyy"

    /**
     * Formats a date object to display format.
     */
    fun formatDateToDisplay(date: Date): String {
        val sdf = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Formats current date to ISO string (yyyy-MM-dd).
     */
    fun getCurrentDateIso(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Checks if a scheduled date/time string is within [cutoffHours] hours from now.
     * Used for the 24-hour cancellation warning dialog requirement.
     */
    fun isWithinHours(dateTimeStr: String, cutoffHours: Int = 24): Boolean {
        return try {
            val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.US)
            val scheduledDate = sdf.parse(dateTimeStr) ?: return false
            val now = Date()
            val diffInMillis = scheduledDate.time - now.time
            val diffInHours = diffInMillis / (1000 * 60 * 60)
            diffInHours in 0 until cutoffHours
        } catch (e: Exception) {
            false
        }
    }
}
