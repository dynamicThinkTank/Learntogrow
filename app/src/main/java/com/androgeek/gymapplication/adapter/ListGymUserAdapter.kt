package com.androgeek.gymapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androgeek.gymapplication.databinding.UserDetailsListBinding
import com.androgeek.gymapplication.ui.home.HomeViewModel
import com.androgeek.gymapplication.ui.utils.loadImage

class ListGymUserAdapter(var userDetailsListener:((userDetails: HomeViewModel.UserdataToView) -> Unit)? = null) : RecyclerView.Adapter<ListGymUserAdapter.ViewHolder>() {

   private val userList : MutableList<HomeViewModel.UserdataToView> = mutableListOf()

    fun userDetailsFun(users: MutableList<HomeViewModel.UserdataToView>){
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    inner class ViewHolder(inflate:  UserDetailsListBinding) : RecyclerView.ViewHolder(inflate.root) {
        private val userNameValue = inflate.UserName
        private val joiningDate = inflate.dateOfJoining
        private val enterMonth = inflate.noOfMonth
        private val imageValue = inflate.teacherImageView
        private val mainContainer = inflate.mainLayout
        lateinit var userDetails: HomeViewModel.UserdataToView

        init {
            mainContainer.setOnClickListener{
                userDetails?.let {
                    userDetailsListener?.invoke(it)
                }
            }
        }
        fun populate(userData: HomeViewModel.UserdataToView) {
            this.userDetails = userData
            userNameValue.text = userData.userName
            joiningDate.text = userData.expiryDate.toString()
            enterMonth.text = userData.userKey
            imageValue.loadImage(userData.ImageUrl)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserDetailsListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populate(userList[position])
    }

    override fun getItemCount(): Int {
       return userList.size
    }

}