package com.example.telephoto.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.telephoto.data.storage.client.models.ClientSQLite
import com.example.telephoto.data.storage.client.sqlite.SQLiteRepositoryImpl
import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.repository.DataBaseRepository

class DataBaseRepositoryImpl(context: Context, factory: SQLiteDatabase.CursorFactory?): DataBaseRepository {

    private val sqLiteRepository = SQLiteRepositoryImpl(context = context, factory = factory)

    override fun addClient(client: Client): Boolean{

        return sqLiteRepository.addClient(clientSQLite = clientToData(client))

    }

    override fun getClientByNickname(client: Client): Client? {

        return sqLiteRepository.getClientByNickname(clientSQLite = clientToData(client))?.let { clientToDomain(it) }

    }

    override fun deleteClientByNickname(client: Client): Boolean {

        return sqLiteRepository.deleteClientByNickname(clientSQLite = clientToData(client))

    }
    override fun getAll(): ArrayList<Client> {

        return arrayClientSQLiteToDomainArray(sqLiteRepository.getAll())

    }

    fun clientToData (client: Client): ClientSQLite {
        return ClientSQLite(
            chatId = client.chatId,
            firstName = client.firstName,
            lastName = client.lastName,
            nickname = client.nickname,
            addStatus = client.addStatus
        )
    }

    fun clientToDomain(clientSQLite: ClientSQLite): Client{
        return Client(
            chatId = clientSQLite.chatId,
            firstName = clientSQLite.firstName,
            lastName = clientSQLite.lastName,
            nickname = clientSQLite.nickname,
            addStatus = clientSQLite.addStatus
        )
    }

    private fun arrayClientSQLiteToDomainArray(arrayList: ArrayList<ClientSQLite>): ArrayList<Client>{
        val returnArray = arrayListOf<Client>()

        for (i in arrayList){
            returnArray.add(clientToDomain(i))
        }

        return returnArray
    }
}