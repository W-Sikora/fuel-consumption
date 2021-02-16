package pl.wsikora.fce.util

import java.util.*

class Coordinates(val latitude: Double, val longitude: Double) {
    val dateTime: Date = Calendar.getInstance().time

    override fun toString(): String {
        return "Coordinates(latitude=$latitude, longitude=$longitude, dateTime=$dateTime)"
    }

}