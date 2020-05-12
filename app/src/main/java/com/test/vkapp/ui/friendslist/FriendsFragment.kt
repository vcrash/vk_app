package com.test.vkapp.ui.friendslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.vkapp.R
import com.test.vkapp.databinding.FragmentFriendsBinding
import com.test.vkapp.ui.login.LoginActivityViewModelAbstract
import com.test.vkapp.view.SingleToast
import kotlinx.android.synthetic.main.fragment_friends.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FriendsFragment : Fragment() {
    private val args: FriendsFragmentArgs by navArgs()
    private val viewModel: FriendsViewModelAbstract by viewModel { parametersOf(args.userId, args.accessToken) }
    private val activityViewModel: LoginActivityViewModelAbstract by sharedViewModel()
    private var toast: SingleToast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        with (FragmentFriendsBinding.inflate(inflater, container, false)) {
            setupListView(recyclerView)
            setupRefreshButton(refreshButton)
            setupToast(root.context)
            return root
        }
    }

    private fun setupListView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        updateListView(emptyList(), recyclerView)
    }

    private fun setupRefreshButton(button: View) {
        button.setOnClickListener {
            onRequestPageClick()
        }
    }

    private fun setupToast(context: Context) {
        toast = SingleToast(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeForData()
        viewModel.getPage()
    }

    private fun subscribeForData() {
        with(viewModel) {
            friendsList.observe(viewLifecycleOwner, Observer {
                updateListView(it, recycler_view)
            })
            fullscreenProgressData.observe(viewLifecycleOwner, Observer {
                setProgressVisible(it)
            })
            onNeedToastData.observe(viewLifecycleOwner, Observer {
                toast?.show(it)
            })
            onNeedCenterRefreshButton.observe(viewLifecycleOwner, Observer {
                setCenterRefreshButtonVisible(it)
            })
            onNeedLogin.observe(viewLifecycleOwner, Observer { repeatLogin() })
        }
        with(activityViewModel) {
            onAccessTokenResetData.observe(viewLifecycleOwner, Observer { viewModel.onAccessTokenReset() })
        }
    }

    private fun updateListView(listData: List<FriendsListItem>, recyclerList: RecyclerView?) {
        val listView = recyclerList ?: return
        if (listData.isEmpty()) {
            listView.visibility = View.INVISIBLE
        } else {
            listView.visibility = View.VISIBLE
        }
        (listView.adapter as? FriendsListAdapter)?.setDataAndNotify(listData) ?:
        getNewAdapter(listData).also {
            listView.adapter = it
        }
    }

    private fun getNewAdapter(listData: List<FriendsListItem>) : FriendsListAdapter {
        return FriendsListAdapter(listData, this::onRequestPageClick, this::onPageLoaderAutoLoaderHasShown)
    }

    private fun onPageLoaderAutoLoaderHasShown() {
        viewModel.onPageLoaderProgressHasShown()
    }

    private fun onRequestPageClick() {
        viewModel.onPageRequestTapped()
    }

    private fun setProgressVisible(visible: Boolean) {
        progress?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setCenterRefreshButtonVisible(visible: Boolean) {
        refresh_button?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun repeatLogin() {
        findNavController().popBackStack(R.id.login_fragment, false)
    }

    companion object {
        const val TAG = "FriendsFragment"
    }
}