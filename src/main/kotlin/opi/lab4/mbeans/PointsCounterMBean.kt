package opi.lab4.mbeans

interface PointsCounterMBean {
    fun getTotalPoints(): Int
    fun getHitPoints(): Int
    fun incrementTotalPoints()
    fun incrementHitPoints()
    fun incrementMissedPoints()
    fun resetMissedPointsSequenceLength()
    fun resetAndInitCounters()
}