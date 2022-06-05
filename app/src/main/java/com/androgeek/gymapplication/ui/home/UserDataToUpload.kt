package com.androgeek.gymapplication.ui.home


import com.google.firebase.database.Exclude

data class UserDataToUpload(
    var userId: String? = null, var userName: String? = null,
    var UserPhone: String? = null, var UserJoinDate: String? = null,
    var userImageExt: String? =null,
    var ImageUrl: String? = null,
    @get :Exclude
    @set  :Exclude
    var key:String? = null
)
