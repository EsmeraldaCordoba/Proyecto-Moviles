package cr.ac.utn.nutristatus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.domain.model.Doctor
import cr.ac.utn.nutristatus.domain.model.User
import cr.ac.utn.nutristatus.domain.model.WeightRecord
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    repository: NutriStatusRepository = MemoryDataRepository(),
) : ViewModel() {

    val userProfile: StateFlow<User?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val weightRecord: StateFlow<WeightRecord?> = repository.getWeightRecord()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val assignedDoctor: StateFlow<Doctor?> = repository.getAssignedDoctor()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
