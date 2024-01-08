package com.satyajit.moviecompose.worker

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.satyajit.moviecompose.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class MovieWorker @Inject constructor(
    private val context: Context,
    params: WorkerParameters,
): Worker(context,params) {

    private val repository = (context as MyApplication).movieRepository

    override fun doWork(): Result {
        Log.d("Work_Manager_task", "Task Started")

        CoroutineScope(Dispatchers.IO).launch {
            repository.workerTaskInBackGround()
        }
        return Result.success()
    }

}