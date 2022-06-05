package com.androgeek.gymapplication.ui.userdetailsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androgeek.gymapplication.R
import com.androgeek.gymapplication.ui.home.HomeViewModel

class UserDetailsFragment(userdataToView: HomeViewModel.UserdataToView) : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var userData : HomeViewModel.UserdataToView = userdataToView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.user_details_list, container, false)
        initializeViews()
        return root
    }

    private fun initializeViews() {

    }
}