package com.example.contacthelpler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.contacthelpler.databinding.ContactItemBinding
import com.example.contacthelpler.models.Contact

class ContactAdapter(val list: ArrayList<Contact>, val listener: OnItemClickListener):RecyclerView.Adapter<ContactAdapter.ContactVH>(){


    inner class ContactVH(val itemBinding: ContactItemBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun onBind(contact: Contact){
            itemBinding.nameTv.text = contact.name
            itemBinding.numberTv.text = contact.number
            itemBinding.callCard.setOnClickListener {
                listener.onItemCallClick(contact.number!!)
            }
            itemBinding.sendMessageCard.setOnClickListener {
                listener.onItemSendClick(contact)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        return ContactVH(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemCallClick(number: String)
        fun onItemSendClick(contact: Contact)
    }
}