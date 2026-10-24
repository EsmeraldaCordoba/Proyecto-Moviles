package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.ClinicLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getClinicLocations(): Flow<List<ClinicLocation>>
    suspend fun getLocationById(id: String): ClinicLocation?
}
