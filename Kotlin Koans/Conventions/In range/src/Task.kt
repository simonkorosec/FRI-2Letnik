class DateRange(val start: MyDate, val endInclusive: MyDate) {
    operator fun contains(d: MyDate) : Boolean {
        return d in start..endInclusive
    }
}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}
