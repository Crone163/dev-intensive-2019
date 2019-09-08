package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.nullableDate
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->

        val result: MutableList<ChatItem> = mutableListOf()
        val chats = chats.groupBy { it.isArchived }
        val allArchive = chats[true]
        val items = chats[false]?.sortedByDescending { it.lastMessageDate()!! }

        if (!allArchive.isNullOrEmpty()) {
            val lastArchive =
                allArchive.maxBy { it.lastMessageDate()!! }

            val firstName =
                "@" + if (lastArchive?.toChatItem()?.chatType == ChatType.GROUP) lastArchive.toChatItem().author else lastArchive?.lastMessageShort()?.second
            val archiveItem = ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                lastArchive!!.lastMessageShort().first,
                allArchive.sumBy { it.unreadableMessageCount() },
                if (lastArchive.lastMessageDate()!!.equals(Date().nullableDate())) "" else lastArchive.lastMessageDate()!!.shortFormat(),
                lastArchive.toChatItem().isOnline,
                ChatType.ARCHIVE,
                firstName

            )

            result.add(archiveItem)
        }
        if (!items.isNullOrEmpty()) {
            result.addAll(items.map { it.toChatItem() })
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