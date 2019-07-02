package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId: Int = -1
        //Реализуй паттерн AbstractFactory с методом makeMessage(from, chat, date, type, payload, isIncoming = false)
        fun makeMessage(
            from: User?,
            chat: Chat,
            // Я хз ошибка это или нет, но в реализации патерна сначала идёт тип, потом payload, а в примере наоборот: сначала payload, потом тип
            date: Date = Date(),
            type: String = "text",
            payload: Any?,
            isIncoming: Boolean = false
        ): BaseMessage {
            lastId++
            return when (type) {
                "image" -> ImageMessage("$lastId", from, chat, isIncoming, date, image = payload as String)
                else -> TextMessage("$lastId", from, chat, isIncoming, date, text = payload as String)
            }
        }

    }
}