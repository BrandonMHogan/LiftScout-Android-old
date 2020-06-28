package com.brandonhogan.liftscout.repository.repo

import com.brandonhogan.liftscout.repository.objects.About
import com.brandonhogan.liftscout.repository.dao.AboutDao

/**
 * @Creator         Brandon Hogan
 * @Date            2020-06-28
 * @File            AboutRepo
 * @Description     {{ foo }}
 */

class AboutRepo private constructor(private val aboutDao: AboutDao){

    // This may seem redundant.
    // Imagine a code which also updates and checks the backend.
    fun set(about: About) {
        aboutDao.setAbout(about)
    }

    fun getAbout() = aboutDao.getAbout()

    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: AboutRepo? = null

        fun getInstance(aboutDao: AboutDao) =
            instance ?: synchronized(this) {
                instance ?: AboutRepo(aboutDao).also { instance = it }
            }
    }
}