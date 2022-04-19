package com.example.telephoto.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.databinding.ClientItemBinding
import com.example.telephoto.domain.usecase.DataBaseHelperUseCase
import com.example.telephoto.domain.models.ChatId

class ClientAdapter (private val listChatId: ArrayList<ChatId>) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {
    private val database = DataBaseHelperUseCase(TelegramBotApp.context, null)

    inner class ViewHolder(val binding: ClientItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listChatId[position]
        with(holder){
            binding.username.text = "${currentItem.firstName} ${currentItem.lastName}"
            binding.nickname.text = "${currentItem.nickname}"
            if (currentItem.addStatus){
                binding.imageButton.setImageResource(R.drawable.ic_add_circle_outline_24)
            }
            binding.imageButton.setOnClickListener(object : View.OnClickListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(v: View?) {
                    if (currentItem.addStatus){
                        val newChatId = ChatId(
                            currentItem.chatId,
                            currentItem.firstName,
                            currentItem.lastName,
                            currentItem.nickname,
                            addStatus = false
                        )
                        listChatId.set(listChatId.indexOf(currentItem), newChatId).also {
                            if (database.deleteClientByNickname(currentItem.nickname)){
                                if(database.addClient(newChatId)){
                                    binding.imageButton.setImageResource(R.drawable.ic_delete)
                                }
                            }
                        }
                    }else deleteItem(listChatId.indexOf(currentItem), currentItem)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return listChatId.size
    }

    fun insertItem(currentItem: ChatId){
        val index = listChatId.lastIndex + 1
        listChatId.add(index, currentItem)
        notifyItemInserted(index)
    }


    fun deleteItem(index: Int, currentItem: ChatId){
        listChatId.removeAt(index)
        if (database.deleteClientByNickname(currentItem.nickname)) notifyItemRemoved(index)
    }

}