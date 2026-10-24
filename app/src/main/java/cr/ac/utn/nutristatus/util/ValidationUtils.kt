package cr.ac.utn.nutristatus.util

object ValidationUtils {
    fun isValidDate(dateStr: String): Boolean {
        // Simple regex check for YYYY-MM-DD
        val regex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        return regex.matches(dateStr)
    }

    fun isValidTime(timeStr: String): Boolean {
        // Simple regex check for HH:mm
        val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        return regex.matches(timeStr)
    }
}
