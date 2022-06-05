package com.androgeek.gymapplication.ui.gallery

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androgeek.gymapplication.R
import com.androgeek.gymapplication.adapter.ListGymUserAdapter
import com.androgeek.gymapplication.databinding.FragmentGalleryBinding
import com.androgeek.gymapplication.ui.home.HomeViewModel
import com.androgeek.gymapplication.ui.userdetailsscreen.UserDetailsFragment

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: HomeViewModel
    private val adapter = ListGymUserAdapter()
    private lateinit var binding : FragmentGalleryBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_gallery, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel.fetchAllUserData()
        setUpRecyclerView()
        subscribeToData()
    }

    private fun subscribeToData() {
        adapter.userDetailsListener = {
          fragmentTransaction(it,activity)
        }
        galleryViewModel.textValue.observe(viewLifecycleOwner, Observer {
            adapter.userDetailsFun(it)
        })
    }

    private fun fragmentTransaction(it: HomeViewModel.UserdataToView, activity: FragmentActivity?) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,UserDetailsFragment(it))?.addToBackStack(null)?.commit()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.apply {
            userRecyclerView.layoutManager = layoutManager
            userRecyclerView.adapter = adapter
            userRecyclerView.isNestedScrollingEnabled = false
            userRecyclerView.setHasFixedSize(false)
        }
    }
}