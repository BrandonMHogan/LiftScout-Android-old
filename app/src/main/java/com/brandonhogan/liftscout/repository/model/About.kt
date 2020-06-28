package com.brandonhogan.liftscout.repository.model

import io.realm.RealmObject

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            About
 * @Description     About the app
 */

open class About(
    var name: String = "Lift Scout",
    var version: String = "1.0.0"
): RealmObject()