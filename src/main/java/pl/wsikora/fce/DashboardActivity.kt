package pl.wsikora.fce


import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.*
import pl.wsikora.fce.databinding.ActivityDashboardBinding
import pl.wsikora.fce.util.*
import java.util.*


class DashboardActivity : AppCompatActivity() {
    private val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var calculations: Calculations
    private lateinit var trend: Trend
    private var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        calculations = Calculations()
        trend = Trend(calculations)
        initialize()
        initializeLocation()

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            askForPermission()
        }
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun checkPermissions(): Boolean {
        for (permission in permissions) {
            if (checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    private fun askForPermission() {
        requestPermissions(this, permissions, 1)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun initializeLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create()
                .apply {
                    interval = 1_500L
                    smallestDisplacement = 1.5f
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult?) {
                if (isLocationValid(location)) {
                    updateValues(location!!)
                    counter++
                } else {
                    val accuracy = binding.accuracy
                    accuracy.first.value = location?.lastLocation?.accuracy.toString()
                    accuracy.second.value = calculations.error()
                }
            }
        }
    }

    private fun updateValues(result: LocationResult) {
        calculations.add(result.lastLocation)

        val up = R.drawable.trending_up
        val down = R.drawable.trending_down

        val fuelConsumption = binding.fuelConsumption
        fuelConsumption.first.value = "-"
//            fuelConsumption.first.trend.setImageResource(if (trend.getFcTrend()) up else down)
        fuelConsumption.second.value = "-"
//            fuelConsumption.second.trend.setImageResource(if (trend.getAvgFcTrend()) up else down)

        val speed = binding.speed
        speed.first.value = calculations.latestSpeed()
        speed.first.trend.setImageResource(if (trend.getSpeedTrend()) up else down)
        speed.second.value = calculations.avgSpeed()
        speed.second.trend.setImageResource(if (trend.getAvgSpeedTrend()) up else down)

        val general = binding.general
        general.first.value = calculations.totalDistance()
        general.second.value = calculations.timeElapsed()

        val location = binding.location
        location.cardHeader.text = "lokalizacja $counter"
        location.first.value = result.lastLocation.latitude.toString()
        location.second.value = result.lastLocation.longitude.toString()


        val accuracy = binding.accuracy
        accuracy.first.value = result.lastLocation.accuracy.toString()
        accuracy.second.value = calculations.error()


    }

    private fun initialize() {
        binding.dashboardHeader.section = resources
                .getString(R.string.dashboard_section)

        val fuelConsumptionCard = binding.fuelConsumption

        fuelConsumptionCard.cardHeader.text = resources
                .getString(R.string.dashboard_fuel_consumption_card_header)

        fuelConsumptionCard.icon
                .setBackgroundResource(R.drawable.gas_station)

        fuelConsumptionCard.first.header = resources
                .getString(R.string.dashboard_fuel_consumption_first_header)

        fuelConsumptionCard.first.unit = resources
                .getString(R.string.dashboard_fuel_consumption_unit)

        fuelConsumptionCard.second.header = resources
                .getString(R.string.dashboard_fuel_consumption_second_header)

        fuelConsumptionCard.second.unit = resources
                .getString(R.string.dashboard_fuel_consumption_unit)

        val speedCard = binding.speed

        speedCard.cardHeader.text = resources
                .getString(R.string.dashboard_speed_card_header)

        speedCard.icon
                .setBackgroundResource(R.drawable.speed)

        speedCard.first.header = resources
                .getString(R.string.dashboard_speed_first_header)

        speedCard.first.unit = resources
                .getString(R.string.dashboard_speed_unit)

        speedCard.second.header = resources
                .getString(R.string.dashboard_speed_second_header)

        speedCard.second.unit = resources
                .getString(R.string.dashboard_speed_unit)

        val generalCard = binding.general

        generalCard.cardHeader.text = resources
                .getString(R.string.dashboard_general_card_header)

        generalCard.icon
                .setBackgroundResource(R.drawable.car)

        generalCard.first.header = resources
                .getString(R.string.dashboard_general_first_header)

        generalCard.first.unit = resources
                .getString(R.string.dashboard_general_distance_unit)

        generalCard.second.header = resources
                .getString(R.string.dashboard_general_second_header)

        generalCard.second.unit = resources
                .getString(R.string.dashboard_general_time_unit)

        val locationCard = binding.location

        locationCard.cardHeader.text = resources
                .getString(R.string.dashboard_location_card_header)

        locationCard.icon
                .setBackgroundResource(R.drawable.gps)

        locationCard.first.header = resources
                .getString(R.string.dashboard_location_first_header)

        locationCard.second.header = resources
                .getString(R.string.dashboard_location_second_header)

        val button = binding.dashboardFooter.button

        button.text = resources
                .getString(R.string.dashboard_button)

        button.setOnClickListener {
            startActivity(Intent(baseContext, AskActivity::class.java))
        }
    }

}