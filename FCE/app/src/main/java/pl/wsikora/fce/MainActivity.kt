package pl.wsikora.fce

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.wsikora.fce.util.Calculation
import pl.wsikora.fce.util.Coordinates


class MainActivity : AppCompatActivity() {
    private val calculation = Calculation()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onLocationChanged(location: Location) {
        val coordinates = Coordinates(
            location.latitude,
            location.longitude
        )
        calculation.add(coordinates)
    }

}