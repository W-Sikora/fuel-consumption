package pl.wsikora.fce.util

class Value {
    var current = 0.0
        private set
    var previous = 0.0
        private set

    fun add(value: Double) {
        previous = current
        current = value
    }

    fun getRelativeChange(): Double = (current - previous) / previous * 100

    override fun toString(): String {
        return "Value(current=$current, previous=$previous)"
    }

}