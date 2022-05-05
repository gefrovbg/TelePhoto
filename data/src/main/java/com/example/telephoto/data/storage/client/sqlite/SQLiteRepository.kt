package com.example.telephoto.data.storage.client.sqlite

import com.example.telephoto.data.storage.client.models.ClientSQLite


interface SQLiteRepository {

    fun addClient(clientSQLite: ClientSQLite): Boolean

    fun getClientByNickname(clientSQLite: ClientSQLite): ClientSQLite?

    fun deleteClientByNickname(clientSQLite: ClientSQLite): Boolean

    fun getAll(): ArrayList<ClientSQLite>

}