package opi.lab4.mbeans

import itmo.web.lab3.HistoryManager
import java.util.concurrent.atomic.AtomicInteger
import javax.management.Notification
import javax.management.NotificationBroadcasterSupport

class PointsCounter(private val historyManager: HistoryManager): NotificationBroadcasterSupport(), PointsCounterMBean {
    private val totalPoints = AtomicInteger(0)
    private val hitPoints = AtomicInteger(0)
    private var missedPointsSequenceLength = 0
    
    init { initCounters() }

    private fun initCounters() {
        historyManager.getHistory().forEach { point ->
            totalPoints.incrementAndGet()
            if (point.result) hitPoints.incrementAndGet()
        }
    }

    override fun resetAndInitCounters() {
        totalPoints.set(0)
        hitPoints.set(0)
        resetMissedPointsSequenceLength()
        initCounters()
    }

    override fun getTotalPoints() = totalPoints.get()

    override fun getHitPoints() = hitPoints.get()

    override fun incrementTotalPoints() { totalPoints.incrementAndGet() }

    override fun incrementHitPoints() { hitPoints.incrementAndGet() }

    override fun incrementMissedPoints() {
        missedPointsSequenceLength++
        if (missedPointsSequenceLength == 3) {
            sendNotification(
                Notification(
                    "PointsCounterNotification",
                    this, 0,
                    System.currentTimeMillis(),
                    "3 points missed"
                )
            )
            resetMissedPointsSequenceLength()
        }
    }

    override fun resetMissedPointsSequenceLength() {
        missedPointsSequenceLength = 0
    }

}