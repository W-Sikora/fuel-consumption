package pl.wsikora.fce.util

import java.util.*
import kotlin.collections.HashMap
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

class Calculation {
    private val distance = Distance()
    private val speed = Speed()
    private val data = HashMap<Date, Double>()
    private val coordinatesList = arrayListOf<Coordinates>()

    abstract class Measure {
        var current = 0.0
            private set
        var sum = 0.0
            private set
        var count = 0
            private set

        fun add(value: Double) {
            current = value
            sum += value
            count++
        }
    }

    private class Distance : Measure() {
        override fun toString(): String {
            return "Distance(current=$current, sum=$sum, count=$count)"
        }
    }

    private class Speed : Measure() {
        fun getAvg(): Double = sum / count

        override fun toString(): String {
            return "Speed(current=$current, sum=$sum, count=$count)"
        }
    }

    fun add(coordinates: Coordinates) {
        if (coordinatesList.size > 1) {
            coordinatesList[0] = coordinatesList[1]
            coordinatesList[1] = coordinates
            val currentDistance = calculateDistance()
            val currentInterval = calculateInterval()
            val currentSpeed = calculateSpeed(currentDistance, currentInterval)
            distance.add(currentDistance)
            speed.add(currentSpeed)
            if (data.size < 5) {
                data[coordinatesList[1].dateTime] = currentSpeed
            } else {
                data.clear()
            }
        } else {
            coordinatesList.add(coordinates)
        }
    }

    fun getSpeed(): Double = speed.current

    fun getAvgSpeed(): Double = speed.getAvg()

    fun getTotalDistance(): Double = distance.sum

    fun getData(): HashMap<Date, Double> = data

    override fun toString(): String {
        return "Calculation(distance=$distance, speed=$speed, data=$data, coordinatesList=$coordinatesList)"
    }

    private fun calculateDistance(): Double {
        fun haversine(angle: Double): Double = (1 - cos(angle)) / 2

        fun Double.toRadians() = Math.toRadians(this)

        val currentLatitude = coordinatesList[1].latitude.toRadians()
        val previousLatitude = coordinatesList[0].latitude.toRadians()

        val deltaLatitude = currentLatitude - previousLatitude
        val deltaLongitude = coordinatesList[1].longitude.toRadians()
            .minus(coordinatesList[0].longitude.toRadians())

        return 2 * 6_371.0088 * asin(
            sqrt(
                haversine(deltaLatitude)
                    .plus(
                        cos(previousLatitude)
                            .times(cos(currentLatitude))
                            .times(haversine(deltaLongitude))
                    )
            )
        )
    }

    private fun calculateInterval(): Double {
        return coordinatesList[1].dateTime.time
            .minus(coordinatesList[0].dateTime.time)
            .div(3_600_000.0)
    }

    private fun calculateSpeed(distance: Double, interval: Double): Double = distance / interval

}
