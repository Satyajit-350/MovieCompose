package com.satyajit.moviecompose

import android.annotation.SuppressLint
import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.satyajit.moviecompose.data.repository.MovieRepository
import com.satyajit.moviecompose.worker.MovieWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {

    @Inject
    @OptIn(ExperimentalPagingApi::class)
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()
        scheduleWork()
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun scheduleWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(MovieWorker::class.java,30,TimeUnit.MINUTES)
                .setInitialDelay(30,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }

}