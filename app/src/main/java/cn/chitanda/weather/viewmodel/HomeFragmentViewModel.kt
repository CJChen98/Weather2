package cn.chitanda.weather.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.chitanda.weather.model.Weather
import cn.chitanda.weather.utils.Constant
import com.tencent.mmkv.MMKV

/**
 * @Author:       Chen
 * @Date:         2020/12/29 15:32
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationContext: Context = application.applicationContext
    val currentLocation = MutableLiveData("" to "")
    private val locationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val weatherList = MutableLiveData<List<Weather>>(listOf())

    fun init() {
        val latitude = MMKV.defaultMMKV()?.decodeString(Constant.MMKV_KEY_LATITUDE, "")
        val longitude = MMKV.defaultMMKV()?.decodeString(Constant.MMKV_KEY_LONGITUDE, "")
        if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
            getLastKnowLocation()
        } else {
            currentLocation.value = longitude to latitude
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowLocation() {
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            currentLocation.value =
                lastKnownLocation.longitude.toString() to lastKnownLocation.latitude.toString()
        } else {
            requestUpdateLocation()
        }
    }

    private val locationUpdateListener by lazy {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocation.postValue(location.longitude.toString() to location.latitude.toString())
                locationManager.removeUpdates(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestUpdateLocation() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            0f,
            locationUpdateListener
        )
    }
}