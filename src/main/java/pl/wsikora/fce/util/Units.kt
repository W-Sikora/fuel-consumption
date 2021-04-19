package pl.wsikora.fce.util

const val NANOSECONDS_TO_SECONDS = 1f / 1_000_000_000
const val MILLISECONDS_TO_SECONDS = 1f / 1_000
const val SECONDS_TO_HOURS = 1f / 3_600
const val SECONDS_TO_MINUTES = 1f / 60
const val METERS_TO_KILOMETERS = 1f / 1_000


fun Long.nanosecondsToSeconds(): Float {
    return this * NANOSECONDS_TO_SECONDS
}

fun Long.millisecondsToSeconds(): Float {
    return this * MILLISECONDS_TO_SECONDS
}

fun Float.secondsToIntHours(): Int {
    return (this * SECONDS_TO_HOURS).toInt()
}

fun Float.secondsToHours(): Float {
    return this * SECONDS_TO_HOURS
}

fun Float.secondsToMinutes(): Float {
    return this * SECONDS_TO_MINUTES
}

fun Float.metersToKilometers(): Float {
    return this * METERS_TO_KILOMETERS
}

fun Float.metersPerSecondToKilometersPerHour(): Float {
    return this * METERS_TO_KILOMETERS / SECONDS_TO_HOURS
}
