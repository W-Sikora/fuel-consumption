package pl.wsikora.fce.util

import android.location.Location
import android.util.Log
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

class Calculations {
    private val locations = arrayListOf<Location>()
    private val startTime = Calendar.getInstance().time
    private var deltaTime = 0f
    private var latestDistance = 0f
    private var totalDistance = 0f
    private var latestSpeed = 0f
    private var error = 0f

    fun add(location: Location) {
        when (locations.size) {
            0 -> locations.add(location)
            1 -> {
                locations.add(location)
                error = location.accuracy.pow(2)
                calcValues()
            }
            else -> {
                locations[0] = locations[1]
                locations[1] = location
                runKalmanFilter(latestSpeed)
                calcValues()
            }
        }
    }

    fun timeElapsed(): String {
        val time = calcTimeElapsed()
        Log.d(">>>>", time.toString())
        val hours = time.secondsToIntHours()
        Log.d(">>>>", hours.toString())
        val minutes = (time - hours).secondsToMinutes().toInt()
        Log.d(">>>>", minutes.toString())
        return if (minutes < 10) {
            "${hours}:0$minutes"
        } else {
            "${hours}:$minutes"
        }
    }

    fun totalDistance(): String {
        return totalDistance.metersToKilometers()
                .toFormattedString(2)
    }

    fun latestSpeed(): String {
        return latestSpeed.metersPerSecondToKilometersPerHour()
                .toFormattedString(1)
    }

    fun avgSpeed(): String {
        return calcAvgSpeed().metersPerSecondToKilometersPerHour()
                .toFormattedString(1)
    }

    fun error(): String {
        return error.pow(0.5f).toFormattedString(1)
    }

    private fun calcValues() {
        calcDeltaTime()
        calcDistance()
        calcTotalDistance()
        calcSpeed()
    }

    private fun calcDeltaTime() {
        deltaTime = (locations[1].elapsedRealtimeNanos - locations[0].elapsedRealtimeNanos)
                .nanosecondsToSeconds()
    }

    private fun calcDistance() {
        latestDistance = locations[1].distanceTo(locations[0])
    }

    private fun calcTotalDistance() {
        totalDistance += latestDistance
    }

    private fun calcSpeed() {
        latestSpeed = latestDistance / (deltaTime)
    }

    private fun runKalmanFilter(speed: Float) {
        if (deltaTime > 0) {
            error += deltaTime * speed.pow(2)
        }
        val gain = error / (error + locations[1].accuracy.pow(2))
        locations[1].latitude += gain * (locations[1].latitude - locations[0].latitude)
        locations[1].longitude += gain * (locations[1].longitude - locations[0].longitude)
        error *= (1 - gain)
    }

    private fun calcTimeElapsed(): Float {
        return (Calendar.getInstance().time.time - startTime.time).millisecondsToSeconds()
    }

    private fun calcAvgSpeed(): Float {
        return totalDistance / calcTimeElapsed()
    }

    private fun Float.toFormattedString(decimalPlaces: Int): String {
        return String.format(Locale.US, "%.${decimalPlaces}f", this)
    }

}