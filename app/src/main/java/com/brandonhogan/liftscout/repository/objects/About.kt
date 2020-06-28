package com.brandonhogan.liftscout.repository.objects

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            About
 * @Description     {{ foo }}
 */
class About (val name: String = "Lift Scout", val version: String = "1.0.0") {

    override fun toString(): String {
        return "author is : $name, version is: $version"
    }
}