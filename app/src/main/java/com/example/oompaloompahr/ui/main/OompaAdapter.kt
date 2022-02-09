package com.example.oompaloompahr.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oompaloompahr.api.OompaLoompa
import com.example.oompaloompahr.databinding.ListItemBinding

class OompaAdapter(val onClick: () -> Unit): ListAdapter<OompaLoompa, OompaAdapter.OompaViewHolder>(COMPARATOR) {

    private lateinit var context: Context


    inner class OompaViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(oompaLoompa: OompaLoompa) {

            with(binding) {

                binding.root.setOnClickListener {
                    onClick()
                }

                tvName.text = oompaLoompa.first_name + " " + oompaLoompa.last_name
                tvProfession.text = oompaLoompa.profession

                Glide.with(ivPicture)
                    .load(oompaLoompa.image)
                    .fitCenter()
                    .into(ivPicture)

                }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OompaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return OompaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OompaViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bind(repo)
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<OompaLoompa>() {
            override fun areItemsTheSame(oldItem: OompaLoompa, newItem: OompaLoompa): Boolean =
                // User ID serves as unique ID
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: OompaLoompa, newItem: OompaLoompa): Boolean =
                // Compare full contents (note: Java users should call .equals())
                oldItem == newItem
        }
    }

}