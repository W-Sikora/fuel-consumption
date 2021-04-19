package pl.wsikora.fce.util

import android.location.Location
import com.google.android.gms.location.LocationResult
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class LocationValidatorKtTest : TestCase() {

    private val emptyLocationResult = LocationResult.create(emptyList())
    private val loc = Location("testLocation")

    @Test
    fun test() {
        assertEquals(false, isLocationValid(emptyLocationResult))
    }

}