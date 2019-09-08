package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->

        val result: MutableList<ChatItem> = mutableListOf()
        val chats = chats.groupBy { it.isArchived }
        val allArchive = chats[true]

        if (!allArchive.isNullOrEmpty()) {
            val countAllUnreadable =
                allArchive.sumBy { it.unreadableMessageCount() }
            Log.e("CountUnreadable", countAllUnreadable.toString())
            val lastArchive =
                allArchive.sortedByDescending { it.lastMessageDate() }
            //Дата сообщения может быть пуста???
            val lastDate = lastArchive.filter { it.lastMessageDate() != null }

            val archiveItem = ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                lastArchive.first().lastMessageShort().first,
                countAllUnreadable,
                if (lastDate.size > 0) lastDate.first().lastMessageDate()!!.shortFormat() else "",
                lastArchive.first().toChatItem().isOnline,
                ChatType.ARCHIVE,
                "@" + lastArchive.first().lastMessageShort().second

            )
            result.add(archiveItem)
        }
        if (!chats[false].isNullOrEmpty()) {
            val items = chats[false]!!.map { it.toChatItem() }.sortedBy { it.id.toInt() }
            result.addAll(items)
        }
        return@map result
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatList = chats.value!!

            result.value = if (queryStr.isEmpty()) chatList
            else chatList.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }


    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }

}