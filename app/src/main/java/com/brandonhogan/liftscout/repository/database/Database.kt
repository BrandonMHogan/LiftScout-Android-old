package com.brandonhogan.liftscout.repository.database

import com.brandonhogan.liftscout.repository.dao.AboutDao
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            database
 * @Description     {{ foo }}
 */

// Private primary constructor inaccessible from other classes
class LiftDatabase private constructor() {

    // All the DAOs go here!
    var aboutDao = AboutDao()
        private set

    companion object {
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: LiftDatabase? = null

        // The only way to get hold of the FakeDatabase object
        suspend fun getInstance() = withContext(Dispatchers.IO) {
            return@withContext instance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                instance ?: LiftDatabase().also { instance = it }
            }
        }
    }
}