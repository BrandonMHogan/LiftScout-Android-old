package com.brandonhogan.liftscout.features.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.brandonhogan.liftscout.R
import com.brandonhogan.liftscout.injection.Injector
import com.brandonhogan.liftscout.repository.objects.About
import com.brandonhogan.liftscout.views.base.BaseFragment
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.coroutines.*

class AboutFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = AboutFragment()
    }

    //private val viewModel: AboutViewModel by viewModels {  Injector.provideAboutViewModelFactory() }
    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycleScope.launch {

            viewModel = ViewModelProviders.of(
                    this@AboutFragment,
                    Injector.provideAboutViewModelFactory()
            ).get(AboutViewModel::class.java)


            withContext(Dispatchers.Main) {
                viewModel.getAbout().observe(viewLifecycleOwner, Observer { about ->
                    testName.text = about?.name
                })
            }
        }

        // When button is clicked, instantiate a Comment and add it to DB through the ViewModel
        testButton.setOnClickListener {
            GlobalScope.launch {
                val about = About(
                    "Does this still work while within a launch/async",
                    "2.2.2"
                )
                viewModel.setAbout(about)
            }
        }
    }
}