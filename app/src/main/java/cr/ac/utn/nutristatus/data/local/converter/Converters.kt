package cr.ac.utn.nutristatus.data.local.converter

import androidx.room.TypeConverter
import cr.ac.utn.nutristatus.domain.model.AppointmentStatus
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromAppointmentStatus(status: AppointmentStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toAppointmentStatus(value: String?): AppointmentStatus? {
        return value?.let { AppointmentStatus.valueOf(it) }
    }
}
