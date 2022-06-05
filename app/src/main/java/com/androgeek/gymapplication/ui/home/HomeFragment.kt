package com.androgeek.gymapplication.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androgeek.gymapplication.R
import com.androgeek.gymapplication.data.AppConstant
import com.androgeek.gymapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment(),View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    private  var mImageUrl : Uri? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)
        binding.chooseButton.setOnClickListener(this)
        binding.uploadButton.setOnClickListener(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeForLiveData()
    }

    private fun subscribeForLiveData() {
        homeViewModel.loadingStatus.observe(viewLifecycleOwner, {
            if(it.isLoading == true){
                binding.progressBar.visibility = View.VISIBLE
                Toast.makeText(activity,it.errorValue,Toast.LENGTH_LONG).show()
            }else if(it.isLoading == false){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity,it.errorValue,Toast.LENGTH_LONG).show()

            }
        })
    }

    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.chooseButton -> {
               chooseImageFromLocal()
           }
           R.id.uploadButton -> {

               val userId = binding.userId.text.toString()
               val userName = binding.userName.text.toString()
               val userPhone = binding.phoneNumber.text.toString()
               val userJoiningDate = binding.joiningDate.text.toString()
               val userImageUrl = getFileExtension(mImageUrl)
               val userObject = UserDataToUpload(userId,userName,userPhone,userJoiningDate,userImageUrl,mImageUrl.toString())
               homeViewModel.uploadUserDataToFirebaseDb(userObject,mImageUrl,binding.progressBar)
           }

       }
    }

    private fun chooseImageFromLocal() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,AppConstant.PICK_IMAGE_CONTENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AppConstant.PICK_IMAGE_CONTENT && resultCode == RESULT_OK && data != null && data.data != null) {
            mImageUrl = data.data
            binding.imageValue.visibility = View.VISIBLE
            binding.imageValue.setImageURI(mImageUrl)
            print("Sintu: $mImageUrl")
        }
    }

    private fun getFileExtension(userImageUrl: Uri?): String? {
        val cR = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(userImageUrl?.let { cR.getType(it) })
    }
}

