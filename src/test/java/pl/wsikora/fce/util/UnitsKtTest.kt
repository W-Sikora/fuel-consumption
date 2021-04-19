package pl.wsikora.fce.util

import junit.framework.TestCase

class UnitsKtTest : TestCase() {

    fun testNanosecondsToSeconds() {
        assertEquals(0f, 0L.nanosecondsToSeconds())
        assertEquals(1f, 1_000_000_000L.nanosecondsToSeconds())
    }

    fun testSecondsToHours() {
        assertEquals(0f, 0f.secondsToHours())
        assertEquals(0.1f, 360f.secondsToHours())
        assertEquals(1f, 3600f.secondsToHours())
    }

    fun testMetersToKilometers() {
        assertEquals(0f, 0f.metersToKilometers())
        assertEquals(0.277f, 277f.metersToKilometers())
        assertEquals(1f, 1000f.metersToKilometers())
    }

    fun testMetersPerSecondToKilometersPerHour() {
        assertEquals(0f, 0f.metersPerSecondToKilometersPerHour())
        assertEquals(36f, 10f.metersPerSecondToKilometersPerHour())
    }

}