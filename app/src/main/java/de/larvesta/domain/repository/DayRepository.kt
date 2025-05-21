package de.larvesta.domain.repository

import de.larvesta.data.dao.DayDao
import de.larvesta.domain.mapper.toDomain
import de.larvesta.domain.mapper.toEntity
import de.larvesta.domain.model.Day
import java.time.LocalDate
import java.util.Date

class DayRepository(private val dayDao: DayDao) {
    suspend fun insertDay(day: Day) {
        dayDao.insert(day.toEntity())
    }

    suspend fun getDays(): List<Day> {
        return dayDao.getAllDays().map { it.toDomain() }
    }

    suspend fun getDayById(id: Int): Day? {
        val dayEntity = dayDao.getDayById(id)
        return dayEntity?.toDomain()
    }

    suspend fun getDayByDate(date: LocalDate): Day? {
        val dayEntity = dayDao.getDayByDate(date)
        return dayEntity?.toDomain()
    }

    suspend fun deleteDay(day: Day) {
        dayDao.delete(day.toEntity())
    }

    suspend fun updateDay(day: Day) {
        dayDao.update(day.toEntity())
    }
}

// Info:    fetch data from the database.
//          Use mappers to map toDomain DayDao or to Entity Day