package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.domain.model.Nutritionist
import cr.ac.utn.nutristatus.domain.model.NutritionistApproach
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import cr.ac.utn.nutristatus.util.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for directory of specialists and approach filtering.
 */
class NutritionistViewModel(
    private val repository: NutriStatusRepository
) : ViewModel() {

    private val _nutritionists = MutableStateFlow<List<Nutritionist>>(emptyList())
    val nutritionists: StateFlow<List<Nutritionist>> = _nutritionists.asStateFlow()

    private val _selectedApproach = MutableStateFlow<NutritionistApproach?>(null)
    val selectedApproach: StateFlow<NutritionistApproach?> = _selectedApproach.asStateFlow()

    init {
        loadNutritionists()
    }

    fun loadNutritionists() {
        _nutritionists.value = repository.getNutritionistsByApproach(_selectedApproach.value)
    }

    fun filterByApproach(approach: NutritionistApproach?) {
        _selectedApproach.value = approach
        loadNutritionists()
    }

    fun getAvailableTimeSlots(): List<String> = AppConstants.AVAILABLE_TIME_SLOTS
}
