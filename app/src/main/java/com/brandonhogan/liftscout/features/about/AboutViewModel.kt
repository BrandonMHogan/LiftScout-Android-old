package com.brandonhogan.liftscout.features.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class AboutViewModel : ViewModel() {

    private val realmThreadContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    lateinit var realm: Realm

    init {
        viewModelScope.launch(realmThreadContext) {
            realm = Realm.getDefaultInstance()
        }
    }

    var about: LiveData<String>? = null

}