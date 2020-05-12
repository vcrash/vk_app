package com.test.vkapp.view

import android.content.Context
import android.widget.Toast
import java.lang.ref.WeakReference

class SingleToast(context: Context) {
    var toast: Toast? = null
    private val weakContext = WeakReference(context)

    fun show(res: Int, duration: Int = Toast.LENGTH_LONG) {
        show((weakContext.get() ?: return).getString(res), duration)
    }

    fun show(message: String, duration: Int = Toast.LENGTH_LONG) {
        val context = weakContext.get() ?: return
        toast?.also {
            it.setText(message)
            it.show()
        } ?: run {
            toast = Toast.makeText(context, message, duration).also { it.show() }
        }
    }
}