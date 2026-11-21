package cr.ac.utn.nutristatus.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.nutristatus.data.memory.MemoryDataRepository
import cr.ac.utn.nutristatus.domain.model.NutritionalEntry
import cr.ac.utn.nutristatus.domain.repository.NutritionalDiaryRepository

class NutritionalDiaryViewModel(
    private val repository: NutritionalDiaryRepository = MemoryDataRepository.getInstance()
) : ViewModel() {

    fun addEntry(entry: NutritionalEntry): Boolean {
        return repository.addEntry(entry)
    }

    fun updateEntry(entry: NutritionalEntry): Boolean {
        return repository.updateEntry(entry)
    }

    fun deleteEntry(id: String): Boolean {
        return repository.deleteEntry(id)
    }

    fun loadEntries(): List<NutritionalEntry> {
        return repository.getEntries()
    }

    fun filterEntries(query: String): List<NutritionalEntry> {
        val all = repository.getEntries()
        if (query.isBlank()) return all
        return all.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}
