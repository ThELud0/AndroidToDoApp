package com.ludo.todoapp.user


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludo.todoapp.data.Api
import com.ludo.todoapp.data.Api.userWebService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Part


class UserViewModel : ViewModel() {
    private val webService = userWebService

    fun editAvatar(@Part avatarPart: MultipartBody.Part) {

        viewModelScope.launch {
            val response = webService.updateAvatar(avatarPart)
            if (response.isSuccessful) {
                println("update avatar réussi")
            } else {
                println("erreur update avatar")
            }
        }
    }


}