package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    return this
}

// Довольно долго гуглил как правильно организовать данную функцию
// и наткнулся на репозиторий от bgv26 - спасибо ему за данную функцию
// Немного модернизировал
fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = date.time - this.time
    val absDiff = Math.abs(diff)
    val isPast = diff > 0
    //для мультиязычности
    val backwards = "назад" to "через" //"ago" to "in a"
    val now = "только что" // "just now"
    val fewSeconds = "несколько секунд"//few seconds
    val minute = "минуту" // minute
    val hour = "час" // hour
    val day = "день" // day

    return when {
        absDiff / SECOND <= 1 -> now
        absDiff / SECOND <= 45 -> if (isPast) "$fewSeconds ${backwards.first}" else "${backwards.second} $fewSeconds"
        absDiff / SECOND <= 75 -> if (isPast) "$minute ${backwards.first}" else "${backwards.second} $minute"
        absDiff / MINUTE <= 45 -> if (isPast) "${TimeUnits.MINUTE.plural(absDiff / MINUTE)} ${backwards.first}" else "${backwards.second} ${TimeUnits.MINUTE.plural(absDiff / MINUTE)}"
        absDiff / MINUTE <= 75 -> if (isPast) "$hour ${backwards.first}" else "${backwards.second} $hour"
        absDiff / HOUR <= 22 -> if (isPast) "${TimeUnits.HOUR.plural(absDiff / HOUR)} ${backwards.first}" else "${backwards.second} ${TimeUnits.HOUR.plural(absDiff / HOUR)}"
        absDiff / HOUR <= 26 -> if (isPast) "$day ${backwards.first}" else "${backwards.second} $day"
        absDiff / DAY <= 360 -> if (isPast) "${TimeUnits.DAY.plural(absDiff / DAY)} ${backwards.first}" else "${backwards.second} ${TimeUnits.DAY.plural(absDiff / DAY)}"
        else -> if (diff > 0) "более года назад" else "более чем через год"
    }
}



enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY;

    fun plural(value: Long): String {
        val remainder = value % 10
        val preLastDigit = value % 100 / 10
        val plurals = mapOf(
            SECOND to mapOf(
                PluralUnits.FEW to "секунды",
                PluralUnits.ONE to "секунду",
                PluralUnits.MANY to "секунд"
            ),
            MINUTE to mapOf(
                PluralUnits.FEW to "минуты",
                PluralUnits.ONE to "минуту",
                PluralUnits.MANY to "минут"
            ),
            HOUR to mapOf(
                PluralUnits.FEW to "часа",
                PluralUnits.ONE to "час",
                PluralUnits.MANY to "часов"
            ),
            DAY to mapOf(
                PluralUnits.FEW to "дня",
                PluralUnits.ONE to "день",
                PluralUnits.MANY to "дней"
            )
        )
        return when {
            (preLastDigit == 1L) -> "$value ${plurals[this]?.get(PluralUnits.MANY)}"
            (remainder in 2..4)   -> "$value ${plurals[this]?.get(PluralUnits.FEW)}"
            (remainder == 1L)  -> "$value ${plurals[this]?.get(PluralUnits.ONE)}"
            else -> "$value ${plurals[this]?.get(PluralUnits.MANY)}"
        }
    }
    private enum class PluralUnits {
        FEW, ONE, MANY
    }
}