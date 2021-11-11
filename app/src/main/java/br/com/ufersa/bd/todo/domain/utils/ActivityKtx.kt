package br.com.ufersa.bd.todo.domain.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> AppCompatActivity.viewBindings(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun Activity.openActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

inline fun Activity.openActivity(cls: Class<*>, extras: Bundle.() -> Unit) {
    startActivity(Intent(this, cls).putExtras(Bundle().apply(extras)))
}

fun Activity.newActivity(cls: Class<*>) {
    openActivity(cls)
    finish()
}

inline fun Activity.newActivity(cls: Class<*>, extras: Bundle.() -> Unit) {
    openActivity(cls, extras)
    finish()
}