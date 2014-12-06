package ii_conventions

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate): Int {
        return if (year != other.year) {
            year.compareTo(other.year)
        } else if (month != other.month) {
            month.compareTo(other.month)
        } else if (dayOfMonth != other.dayOfMonth) {
            dayOfMonth.compareTo(other.dayOfMonth)
        } else {
            0
        }
    }

    fun rangeTo(other: MyDate) = DateRange(this, other)

    fun plus(interval: TimeInterval) =
        when (interval) {
            TimeInterval.DAY -> nextDay()
            TimeInterval.WEEK -> this + TimeInterval.DAY * 7
            TimeInterval.YEAR -> MyDate(year + 1, month, dayOfMonth)
            else -> throw IllegalArgumentException("Invalid interval kind")
        }

    fun plus(interval: MultiInterval): MyDate {
        var result = this
        for (i in 1..interval.count) {
            result += interval.kind
        }
        return result
    }
}

enum class TimeInterval {
    DAY
    WEEK
    YEAR

    fun times(y: Int) = MultiInterval(y, this)
}

data class MultiInterval(val count: Int, val kind: TimeInterval)

class DateRange(override val start: MyDate, override val end: MyDate) : Iterable<MyDate>, Range<MyDate> {
    override fun iterator(): Iterator<MyDate> {
        return object : Iterator<MyDate> {
            var state = start

            override fun next(): MyDate {
                val result = state
                state = state.nextDay()
                return result
            }

            override fun hasNext(): Boolean {
                return end >= state
            }
        }
    }

    override fun contains(item: MyDate): Boolean = item >= start && item <= end
}
