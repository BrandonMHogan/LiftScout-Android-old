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

class AboutDao() {
    private val _about = MutableLiveData<About>()

    init {
        val realm = Realm.getDefaultInstance()
        val aboutModel = realm.where(AboutModel::class.java).findFirst()
        _about.postValue(aboutModel?.toObject() ?: About())
        realm.close()
    }

    suspend fun setAbout(about: About) = withContext(Dispatchers.IO) {
        val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            // adds the about model to the database
            realm.copyToRealmOrUpdate(AboutModel.from(about))
            // After adding model to the "database",
            // update the value of MutableLiveData
            // which will notify its active observers
            _about.postValue(about)
        }
        realm.close()
    }

    // Casting MutableLiveData to LiveData because its value
    // shouldn't be changed from other classes
    fun getAbout() = _about as LiveData<About>
}