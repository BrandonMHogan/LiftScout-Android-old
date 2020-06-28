package com.brandonhogan.liftscout.repository.database

import com.brandonhogan.liftscout.repository.dao.AboutDao
import io.realm.Realm

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            database
 * @Description     {{ foo }}
 */

// Private primary constructor inaccessible from other classes
class Database private constructor(val realm: Realm) {

    // All the DAOs go here!
    var aboutDao = AboutDao(realm)
        private set

    companion object {
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: Database? = null

        // The only way to get hold of the FakeDatabase object
        fun getInstance() =
        // Already instantiated? - return the instance
            // Otherwise instantiate in a thread-safe manner
            instance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                instance ?: Database(Realm.getDefaultInstance()).also { instance = it }
            }
    }
}