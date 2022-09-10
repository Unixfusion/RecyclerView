package com.unixfusion.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unixfusion.recyclerview.databinding.ItemUserBinding
import com.unixfusion.recyclerview.model.User

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    var users: List<User> = emptyList()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.userBinding) {
            tvNickname.text = user.name
            tvCompanyName.text = user.company

            if (user.photo.isNotBlank()) {
                Glide.with(imageAvatar.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_person_avatar)
                    .error(R.drawable.ic_person_avatar)
                    .into(imageAvatar)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    class UsersViewHolder(
        val userBinding: ItemUserBinding
    ) : RecyclerView.ViewHolder(userBinding.root)
}