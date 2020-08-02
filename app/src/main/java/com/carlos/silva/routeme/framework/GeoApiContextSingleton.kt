package com.carlos.silva.routeme.framework

import android.content.Context
import com.carlos.silva.routeme.R
import com.google.maps.GeoApiContext

object GeoApiContextSingleton {
    private var sInstance: GeoApiContext? = null
    fun getApiContext(): GeoApiContext {
        val tmpGeoApiContext = sInstance

        if (tmpGeoApiContext != null) {
            return tmpGeoApiContext
        }

        synchronized(this) {
            val key = "AIzaSyAfcr0lyY94W5ekpN-M6AgmHBqdW4Tv2Ws"
            val geoApiContext = GeoApiContext.Builder()
                .apiKey(key)
                .build()

            sInstance = geoApiContext

            return geoApiContext
        }
    }
}