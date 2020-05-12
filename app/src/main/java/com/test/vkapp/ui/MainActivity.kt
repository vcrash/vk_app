package com.test.vkapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.test.vkapp.R
import com.test.vkapp.ui.login.LoginActivityViewModelAbstract
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: LoginActivityViewModelAbstract? by viewModel()

    private val vkAccessTokenTracker: VKAccessTokenTracker = object : VKAccessTokenTracker() {
        override fun onVKAccessTokenChanged(
            oldToken: VKAccessToken?,
            newToken: VKAccessToken?
        ) {
            if (newToken == null) {
                viewModel?.tokenHasReset()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
        listenTokenChange()
    }

    private fun initNavigation() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.content)
        val configuration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(this, navController, configuration)
        toolbar.setupWithNavController(navController, configuration)
    }

    private fun listenTokenChange() {
        vkAccessTokenTracker.startTracking();
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (!VKSdk.onActivityResult(
                requestCode,
                resultCode,
                data,
                object : VKCallback<VKAccessToken?> {
                    override fun onResult(res: VKAccessToken?) {
                        res ?: run {
                            viewModel?.provideLoginFailed()
                            return
                        }
                        viewModel?.provideLoginSuccess(res)
                    }
                    override fun onError(error: VKError) {
                        viewModel?.provideLoginFailed()
                    }
                })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
