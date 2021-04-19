package pl.wsikora.fce.util

import android.location.Location
import android.os.SystemClock
import com.google.android.gms.location.LocationResult

/**
 * Check that the location meets the requirements
 * @param locationResult LocationResult object
 * @param timeThreshold threshold in seconds
 * @param distanceThreshold threshold in meters
 */
fun isLocationValid(locationResult: LocationResult?,
                    timeThreshold: Int = 3,
                    distanceThreshold: Float = 21f): Boolean {
    return locationResult != null
            && locationResult.locations.isNotEmpty()
            && isLocationActual(locationResult.lastLocation, timeThreshold)
            && isLocationAccurate(locationResult.lastLocation, distanceThreshold)
}

/**
 * Check that the location meets the requirement of maximum time interval
 **/
private fun isLocationActual(location: Location, timeThreshold: Int): Boolean {
    return (SystemClock.elapsedRealtimeNanos() - location.elapsedRealtimeNanos)
            .nanosecondsToSeconds() < timeThreshold
}

/**
 * Check that the location meets the requirement of minimum accuracy
 **/
private fun isLocationAccurate(location: Location, distanceThreshold: Float): Boolean {
    return location.accuracy > 0.3f && location.accuracy < distanceThreshold
}
