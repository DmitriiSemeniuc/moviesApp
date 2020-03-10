package com.pop.movies.app.fragments

import com.pop.movies.app.R
import com.pop.movies.app.base.AbstractBaseFragment
import timber.log.Timber

class EmptyFragment : AbstractBaseFragment() {

    override fun fragmentTag() = TAG

    override val contentViewId: Int = R.layout.fragment_empty

    override fun initViews() {
        Timber.tag(TAG).d("initViews")
    }

    companion object {

        const val TAG = "EmptyFr"
        fun newInstance() = EmptyFragment()
    }
}