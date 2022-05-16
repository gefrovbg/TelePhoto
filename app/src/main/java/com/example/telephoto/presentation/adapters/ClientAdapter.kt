package com.example.telephoto.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.databinding.ClientItemBinding
import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.usecase.AddClientUseCase
import com.example.telephoto.domain.usecase.DeleteClientByNicknameUseCase
import com.example.telephoto.domain.usecase.GetClientByNicknameUseCase

class ClientAdapter (private val listClient: ArrayList<Client>) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {
    private val contextApp = TelegramBotApp.context
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }
    private val deleteClientByNicknameUseCase by lazy { DeleteClientByNicknameUseCase(dataBaseRepository) }
    private val addClientUseCase by lazy { AddClientUseCase(dataBaseRepository) }
    private val getClientByNicknameUseCase by lazy { GetClientByNicknameUseCase(dataBaseRepository) }

    inner class ViewHolder(val binding: ClientItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listClient[position]
        with(holder){
            binding.username.text = "${currentItem.firstName} ${currentItem.lastName}"
            binding.nickname.text = "${currentItem.nickname}"
            if (currentItem.addStatus == true){
                binding.imageButton.setImageResource(R.drawable.ic_add_circle_outline_24)
            }
            binding.imageButton.setOnClickListener {
                val addStatus = getClientByNicknameUseCase.execute(Client(nickname = currentItem.nickname))?.addStatus
                if (addStatus == true) {
                    val newChatId = Client(
                        currentItem.chatId,
                        currentItem.firstName,
                        currentItem.lastName,
                        currentItem.nickname,
                        addStatus = false
                    )
                    Log.d("keka", listClient.indexOf(currentItem).toString())
                    listClient.set(listClient.indexOf(currentItem), newChatId).also {
                        if (deleteClientByNicknameUseCase.execute(currentItem)) {
                            if (addClientUseCase.execute(newChatId)) {
                                binding.imageButton.setImageResource(R.drawable.ic_delete)
                            }
                        }
                    }
                } else if (addStatus == false) deleteItem(position, currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return listClient.size
    }

    fun insertItem(currentItem: Client){
        listClient.add(currentItem)
        Log.d("adapter", listClient.toString())
        notifyItemInserted(listClient.size - 1)
    }


    fun deleteItem(index: Int, currentItem: Client){
        if (deleteClientByNicknameUseCase.execute(currentItem)){
            listClient.removeAt(index)
            Log.d("adapter", listClient.toString())
            notifyItemRemoved(index)
            notifyItemRangeChanged(index,listClient.size)
        }
    }

}