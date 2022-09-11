package com.unixfusion.recyclerview

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unixfusion.recyclerview.databinding.ItemUserBinding
import com.unixfusion.recyclerview.model.User

interface UserActionListener {

    fun onUserMove(user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}

class UsersAdapter(
    private val actionListener: UserActionListener
): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    var users: List<User> = emptyList()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        val user = v.tag as User

        when(v.id) {
            R.id.imageMore -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.imageMore.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.userBinding) {
            holder.itemView.tag = user
            imageMore.tag = user

            tvNickname.text = user.name
            tvCompanyName.text = user.company

            if (user.photo.isNotBlank()) {
                Glide.with(imageAvatar.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_person_avatar)
                    .error(R.drawable.ic_person_avatar)
                    .into(imageAvatar)
            } else {
                Glide.with(imageAvatar.context).clear(imageAvatar)
                imageAvatar.setImageResource(R.drawable.ic_person_avatar)
            }
        }
    }

    override fun getItemCount(): Int = users.size


    private fun showPopupMenu(view: View) {
        val popupMenu = android.widget.PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.id == user.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                ID_MOVE_UP -> { actionListener.onUserMove(user, -1) }
                ID_MOVE_DOWN -> { actionListener.onUserMove(user, 1) }
                ID_REMOVE -> { actionListener.onUserDelete(user)}
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    class UsersViewHolder(
        val userBinding: ItemUserBinding
    ) : RecyclerView.ViewHolder(userBinding.root)

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }
}