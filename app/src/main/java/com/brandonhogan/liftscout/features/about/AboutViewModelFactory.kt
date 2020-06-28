package com.brandonhogan.liftscout.features.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brandonhogan.liftscout.repository.repo.AboutRepo

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            AboutViewModelFactory
 * @Description     {{ foo }}
 */

class AboutViewModelFactory (private val aboutRepo: AboutRepo)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AboutViewModel(aboutRepo) as T
    }
}