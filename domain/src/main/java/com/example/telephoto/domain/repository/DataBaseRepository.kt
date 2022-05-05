package com.example.telephoto.domain.repository

import com.example.telephoto.domain.models.Client

interface DataBaseRepository {

    fun addClient(client: Client): Boolean

    fun getClientByNickname(client: Client): Client?

    fun deleteClientByNickname(client: Client): Boolean

    fun getAll(): ArrayList<Client>

}