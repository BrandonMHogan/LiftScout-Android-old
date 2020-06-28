package com.brandonhogan.liftscout.repository.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brandonhogan.liftscout.repository.objects.About
import com.brandonhogan.liftscout.repository.model.AboutModel
import io.realm.Realm
import kotlinx.coroutines.*

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            AboutDao
 * @Description     {{ foo }}
 */

class AboutDao(val realm: Realm) {
    // MutableLiveData is from the Architecture Components Library
    // LiveData can be observed for changes
    private val _about = MutableLiveData<About>()

    init {
        val aboutModel = realm.where(AboutModel::class.java).findFirst()
        _about.value = aboutModel?.toObject() ?: About()
    }

    suspend fun setAbout(about: About) = withContext(Dispatchers.IO) {
        realm.executeTransaction {
            // adds the about model to the database
            val aboutModel = AboutModel.from(about)
            realm.copyToRealmOrUpdate(AboutModel.from(about))
            // After adding model to the "database",
            // update the value of MutableLiveData
            // which will notify its active observers
            _about.value = about
        }
    }

    // Casting MutableLiveData to LiveData because its value
    // shouldn't be changed from other classes
    suspend fun getAbout() = withContext(Dispatchers.IO) { return@withContext _about as LiveData<About> }
}