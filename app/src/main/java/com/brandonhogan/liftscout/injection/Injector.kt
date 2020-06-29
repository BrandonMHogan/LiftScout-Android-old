package com.brandonhogan.liftscout.injection

import androidx.lifecycle.LifecycleCoroutineScope
import com.brandonhogan.liftscout.features.about.AboutViewModelFactory
import com.brandonhogan.liftscout.repository.database.LiftDatabase
import com.brandonhogan.liftscout.repository.repo.AboutRepo
import kotlinx.coroutines.*

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            Injector
 * @Description     {{ foo }}
 */

object Injector {

    // This will be called from About Fragment
    suspend fun provideAboutViewModelFactory(): AboutViewModelFactory = withContext(Dispatchers.Default) {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        //val aboutRepo = AboutRepo.getInstance(LiftDatabase.getInstance().aboutDao)
        AboutViewModelFactory(getAboutRepo())

    }

    private suspend fun getAboutRepo() = withContext(Dispatchers.IO) {
        AboutRepo.getInstance(LiftDatabase.getInstance().aboutDao)
    }

}