package ru.skillbranch.devintensive.models


import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false

) {


    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    companion object Factory {
        private var lastId: Int = -1
        fun makeUser(fullName: String?): User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }
    }


    data class Builder(
        var id: String = "0",
        var firstName: String? = null,
        var lastName: String? = null,
        var avatar: String? = null,
        var rating: Int = 0,
        var respect: Int = 0,
        var lastVisit: Date? = null,
        var isOnline: Boolean = false
    ) {
        fun setId(id: String) = apply { this.id = id }
        fun setFirstName(firstName: String) = apply { this.firstName = firstName }
        fun setLastName(lastName: String) = apply { this.lastName = lastName }
        fun setAvatar(avatar: String) = apply { this.avatar = avatar }
        fun setRating(rating: Int) = apply { this.rating = rating }
        fun setRespect(respect: Int) = apply { this.respect = respect }
        fun setLastVisit(lastVisit: Date) = apply { this.lastVisit = lastVisit }
        fun setIsOnline(isOnline: Boolean) = apply { this.isOnline = isOnline }
        fun build() = User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
    }


    init {
        println(
            "It's Alive!!!\n" +
                    "${if (lastName === "Doe") "His name is ${getFullName()}" else "And his name is ${getFullName()}!!!"}\n"
        )
    }

    //region=================================== GETTERS AND SETTERS ===================================

    fun getFullName(): String {
        return "$firstName $lastName"
    }

    //endregion

}