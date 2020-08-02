package com.carlos.silva.routeme.framework

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalStateException

object RouteMeViewModelFactory : ViewModelProvider.Factory {

    private lateinit var application: Application
    private lateinit var interactors: Interactors

    fun inject(application: Application, interactors: Interactors) {
        this.application = application
        this.interactors = interactors
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (RouteMeViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(Application::class.java, Interactors::class.java)
                .newInstance(application, interactors)
        }

        throw IllegalStateException("Error")
    }
}