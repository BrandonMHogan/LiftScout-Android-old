package com.brandonhogan.liftscout.injection

import com.brandonhogan.liftscout.features.about.AboutViewModelFactory
import com.brandonhogan.liftscout.repository.database.Database
import com.brandonhogan.liftscout.repository.repo.AboutRepo

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            Injector
 * @Description     {{ foo }}
 */

object Injector {

    // This will be called from About Fragment
    fun provideAboutViewModelFactory(): AboutViewModelFactory {
        // ViewModelFactory needs a repository, which in turn needs a DAO from a database
        // The whole dependency tree is constructed right here, in one place
        val aboutRepo = AboutRepo.getInstance(Database.getInstance().aboutDao)
        return AboutViewModelFactory(aboutRepo)
    }
}