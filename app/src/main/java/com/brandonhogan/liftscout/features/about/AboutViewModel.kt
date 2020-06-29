package com.brandonhogan.liftscout.features.about

import androidx.lifecycle.ViewModel
import com.brandonhogan.liftscout.repository.objects.About
import com.brandonhogan.liftscout.repository.repo.AboutRepo


class AboutViewModel(private val aboutRepo: AboutRepo) : ViewModel() {

    suspend fun getAbout() = aboutRepo.getAbout()
    suspend fun setAbout(about: About) = aboutRepo.set(about)

}