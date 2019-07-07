package com.jacobarau.fossrun

import android.app.Application

private lateinit var tripModel : TripModel
public fun getTripModel() : TripModel {
    return tripModel
}

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        tripModel = TripModel(applicationContext)
    }
}