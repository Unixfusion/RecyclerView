package com.unixfusion.recyclerview

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.unixfusion.recyclerview.databinding.ActivityMainBinding
import com.unixfusion.recyclerview.model.User
import com.unixfusion.recyclerview.model.UserListener
import com.unixfusion.recyclerview.model.UsersService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val userService: UsersService
        get() = (applicationContext as App).usersSevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onUserMove(user: User, moveBy: Int) {
                userService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                userService.deleteUser(user)
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        userService.addListener(usersListener)
        }

    override fun onDestroy() {
        super.onDestroy()
        userService.removeListener(usersListener)
    }

    private val usersListener: UserListener = {
        adapter.users = it
    }
}