package ru.skillbranch.devintensive

import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.*
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

class TestsByCommunity {
    @Test
    fun test_fullNameParsing() {
        assertEquals(null to null, Utils.parseFullName(null))
        assertEquals(null to null, Utils.parseFullName(""))
        assertEquals(null to null, Utils.parseFullName(" "))
        assertEquals("John" to null, Utils.parseFullName("John "))
        assertEquals("John" to "Wick", Utils.parseFullName("John  Wick"))
    }

    @Test
    fun test_transliteration() {
        assertEquals("Ivan Stereotipov", Utils.transliteration("Иван Стереотипов"))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
        assertEquals("Privet mir", Utils.transliteration("Привет мир"))
        assertEquals("    Privet    mir   ", Utils.transliteration("    Привет    мир   "))
        assertEquals("pRIvet mir", Utils.transliteration("pRIвет мир"))
        assertEquals("PRIvet Mir", Utils.transliteration("PRIвет Mир"))
        assertEquals("PRIvet1345 Mir", Utils.transliteration("PRIвет1345 Mир"))
        assertEquals("[]{}PRIvet Mir/", Utils.transliteration("[]{}PRIвет Mир/"))
        assertEquals("[]{}PRIvet____Mir/", Utils.transliteration("[]{}PRIвет    Mир/", "_"))
        assertEquals("[_444__444__444__444_]{}PRIvet_444__444_Mir/", Utils.transliteration("[    ]{}PRIвет  Mир/", "_444_"))
    }

    @Test
    fun test_toInitials() {
        assertEquals("JD", Utils.toInitials("john", "doe"))
        assertEquals("J", Utils.toInitials("John", null))
        assertEquals(null, Utils.toInitials(null, null))
        assertEquals(null, Utils.toInitials(" ", ""))
    }

    @Test
    fun test_builder() {
        val user = User.Builder()
            .id("123")
            .firstName("Vasya")
            .lastName("Пупкин")
            .avatar(null)
            .rating(1)
            .respect(1)
            .lastVisit(null)
            .isOnline(false)
            .build()
        println(user)
        val user2 = User.Builder()
            .firstName("Vasya")
            .lastName("Пупкин")
            .avatar(null)
            .rating(1)
            .respect(1)
            .lastVisit(null)
            .isOnline(false)
            .build()
        println(user2)
        val user3 = User.Builder()
            .firstName("Vasya")
            .lastName("Пупкин")
            .avatar(null)
            .rating(1)
            .respect(1)
            .lastVisit(null)
            .isOnline(false)
            .build()
        println(user3)
        assertThat(user, instanceOf(User::class.java))
    }

    @Test
    fun test_humanizeDiff() {
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
        assertEquals("только что", Date().add(-1, TimeUnits.SECOND).humanizeDiff())
        assertEquals("несколько секунд назад", Date().add(-45, TimeUnits.SECOND).humanizeDiff())
        assertEquals("минуту назад", Date().add(-46, TimeUnits.SECOND).humanizeDiff())
        assertEquals("1 минуту назад", Date().add(-76, TimeUnits.SECOND).humanizeDiff())
        assertEquals("минуту назад", Date().add(-1, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("2 минуты назад", Date().add(-2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("3 минуты назад", Date().add(-3, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("45 минут назад", Date().add(-45, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("час назад", Date().add(-1, TimeUnits.HOUR).humanizeDiff())
        assertEquals("1 час назад", Date().add(-76, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("2 часа назад", Date().add(-120, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("3 часа назад", Date().add(-3, TimeUnits.HOUR).humanizeDiff())
        assertEquals("4 часа назад", Date().add(-4, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 часов назад", Date().add(-5, TimeUnits.HOUR).humanizeDiff())
        assertEquals("день назад", Date().add(-26, TimeUnits.HOUR).humanizeDiff())
        assertEquals("1 день назад", Date().add(-27, TimeUnits.HOUR).humanizeDiff())
        assertEquals("4 дня назад", Date().add(-4, TimeUnits.DAY).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("360 дней назад", Date().add(-360, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-361, TimeUnits.DAY).humanizeDiff())
    }

    @Test
    fun test_parse_fullname() {
        assertEquals("John" to "Wick", Utils.parseFullName("John Wick"))
        assertEquals(null to null, Utils.parseFullName(" "))
        assertEquals(null to null, Utils.parseFullName(""))
        assertEquals("John" to "Wick", Utils.parseFullName("John    Wick"))
        assertEquals("John" to "Wick", Utils.parseFullName("   John    Wick"))
        assertEquals("John" to "Wick", Utils.parseFullName("   John    Wick   "))
        assertEquals(null to null, Utils.parseFullName(null))
        assertEquals(null to null, Utils.parseFullName(""))
        assertEquals(null to null, Utils.parseFullName(" "))
        assertEquals("John" to null, Utils.parseFullName("John"))
        assertEquals(null to null, Utils.parseFullName(" "))
    }

    @Test
    fun test_abstract_factory() {
        val user = User.makeUser("Евгений Иванов")
        val textMessage =
            BaseMessage.makeMessage(user, Chat("0"), Date(),  "any text message","text")
        val imageMessage =
            BaseMessage.makeMessage(
                user,
                Chat("0"),
                Date().add(-2, TimeUnits.HOUR),
                "https://anyurl.com",
                "image",
                true
            )

        //assertEquals("Евгений отправил сообщение \"any text message\" только что", textMessage.formatMessage())
      //  assertEquals("Евгений получил изображение \"https://anyurl.com\" 2 часа назад", imageMessage.formatMessage())
    }

    @Test
    fun test_date_format() {
        val date =
            Date.from(Calendar.getInstance(Locale("ru")).apply { set(2019, Calendar.JUNE, 27, 14, 0, 0) }.toInstant())
        assertEquals("14:00:00 27.06.19", date.format())
        assertEquals("14:00", date.format("HH:mm"))
    }

    @Test
    fun test_date_add() {
        val instant = Calendar.getInstance().apply { set(2019, Calendar.JUNE, 27, 14, 0, 0) }.toInstant()
        val date = Date.from(instant)
        val date2 = Date.from(instant)
        assertEquals("14:00:02 27.06.19", date.add(2, TimeUnits.SECOND).format())
        assertEquals("14:00:00 23.06.19", date2.add(-4, TimeUnits.DAY).format())
    }

    @Test
    fun test_initial() {
        assertEquals("JD", Utils.toInitials("john", "doe"))
        assertEquals("JD", Utils.toInitials("John", "Doe"))
        assertEquals("J", Utils.toInitials("John", null))
        assertEquals(null, Utils.toInitials(null, null))
        assertEquals(null, Utils.toInitials(" ", ""))
    }

    @Test
    fun transliteration() {
        assertEquals("Ivan Stereotipov", Utils.transliteration("Иван Стереотипов"))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))

        assertEquals("Zh Zh", Utils.transliteration("Ж Ж"))
        assertEquals("ZhZh", Utils.transliteration("ЖЖ"))
        assertEquals("AbrAKadabra", Utils.transliteration("AbrAKadabra"))
        assertEquals("StraNNIi NikVash'e", Utils.transliteration("СтраННЫй НикВаще"))
    }

    @Test
    fun test_humanizeDiff2() {
        assertEquals("через несколько секунд", Date().humanizeDiff(Date().add(-2, TimeUnits.SECOND)))
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())


        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())

        assertEquals("минуту назад", Date().add(-1, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("2 минуты назад", Date().add(-2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("3 минуты назад", Date().add(-3, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("5 минут назад", Date().add(-5, TimeUnits.MINUTE).humanizeDiff())

        assertEquals("час назад", Date().add(-1, TimeUnits.HOUR).humanizeDiff())
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("3 часа назад", Date().add(-3, TimeUnits.HOUR).humanizeDiff())
        assertEquals("4 часа назад", Date().add(-4, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 часов назад", Date().add(-5, TimeUnits.HOUR).humanizeDiff())

        assertEquals("день назад", Date().add(-1, TimeUnits.DAY).humanizeDiff())
        assertEquals("4 дня назад", Date().add(-4, TimeUnits.DAY).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("100 дней назад", Date().add(-100, TimeUnits.DAY).humanizeDiff())

        assertEquals("несколько секунд назад", Date().humanizeDiff(Date().add(34, TimeUnits.SECOND)))
        assertEquals("минуту назад", Date().humanizeDiff(Date().add(61, TimeUnits.SECOND)))
        assertEquals("5 минут назад", Date().humanizeDiff(Date().add(5, TimeUnits.MINUTE)))
        assertEquals("20 дней назад", Date().humanizeDiff(Date().add(20, TimeUnits.DAY)))
        assertEquals("90 дней назад", Date().humanizeDiff(Date().add(90, TimeUnits.DAY)))
        assertEquals("через несколько секунд", Date().humanizeDiff(Date().add(-13, TimeUnits.SECOND)))
        assertEquals("через минуту", Date().humanizeDiff(Date().add(-63, TimeUnits.SECOND)))
        assertEquals("через минуту", Date().humanizeDiff(Date().add(-1, TimeUnits.MINUTE)))
        assertEquals("через 29 дней", Date().humanizeDiff(Date().add(-29, TimeUnits.DAY)))
        assertEquals("только что", Date().humanizeDiff(Date().add(0, TimeUnits.HOUR)))
        assertEquals(
            "через несколько секунд",
            Date().humanizeDiff(Date().add(-2, TimeUnits.SECOND))
        ) //несколько секунд назад
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff()) //2 часа назад
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff()) //5 дней назад
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff()) //через 2 минуты
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff()) //через 7 дней
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff()) //более года назад
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff()) //более чем через год
        assertEquals("только что", Date().humanizeDiff())
        assertEquals(
            "через несколько секунд",
            Date().humanizeDiff(Date().add(-2, TimeUnits.SECOND))
        ) //через несколько секунд
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("минуту назад", Date().add(-1, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("2 минуты назад", Date().add(-2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("3 минуты назад", Date().add(-3, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("5 минут назад", Date().add(-5, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("час назад", Date().add(-1, TimeUnits.HOUR).humanizeDiff())
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("3 часа назад", Date().add(-3, TimeUnits.HOUR).humanizeDiff())
        assertEquals("4 часа назад", Date().add(-4, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 часов назад", Date().add(-5, TimeUnits.HOUR).humanizeDiff())
        assertEquals("день назад", Date().add(-1, TimeUnits.DAY).humanizeDiff())
        assertEquals("4 дня назад", Date().add(-4, TimeUnits.DAY).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())

        assertEquals("100 дней назад", Date().add(-100, TimeUnits.DAY).humanizeDiff())
    }


    @Test
    fun test_builder2() {
        val date = Date.from(Calendar.getInstance().apply { set(2019, Calendar.JUNE, 27, 14, 0, 0) }.toInstant())

        val user = User.Builder().id("1")
            .firstName("firstName")
            .lastName("lastName")
            .avatar("avatar")
            .rating(1)
            .respect(2)
            .lastVisit(date)
            .isOnline(true)
            .build()

        assertEquals("1", user.id)
        assertEquals("firstName", user.firstName)
        assertEquals("lastName", user.lastName)
        assertEquals("avatar", user.avatar)
        assertEquals(1, user.rating)
        assertEquals(2, user.respect)
        assertEquals(date, user.lastVisit)
        assertEquals(true, user.isOnline)

        val date2 = Date.from(Calendar.getInstance().apply { set(2018, Calendar.JUNE, 27, 14, 0, 0) }.toInstant())

        val user2 = User.Builder().id("2")
            .firstName("firstName2")
            .lastName("lastName2")
            .avatar("avatar2")
            .rating(3)
            .respect(4)
            .lastVisit(date2)
            .isOnline(false)
            .build()

        assertEquals("2", user2.id)
        assertEquals("firstName2", user2.firstName)
        assertEquals("lastName2", user2.lastName)
        assertEquals("avatar2", user2.avatar)
        assertEquals(3, user2.rating)
        assertEquals(4, user2.respect)
        assertEquals(date2, user2.lastVisit)
        assertEquals(false, user2.isOnline)

    }

 /*   @Test
    fun test_makeUser() {
        User.resetCounter()

        val user1 = User("0", "Name", "Surname")
        val user2 = User.makeUser("Name Surname")
        user2.lastVisit = user1.lastVisit

        assertEquals(user1, user2)
    }

    @Test
    fun test_builder3() {
        User.resetCounter()

        val lastVisit = Date().add(-10, TimeUnits.DAY)
        val user1 = User("0", "Name", "Surname", lastVisit = lastVisit)
        val user2 = User.Builder()
            .id("0")
            .firstName("Name")
            .lastName("Surname")
            .lastVisit(lastVisit)
            .build()
        val user3 = User.Builder()
            .firstName("Name")
            .lastName("Surname")
            .lastVisit(lastVisit)
            .build()

        assertEquals(user1, user2)
        assertEquals(user1, user3)
    }
*/
    @Test
    fun test_parseFullName() {
        assertEquals("Name" to "Surname", Utils.parseFullName("Name Surname"))
        assertEquals(null to null, Utils.parseFullName(null))
        assertEquals(null to null, Utils.parseFullName(""))
        assertEquals(null to null, Utils.parseFullName(" "))
        assertEquals("John" to null, Utils.parseFullName("John"))
    }

    @Test
    fun test_toInitials3() {
        assertEquals("JD", Utils.toInitials("john", "doe"))
        assertEquals("J", Utils.toInitials("John", null))
        assertEquals(null, Utils.toInitials(null, null))
        assertEquals(null, Utils.toInitials(" ", ""))
    }


    @Test
    fun test_transliteration3() {
        assertEquals("Zhenya Stereotipov", Utils.transliteration("Женя Стереотипов"))
        assertEquals("Amazing_Petr", Utils.transliteration("Amazing Петр", "_"))
    }

    @Test
    fun test_dateAdd() {
        val dateAdd15min = Date(1561612500000)
        val dateAdd6hr = Date(1561633200000)
        val date3daysAgo = Date(1561352400000)

        assertEquals(dateAdd15min, Date(1561611600000).add(15, TimeUnits.MINUTE))
        assertEquals(dateAdd6hr, Date(1561611600000).add(6, TimeUnits.HOUR))
        assertEquals(date3daysAgo, Date(1561611600000).add(-3, TimeUnits.DAY))
    }

    @Test
    fun test_humanizeDiff3() {
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("5 минут назад", Date().add(-5, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 111 дней", Date().add(111, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 211 дней", Date().add(211, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 203 дня", Date().add(203, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 103 дня", Date().add(103, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
    }

    @Test
    fun test_plurals() {
        assertEquals("2 часа назад", Date().add(-2, TimeUnits.HOUR).humanizeDiff())
        assertEquals("5 дней назад", Date().add(-5, TimeUnits.DAY).humanizeDiff())
        assertEquals("5 минут назад", Date().add(-5, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 2 минуты", Date().add(2, TimeUnits.MINUTE).humanizeDiff())
        assertEquals("через 7 дней", Date().add(7, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 111 дней", Date().add(111, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 211 дней", Date().add(211, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 203 дня", Date().add(203, TimeUnits.DAY).humanizeDiff())
        assertEquals("через 103 дня", Date().add(103, TimeUnits.DAY).humanizeDiff())
        assertEquals("более года назад", Date().add(-400, TimeUnits.DAY).humanizeDiff())
        assertEquals("более чем через год", Date().add(400, TimeUnits.DAY).humanizeDiff())
    }
    @Test
    fun test_timeunit_plural() {
        val result1 = TimeUnits.SECOND.plural(1) //1 секунду
        val result2 = TimeUnits.MINUTE.plural(4) //4 минуты
        val result3 = TimeUnits.HOUR.plural(19) //19 часов
        val result4 = TimeUnits.DAY.plural(222) //222 дня
        val result5 = TimeUnits.DAY.plural(111) //111 дней

        assertEquals("1 секунду", result1)
        assertEquals("4 минуты", result2)
        assertEquals("19 часов", result3)
        assertEquals("222 дня", result4)
        assertEquals("111 дней", result5)
    }

    @Test
    fun test_string_truncate() {
        val result1 = "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate() //Bender Bending R...
        val result2 = "Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(15) //Bender Bending...
        val result3 = "A     ".truncate(3) //A
        val result4 = "     A     ".truncate(3) //A
        val result5 = "Посмотрим на это".truncate(9)
        val result6 = "Посмотрим на это".truncate(10)

        assertEquals("Посмотрим...",result5)
        assertEquals("Посмотрим...",result6)
        assertEquals("Bender Bending R...", result1)
        assertEquals("Bender Bending...", result2)
        assertEquals("A", result3)
        assertEquals("A", result4)
    }


    @Test
    fun test_string_striphtml() {
        val result1 = "<p class=\"title\">Образовательное IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch
        val result2 = "<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch

        assertEquals("Образовательное IT-сообщество Skill Branch", result1)
        assertEquals("Образовательное IT-сообщество Skill Branch", result2)
    }
}