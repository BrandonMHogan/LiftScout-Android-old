package com.brandonhogan.liftscout.features.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.brandonhogan.liftscout.R
import com.brandonhogan.liftscout.injection.Injector
import com.brandonhogan.liftscout.repository.objects.About
import com.brandonhogan.liftscout.views.base.BaseFragment
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = AboutFragment()
    }

    private val viewModel: AboutViewModel by viewModels { Injector.provideAboutViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observing LiveData from the CommentsViewModel which in turn observes
        // LiveData from the repository, which observes LiveData from the DAO ☺
        viewModel.getAbout().observe(viewLifecycleOwner, Observer { about ->
            testName.text = about?.name
        })

        // When button is clicked, instantiate a Comment and add it to DB through the ViewModel
        testButton.setOnClickListener {
            val about = About(
                "Wasssup my dude",
                "2.2.2"
            )
            viewModel.setAbout(about)
        }



        //iewModel  = ViewModelProviders.of(this, Injector.provideAboutViewModelFactory()).get(AboutViewModel::class.java)
    }

}