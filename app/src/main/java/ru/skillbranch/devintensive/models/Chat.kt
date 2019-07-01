package ru.skillbranch.devintensive.models

class Chat(
    val id: String,
    members: MutableList<User> = mutableListOf(),
    messages: MutableList<BaseMessage> = mutableListOf()
) {

}