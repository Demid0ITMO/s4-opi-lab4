package opi.lab4.mbeans

import itmo.web.lab3.HistoryManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MiddleTimeValue(private val historyManager: HistoryManager,
                      private val formatter: DateTimeFormatter): MiddleTimeValueMBean {

    override fun getMiddleValue(): Float {
        val times = historyManager.getHistory().map { LocalDateTime.parse(it.currentTime, formatter) }
        var prevTime = times[0]
        val countOfIntervals = (times.size - 1).toFloat()
        var counter = 0L
        for (time in times) {
            counter += ChronoUnit.SECONDS.between(prevTime, time)
            prevTime = time
        }
        return counter / countOfIntervals
    }

}