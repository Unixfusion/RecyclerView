package com.unixfusion.recyclerview

import android.app.Application
import com.unixfusion.recyclerview.model.UsersService

class App: Application() {

    val usersSevice = UsersService()
}