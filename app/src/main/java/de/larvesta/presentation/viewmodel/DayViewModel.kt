package de.larvesta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.larvesta.domain.repository.DayRepository
import de.larvesta.domain.model.Day
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import android.util.Log

class DayViewModel(private val repository: DayRepository) : ViewModel() {
    private val _days = MutableStateFlow<List<Day>>(emptyList())
    val days: StateFlow<List<Day>> get() = _days

    private val _day = MutableStateFlow<Day?>(null)
    val day: StateFlow<Day?> get() = _day

    // Automatically fetch all days when the ViewModel is initialized
    init {
        fetchDays()
    }

    fun fetchDays() {
        viewModelScope.launch {
            val fetchedDays = repository.getDays()
            _days.value = fetchedDays
        }
    }

    fun addDay (day: Day) {
        viewModelScope.launch {
            repository.insertDay(day)
            fetchDays()
        }
    }

    fun updateDay(day: Day) {
        viewModelScope.launch {
            repository.updateDay(day)
        }
    }

    fun deleteDay(day: Day) {
        viewModelScope.launch {
            repository.deleteDay(day)
            fetchDays()
        }
    }

    fun fetchDayById(id: Int) {
        viewModelScope.launch {
            val fetchedDay = repository.getDayById(id)
            _day.value = fetchedDay
        }
    }

    fun fetchDayByDate(date: LocalDate) {
        viewModelScope.launch {
            val fetchedDay = repository.getDayByDate(date)
            _day.value = fetchedDay
        }
    }

    suspend fun fetchDayByDateSuspend(date: LocalDate): Day? {
        val fetchedDay = repository.getDayByDate(date)
        _day.value = fetchedDay
        return fetchedDay
    }
}