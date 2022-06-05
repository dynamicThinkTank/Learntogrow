package com.androgeek.gymapplication.ui.home

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androgeek.gymapplication.data.AppConstant
import com.androgeek.gymapplication.data.UserViewState
import com.androgeek.gymapplication.viewmodel.FirebaseStorageViewModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.auth.FirebaseUser
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeViewModel : FirebaseStorageViewModel() {

     private var _mStorageReference : StorageReference? = null
    private var mStorage : FirebaseStorage? = null
     private var _mDataBaseReference : DatabaseReference? = null
    private var mUploadTask :StorageTask<*>? = null
    private var mDBListener : ValueEventListener? = null



    private val _textError = MutableLiveData<UserViewState>()
    val loadingStatus: LiveData<UserViewState> = _textError

    private val _textUserDetails = MutableLiveData<MutableList<UserdataToView>>()
    val textValue: LiveData<MutableList<UserdataToView>> = _textUserDetails

    init {
        _mStorageReference = FirebaseStorage.getInstance().getReference(AppConstant.FIREBASE_NODE)
        _mDataBaseReference = FirebaseDatabase.getInstance().getReference(AppConstant.FIREBASE_NODE)
    }


    fun uploadUserDataToFirebaseDb(
        userObject: UserDataToUpload,
        mImageUrl: Uri?,
        progressBar: ProgressBar
    ) {
        _textError.postValue(UserViewState(true,null))
        uploadData(userObject,mImageUrl,progressBar)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchAllUserData(){
         var listOfUser : MutableList<UserdataToView> = mutableListOf<UserdataToView>()
        mStorage = FirebaseStorage.getInstance()
        _mDataBaseReference = FirebaseDatabase.getInstance().getReference(AppConstant.FIREBASE_NODE)
        mDBListener = _mDataBaseReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userSnapshot in snapshot.children){
                    val userData = userSnapshot.getValue(UserDataToUpload::class.java)
                    userData!!.key = userSnapshot.key
                    if(userData.UserJoinDate?.isNotEmpty() == true) {
                        userData.UserJoinDate?.let {
                            val parsedDate = LocalDate.parse(
                                userData.UserJoinDate,
                                DateTimeFormatter.ofPattern("d/M/yyyy")
                            )
                            //println("14/02/2018 : "+parsedDate)
                            // userData?.UserJoinDate = parsedDate
                            val userTransformData = UserdataToView(
                                userData.userId,
                                parsedDate,
                                userData.userName,
                                userData.ImageUrl,
                                userData.UserPhone
                            )
                            listOfUser.add(userTransformData)
                        }
                    }
                }
                listOfUser.sortBy { it?.expiryDate?.year}
                _textUserDetails.postValue(listOfUser)
            }
            override fun onCancelled(error: DatabaseError) {
              Log.d("Jitu:","error while dowloading")
            }

        })

    }

    private fun uploadData(userObject: UserDataToUpload, mImageUrl: Uri?, progressBar: ProgressBar) {

        if (userObject.userImageExt != null){
            val fileReference = _mStorageReference?.child(System.currentTimeMillis().toString()+"."+userObject.userImageExt)
            mImageUrl?.let { fileReference?.putFile(it) }?.addOnSuccessListener{
                Log.d("Jit:","Inside addOnSuccessListener")

                val uploadId = _mDataBaseReference?.push()?.key
                if (uploadId?.isNotEmpty() == true) {
                    _mDataBaseReference?.child(uploadId)?.setValue(userObject)
                }
                Handler().postDelayed({
                    _textError.postValue(UserViewState(false,"Data uploaded successfully !!!"))
                },1000)


            }?.addOnFailureListener{
                Log.d("Jit:","Inside addOnFailureListener")
                Log.e("error while uploading",it.localizedMessage)
                //progressBar.visibility = View.GONE
                _textError.postValue(UserViewState(false,it.localizedMessage))
            }?.addOnProgressListener { taskSnapshot ->
                Log.d("Jit:", "Inside addOnProgressListener")
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                Log.e("Byte transfer", progress.toString())
            }

        }

    }


    override fun onCleared() {
        super.onCleared()
        mDBListener?.let { _mDataBaseReference?.removeEventListener(it) }
    }

data class UserdataToView(val userKey : String?, val expiryDate: LocalDate?,val userName : String?, var ImageUrl: String? = null, var UserPhone: String? = null)
}