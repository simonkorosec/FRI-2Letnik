import java.util.*

fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    arrayList.sortWith(Comparator { x, y -> y-x })
    return arrayList
}
