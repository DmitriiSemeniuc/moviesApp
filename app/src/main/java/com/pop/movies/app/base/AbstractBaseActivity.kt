package com.pop.movies.app.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.pop.movies.app.R
import com.pop.movies.app.ext.delayedAction
import com.pop.movies.app.ext.makeSnackBar
import timber.log.Timber

abstract class AbstractBaseActivity : AppCompatActivity() {

    abstract fun getTag() : String

    abstract val contentViewId: Int

    abstract fun getInternetConnectionStatusView(): View

    abstract fun getParentView(): View

    open fun getStatusBarColor() : Int = R.color.statusBarColor

    private var errorSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(getTag()).d("onCreate")

        if (contentViewId != -1) {
            setContentView(contentViewId)
        }

        setStatusBarColor()
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(getTag()).d("onResume")
    }

    override fun onPause() {
        Timber.tag(getTag()).d("onPause")
        super.onPause()
    }

    private fun setStatusBarColor() {
        Timber.tag(getTag()).d("setStatusBarColor")
        window?.let {
            it.statusBarColor = resources.getColor(getStatusBarColor(), this.theme)
        }
    }

    fun onInternetConnectionStateChanged(connected: Boolean) {
        when(connected) {
            true -> showBackOnline()
            else -> showNoInternetConnection()
        }
    }

    private fun showBackOnline() {
        val view = getInternetConnectionStatusView()
        view.findViewById<TextView>(R.id.tv_internet_connection_state)?.apply {
            text = getString(R.string.internet_connection_back_online)
        }
        delayedAction(500) {
            hideError()
            view.visibility = View.GONE
        }
    }

    private fun showNoInternetConnection() {
        val view = getInternetConnectionStatusView()
        view.findViewById<TextView>(R.id.tv_internet_connection_state)?.apply {
            text = getString(R.string.internet_connection_missing)
        }
        delayedAction(100) { view.visibility = View.VISIBLE }
    }

    fun showError(error: String) {
        showError(
            getParentView(),
            error,
            getString(R.string.action_close)
        )
    }

    fun hideError() {
        errorSnackBar?.dismiss()
    }

    private fun showError(
        contextView: View = getParentView(),
        text: String,
        actionText: String,
        onAction: () -> Unit = {}
    ) {
        Timber.tag(getTag()).d("showError")

        errorSnackBar = makeSnackBar(
            parentView = contextView,
            text = text,
            actionText = actionText,
            onAction = onAction
        ).also { it.show() }
    }

    override fun onDestroy() {
        Timber.tag(getTag()).d("onDestroy")
        super.onDestroy()
    }
}