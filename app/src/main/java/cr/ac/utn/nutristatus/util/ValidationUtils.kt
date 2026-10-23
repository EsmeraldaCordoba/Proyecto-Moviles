package cr.ac.utn.nutristatus.util

/**
 * Utility object for validating user input fields.
 */
object ValidationUtils {

    /**
     * Validates appointment creation inputs.
     */
    fun validateAppointmentInput(
        nutritionistId: String,
        dateTimeStr: String,
        timeSlot: String
    ): Result<Unit> {
        if (nutritionistId.isBlank()) {
            return Result.failure(IllegalArgumentException("Doctor must be selected."))
        }
        if (dateTimeStr.isBlank()) {
            return Result.failure(IllegalArgumentException("Date must be selected."))
        }
        if (timeSlot.isBlank()) {
            return Result.failure(IllegalArgumentException("Time slot must be selected."))
        }
        return Result.success(Unit)
    }

    /**
     * Validates meal diary creation inputs.
     */
    fun validateMealInput(title: String, mealCategory: String): Result<Unit> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Meal title cannot be empty."))
        }
        if (mealCategory.isBlank()) {
            return Result.failure(IllegalArgumentException("Meal category must be selected."))
        }
        return Result.success(Unit)
    }
}
