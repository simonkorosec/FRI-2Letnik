class DateRange(val start: MyDate, val end: MyDate) : Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> {
        return DateIterator(this)
    }

    class DateIterator(private val dateRange: DateRange) : Iterator<MyDate> {
        private var cur: MyDate = dateRange.start

        override fun hasNext(): Boolean {
            return cur <= dateRange.end
        }

        override fun next(): MyDate {
            val ret = cur
            cur = cur.nextDay()
            return ret
        }

    }
}

fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
    for (date in firstDate..secondDate) {
        handler(date)
    }
}
