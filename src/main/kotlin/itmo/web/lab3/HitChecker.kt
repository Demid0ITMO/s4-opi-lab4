package itmo.web.lab3

import jakarta.enterprise.context.SessionScoped
import jakarta.faces.context.FacesContext
import jakarta.inject.Named
import opi.lab4.mbeans.MiddleTimeValue
import opi.lab4.mbeans.PointsCounter
import java.io.Serializable
import java.lang.management.ManagementFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.management.ObjectName


@Named
@SessionScoped
class HitChecker: Serializable {
    val historyManager = HistoryManager()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val pointsCounter = PointsCounter(historyManager)
    private val middleTimeValue = MiddleTimeValue(historyManager, formatter)

    init {
        try {
            val mbs = ManagementFactory.getPlatformMBeanServer()

            val pointsCounterName = ObjectName("opi.lab4.mbeans:type=PointsCounter")
            mbs.registerMBean(pointsCounter, pointsCounterName)

            val middleTimeValueName = ObjectName("opi.lab4.mbeans:type=MiddleTimeValue")
            mbs.registerMBean(middleTimeValue, middleTimeValueName)

        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun check(v: Boolean, x: Double, y: Double, r: Double) {
        if (!v) return
        val point = Point(x, y, r, LocalDateTime.now().format(formatter).toString(), isInArea(x, y, r))
        if (point.result) {
            pointsCounter.incrementTotalPoints()
            pointsCounter.incrementHitPoints()
            pointsCounter.resetMissedPointsSequenceLength()
        }
        else {
            pointsCounter.incrementTotalPoints()
            pointsCounter.incrementMissedPoints()
        }
        historyManager.addPoint(point)
    }
    fun checkFromSVG() {
        val params = FacesContext.getCurrentInstance().externalContext.requestParameterMap
        try {
            val x = params["x"]!!.toDouble()
            val y = params["y"]!!.toDouble()
            val r = params["r"]!!.toDouble()
            check(true, x, y, r)
        } catch (e: NullPointerException) {
            println(e.message)
            return
        } catch (e: NumberFormatException) {
            println(e.message)
            return
        }
    }
    fun getHistory() = historyManager.getHistory()
    fun clearHistory() {
        pointsCounter.resetAndInitCounters()
        pointsCounter.resetMissedPointsSequenceLength()
        historyManager.clearHistory()
    }

    private fun isInArea(x: Double, y: Double, r: Double) = isInTriangle(x, y, r) || isInCircle(x, y, r) || isInRectangle(x, y, r)
    private fun isInRectangle(x: Double, y: Double, r: Double) = (x in -r..0.0) && (y in 0.0..r)
    private fun isInCircle(x: Double, y: Double, r: Double) = (x*x + y*y <= r*r/4) && (y <= 0) && (x >= 0)
    private fun isInTriangle(x: Double, y: Double, r: Double) = (y >= - x / 2 - r / 2) && (y <= 0) && (x <= 0)
}