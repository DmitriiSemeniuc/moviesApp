package com.pop.movies.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class AbstractBaseFragment : Fragment() {

    open fun fragmentTag(): String = "$this"

    protected abstract val contentViewId: Int

    protected abstract fun initViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(fragmentTag()).d("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentViewId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun handleError(error: String?) {
        if (error.isNullOrEmpty()) {
            hideError()
        } else {
            showError(error)
        }
    }

    private fun showError(error: String) {
        (activity as? AbstractBaseActivity)?.showError(error)
    }

    private fun hideError() {
        (activity as? AbstractBaseActivity)?.hideError()
    }
}