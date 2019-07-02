package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.Chat
import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_instance() {
        val user = User("2", "John", "Cena")
        println(user)
    }

    @Test
    fun test_data_mapping() {
        val user = User.makeUser("Андрей Чувачковый")
        val newUser = user.copy(lastVisit = Date().add(-7, TimeUnits.SECOND))
        println(newUser)
        val userView = newUser.toUserView()
        userView.printMe()
    }

    //Задание 2.1
    @Test
    fun test_copy() {
        val user = User.makeUser("John Wick")
        val user2 = user.copy(lastVisit = Date())
        val user3 = user.copy(lastVisit = Date().add(-2, TimeUnits.SECOND))
        val user4 = user.copy(lastName = "Cene", lastVisit = Date().add(2, TimeUnits.HOUR))
        println(
            """
            ${user.lastVisit?.format()}
            ${user2.lastVisit?.format()}
            ${user3.lastVisit?.format()}
            ${user4.lastVisit?.format()}
        """.trimIndent()
        )
    }


    //Задание 2.2
    @Test
    fun test_abstract_factory() {
        val user = User.makeUser("Андрей Чувачковый")
        val txtMessage = BaseMessage.makeMessage(user, Chat(id = "0"), payload = "any text message", type = "text")
        val imgMessage = BaseMessage.makeMessage(user, Chat(id = "1"), payload = "any image url", type = "image")

        println(txtMessage.formatMessage())
        println(imgMessage.formatMessage())
    }

    //Задание 2.3
    @Test
    fun test_parse_full_name() {
        assertEquals(Pair(null, null), Utils.parseFullName(null))
        assertEquals(Pair(null, null), Utils.parseFullName(""))
        assertEquals(Pair(null, null), Utils.parseFullName(" "))
        assertEquals(Pair("John", null), Utils.parseFullName("John"))
    }

    //Задание 2.4
    @Test
    fun test_date_format() {
        println(Date().format())
        println(Date().format("HH:mm"))
    }

    //Задание 2.5
    @Test
    fun test_date_add() {
        println(Date().add(2, TimeUnits.SECOND))
        println(Date().add(-4, TimeUnits.DAY))
    }

    //Задание 2.6
    @Test
    fun test_initials() {
        assertEquals("АЧ", Utils.toInitials("Андрей", "Чувачковый"))
        assertEquals("АЧ", Utils.toInitials("андрей", "чувачковый"))
        assertEquals("Ч", Utils.toInitials(null, "Чувачковый"))
        assertEquals("Ч", Utils.toInitials("", "Чувачковый"))
        assertEquals("Ч", Utils.toInitials(null, "чувачковый"))
        assertEquals("Ч", Utils.toInitials("", "чувачковый"))
        assertEquals("А", Utils.toInitials("Андрей", null))
        assertEquals("А", Utils.toInitials("Андрей", ""))
        assertEquals("А", Utils.toInitials("андрей", null))
        assertEquals("А", Utils.toInitials("андрей", ""))
        assertEquals(null, Utils.toInitials(null, null))
        assertEquals(null, Utils.toInitials("", null))
        assertEquals(null, Utils.toInitials(null, ""))
        assertEquals(null, Utils.toInitials("", ""))
        assertEquals(null, Utils.toInitials(" ", ""))
        assertEquals(null, Utils.toInitials("", " "))
        assertEquals(null, Utils.toInitials(" ", " "))
    }

    //Задание 2.7
    @Test
    fun test_transliteration() {
        assertEquals("Zhenya Stereotipov", Utils.transliteration("Женя Стереотипов", " "))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
        assertEquals("Zhirinovskii", Utils.transliteration("Жириновский", " "))
        assertEquals("zhirinovskiI", Utils.transliteration("жириновскиЙ", " "))
        assertEquals("Shcherbakov", Utils.transliteration("Щербаков", " "))
    }

    //Задание 2.8 ty bgv26
    @Test
    fun test_humanizeDiff() {
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
    }


    //Задание 2.9
    @Test
    fun test_builder() {
        val user =
            User.Builder()
                .firstName("Андрей")
                .lastName("Чувачковый")
                .rating(5)
                .isOnline(true)
                .lastVisit(Date().add(-2, TimeUnits.DAY))
                .build()

        print(user)
    }

}
