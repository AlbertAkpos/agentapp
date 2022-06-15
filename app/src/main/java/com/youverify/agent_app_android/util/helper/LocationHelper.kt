package com.youverify.agent_app_android.util.helper

import android.content.Context
import android.content.ContextWrapper
import android.location.Geocoder
import android.location.LocationManager
import android.os.CountDownTimer
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.util.Permissions
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class LocationHelper @Inject constructor(@ApplicationContext private val context: Context) : ContextWrapper(context) {

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    private val settinsClient by lazy { LocationServices.getSettingsClient(this) }

    private val currentLocationRequest = CurrentLocationRequest.Builder().apply {
        setDurationMillis(1000)
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setMaxUpdateAgeMillis(3 * 1000)
    }.build()

    private val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setFastestInterval(1000) // Every 1sec for location resource sharing
        .setInterval(3 * 1000) // Get Location every 3 seconds

    private val locationSettinsRequest by lazy {  LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build() }

    @RequiresPermission(allOf = [Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_BACKGROUND_LOCATION, Permissions.ACCESS_FINE_LOCATION])
    fun getCurrentLocation(callback: (latLng: TasksDomain.LatLong?, address: String?) -> Unit) {

        // Set a timer that waits for 5 seconds to get location update
        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                callback(null, NOT_FOUND)
            }
        }
        timer.start()

        fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, null).addOnSuccessListener { location ->
            if (location != null) {
                timer.cancel()
                val latLng = TasksDomain.LatLong(location.latitude, location.longitude)
                val address = getAddress(location.latitude, location.longitude)
                callback(latLng, address)
            }
        }
    }

    private fun getAddress(lat: Double, lng: Double): String? {
        return try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(lat, lng, 1)
            addresses?.get(0)?.getAddressLine(0)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    fun isGpsEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    companion object {
        const val NOT_FOUND = "Location not found"
    }
}