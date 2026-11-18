package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.NutritionalEntry

interface NutritionalDiaryRepository {
    fun addEntry(entry: NutritionalEntry): Boolean
    fun getEntries(): List<NutritionalEntry>
}
