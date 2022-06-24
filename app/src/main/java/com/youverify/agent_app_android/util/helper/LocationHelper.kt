package com.youverify.agent_app_android.util.helper

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.CountDownTimer
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.youverify.agent_app_android.data.model.tasks.TasksDomain
import com.youverify.agent_app_android.features.task.views.TaskDetailsFragment
import com.youverify.agent_app_android.util.Permissions
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHelper @Inject constructor(@ApplicationContext private val context: Context) : ContextWrapper(context) {

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }



   private val settinsClient by lazy { LocationServices.getSettingsClient(this) }

    private val currentLocationRequest by lazy {  CurrentLocationRequest.Builder().apply {
        setDurationMillis(3000)
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setMaxUpdateAgeMillis(0) //0 means return only freshly derived location
    }.build() }

    private val locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setFastestInterval(1000) // Every 1sec for location resource sharing
        .setInterval(3 * 1000) // Get Location every 3 seconds

    private val locationSettingsRequest by lazy {  LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build() }

    @RequiresPermission(allOf = [Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_BACKGROUND_LOCATION, Permissions.ACCESS_FINE_LOCATION])
    fun getCurrentLocation(callback: (latLng: TasksDomain.LatLong?, address: String?) -> Unit) {

        // Set a timer that waits for 5 seconds to get location update
        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                Timber.d("onFinish()")
                callback(null, NOT_FOUND)
            }
        }
        timer.start()

        fusedLocationProviderClient.getCurrentLocation( currentLocationRequest, null).addOnSuccessListener { location ->

            Timber.d("Location gotten: $location")
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

    fun onLocationSettingsSuccessListener(onSuccess: () -> Unit): Task<LocationSettingsResponse> {
      return settinsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener { onSuccess() }
    }

    private val locationCallback  = object: LocationCallback() {
        private var resultCallback: (() -> Unit)? = null
        fun setCallBack(callback: () -> Unit) {
            Timber.d("Callback set")
            this.resultCallback = callback
        }
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            // Wait for some time (5 sec) to get better location updates
            val timer = object: CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    Timber.d("onFinish()")
                    resultCallback?.invoke()
                }
            }
            timer.start()

            Timber.d("Location result ==> $locationResult" )
        }

        fun resetCallback() {
            resultCallback = null
        }

    }

    fun resetLocationCallback() {
        locationCallback.resetCallback()
    }

    fun requestLocationUpdate(callback: (() -> Unit)? = null) {
        val permissions = arrayOf(Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_FINE_LOCATION)
        val permissionsGranted = permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
        if (callback != null) locationCallback.setCallBack(callback)
        if (permissionsGranted) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        const val NOT_FOUND = "Location not found"
    }
}