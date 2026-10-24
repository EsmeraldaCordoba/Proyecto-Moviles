package cr.ac.utn.nutristatus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.domain.model.DiaryEntry
import cr.ac.utn.nutristatus.domain.repository.NutriStatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModel(
    private val repository: NutriStatusRepository = MemoryDataRepository(),
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val diaryEntries: StateFlow<List<DiaryEntry>> = searchQuery
        .flatMapLatest { query ->
            repository.filterDiaryEntries(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }
}
