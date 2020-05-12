package com.test.vkapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.vkapp.R
import com.test.vkapp.databinding.FragmentLoginBinding
import com.vk.sdk.VKSdk
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModelAbstract by viewModel()
    private val activityViewModel: LoginActivityViewModelAbstract by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(FragmentLoginBinding.inflate(inflater, container, false)) {
            return root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listenData()
        viewModel.initWorkFlow()
    }

    private fun listenData() {
        with(viewModel) {
            needLogin.observe(viewLifecycleOwner, Observer {
                vkLogin(it)
            })
            needShowFriendsList.observe(viewLifecycleOwner, Observer {
                navigateToFriends(it)
            })
            needShowRepeatLoginDialog.observe(viewLifecycleOwner, Observer { showLoginRequiredDialog() })
            needShutDown.observe(viewLifecycleOwner, Observer { shutDown() })
        }
        with(activityViewModel) {
            loginSuccessData.observe(viewLifecycleOwner, Observer { viewModel.loginSucceed(it) })
            loginFailedData.observe(viewLifecycleOwner, Observer { viewModel.loginFailed() })
            onAccessTokenResetData.observe(viewLifecycleOwner, Observer { viewModel.loginFailed() })
        }
    }

    private fun vkLogin(vkScope: String) {
        val activityChecked = activity ?: return
        VKSdk.login(activityChecked, vkScope)
    }

    private fun showLoginRequiredDialog() {
        MaterialAlertDialogBuilder(context ?: return)
            .setMessage(R.string.login_repeat_dialog_message)
            .setPositiveButton(R.string.login_repeat_action) { _, _ ->
                viewModel.loginRepeatConfirmed()
            }.setNegativeButton(android.R.string.cancel) { _, _ ->
                viewModel.loginRepeatDeclined()
            }.setOnCancelListener {
                viewModel.loginRepeatDeclined()
            }.show()
    }

    private fun navigateToFriends(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    private fun shutDown() {
        activity?.finish()
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}