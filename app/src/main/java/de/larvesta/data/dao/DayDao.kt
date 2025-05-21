package de.larvesta.data.dao

import androidx.room.*
import de.larvesta.data.model.DayEntity
import java.time.LocalDate
import java.util.Date

@Dao
interface DayDao {
    @Insert
    suspend fun insert(day: DayEntity)

    @Update
    suspend fun update(day: DayEntity)

    @Query("SELECT * FROM day_table")
    suspend fun getAllDays(): List<DayEntity>

    @Query("SELECT * FROM day_table WHERE id = :id")
    suspend fun getDayById(id: Int): DayEntity?

    @Query("SELECT * FROM day_table WHERE date = :date") // TODO: date = :date geht nicht
    suspend fun getDayByDate(date: LocalDate): DayEntity?

    @Delete
    suspend fun delete(day: DayEntity)
}

// Info: contain the methods to interact with the database.