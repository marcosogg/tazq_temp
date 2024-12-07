package ie.setu.tazq

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TazqApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("TazqApplication", "Starting Tazq Application")
    }
}
