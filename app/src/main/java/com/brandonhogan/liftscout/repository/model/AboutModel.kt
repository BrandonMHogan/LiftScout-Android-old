package com.brandonhogan.liftscout.repository.model

import com.brandonhogan.liftscout.repository.objects.About
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            About
 * @Description     Database layer model for the about object
 */

open class AboutModel(
    var name: String = "",
    @PrimaryKey
    var version: String = ""
): RealmObject() {

    /**
     * Conversion function, to convert the view model layer object to the data layer object
     */
    companion object {
        fun from(about: About): AboutModel = AboutModel(about.name, about.version)
    }

    fun toObject(): About =
        About(
            this.name,
            this.version
        )
}