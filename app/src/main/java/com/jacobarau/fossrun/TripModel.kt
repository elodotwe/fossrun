/*
Copyright 2019 Jacob Rau

This file is part of FOSSRun.

FOSSRun is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

FOSSRun is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with FOSSRun.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jacobarau.fossrun

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import io.jenetics.jpx.GPX
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.DateFormat
import java.util.*


class TripModel(private val appContext: Context) {
    var activeTripName: String? = null
    private val locationList = LinkedList<Location>()
    private val TAG = "TripModel"
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    fun startTrip() {
        if (activeTripName != null) return

        val c = Calendar.getInstance().time

        val df = DateFormat.getDateTimeInstance()
        activeTripName = df.format(c)

        Log.i(TAG, "startTrip: trip started with name $activeTripName")

        // Acquire a reference to the system Location Manager

        locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                Log.i(TAG, "onLocationChanged: $location")
                locationList.add(location)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                Log.d(TAG, "onStatusChanged: $provider, $status")
            }

            override fun onProviderEnabled(provider: String) {
                Log.d(TAG, "onProviderEnabled: $provider")
            }

            override fun onProviderDisabled(provider: String) {
                Log.d(TAG, "onProviderDisabled: $provider")
            }
        }

        // Register the listener with the Location Manager to receive location updates
        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        } catch (e: SecurityException) {
            Log.e(TAG, "onCreate: failed to request location updates", e)
        }
    }

    fun stopTrip() {
        if (activeTripName == null) return
        locationManager?.removeUpdates(locationListener)
        Log.i(TAG, "stopTrip: about to build GPX")

        val json = JSONObject()
        val points = JSONArray()
        json.put("points", points)
        locationList.forEach {
            val point = JSONObject()
            points.put(point)
            point.put("lat", it.latitude)
            point.put("lon", it.longitude)
            point.put("ele", it.altitude)
        }
        val gpx = GPX.builder().addTrack { track -> track.addSegment {
                segment -> for (loc in locationList) {
            segment.addPoint {
                    point -> point.lat(loc.latitude)
                .lon(loc.longitude)
                .ele(loc.altitude)
                .src(loc.provider)
            }
        }
        } }.build()

        Log.i(TAG, "stopTrip: GPX built, writing to disk")
        val outFile = File(appContext.getExternalFilesDir(null), "$activeTripName.gpx")

        GPX.write(gpx, outFile.outputStream())
        Log.i(TAG, "stopTrip: GPX written to ${outFile.absolutePath}")

        activeTripName = null
    }
}