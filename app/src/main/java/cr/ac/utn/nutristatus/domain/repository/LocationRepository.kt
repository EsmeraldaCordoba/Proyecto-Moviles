package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.ClinicLocation

interface LocationRepository {
    fun getClinicLocations(): List<ClinicLocation>
    fun getLocationById(id: String): ClinicLocation?
}
