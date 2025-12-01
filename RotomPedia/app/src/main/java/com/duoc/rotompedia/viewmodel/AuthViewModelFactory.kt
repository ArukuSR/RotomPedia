package com.duoc.rotompedia.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duoc.rotompedia.data.local.UsuarioDao

class AuthViewModelFactory(
    private val application: Application,
    private val usuarioDao: UsuarioDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(application, usuarioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}