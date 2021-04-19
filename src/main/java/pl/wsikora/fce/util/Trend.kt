package pl.wsikora.fce.util

class Trend(private val calculations: Calculations) {
    private var previousFc = 0f
    private var currentFc = 0f

    private var previousAvgFc = 0f
    private var currentAvgFc = 0f

    private var previousSpeed = 0f
    private var currentSpeed = 0f

    private var previousAvgSpeed = 0f
    private var currentAvgSpeed = 0f

    fun getFcTrend(value: Float): Boolean {
        previousFc = currentFc
        currentFc = value
        return compare(currentFc, previousFc)
    }

    fun getAvgFcTrend(value: Float): Boolean {
        previousAvgFc = currentAvgFc
        currentAvgFc = value
        return compare(currentAvgFc, previousAvgFc)
    }

    fun getSpeedTrend(): Boolean {
        previousSpeed = currentSpeed
        currentSpeed = calculations.latestSpeed().toFloat()
        return compare(currentSpeed, previousSpeed)
    }

    fun getAvgSpeedTrend(): Boolean {
        previousAvgSpeed = currentAvgSpeed
        currentAvgSpeed = calculations.avgSpeed().toFloat()
        return compare(currentAvgSpeed, previousAvgSpeed)
    }

    private fun compare(currentValue: Float, previousValue: Float): Boolean {
        return currentValue >= previousValue
    }

}