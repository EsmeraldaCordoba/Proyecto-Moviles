package cr.ac.utn.nutristatus.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.utn.nutristatus.data.local.converter.Converters
import cr.ac.utn.nutristatus.data.local.dao.AppointmentDao
import cr.ac.utn.nutristatus.data.local.entity.AppointmentEntity

@Database(entities = [AppointmentEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NutriStatusDatabase : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: NutriStatusDatabase? = null

        fun getDatabase(context: Context): NutriStatusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriStatusDatabase::class.java,
                    "nutristatus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
