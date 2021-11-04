package br.com.ufersa.bd.todo.domain.utils

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

inline fun <T: ViewBinding> Activity.viewBindings(
    crossinline  bindInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindInflater.invoke(layoutInflater)
}