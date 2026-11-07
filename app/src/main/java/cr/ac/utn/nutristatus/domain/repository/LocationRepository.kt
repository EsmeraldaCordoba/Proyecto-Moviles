package cr.ac.utn.nutristatus.domain.repository

import cr.ac.utn.nutristatus.domain.model.Location

interface LocationRepository {
    fun getClinicLocation(): Location?
}