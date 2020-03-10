package com.pop.movies.app.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.pop.movies.app.R
import com.pop.movies.app.base.AbstractBaseActivity
import com.pop.movies.app.ext.*
import com.pop.movies.app.fragments.EmptyFragment
import com.pop.movies.app.fragments.MoviesDetailsFragment
import com.pop.movies.app.fragments.MoviesListFragment
import com.pop.movies.app.models.Movie
import com.pop.movies.app.viewmodels.MoviesActivityViewModel
import com.pop.movies.app.viewmodels.MoviesActivityViewModel.Companion.selectedMovieLiveData
import kotlinx.android.synthetic.main.activity_movies.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MoviesActivity : AbstractBaseActivity() {

    override fun getTag(): String = TAG

    override val contentViewId: Int = R.layout.activity_movies

    override fun getInternetConnectionStatusView(): View = layout_internet_connection_state

    override fun getParentView(): View = movies_content

    private val model: MoviesActivityViewModel by viewModel()

    private var listFragment: MoviesListFragment? = null
    private var detailsFragment: MoviesDetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUiMode(savedInstanceState)
        listenForInternetConnectionChanges()
        observeViewModel()
    }

    private fun setupUiMode(bundle: Bundle?) {
        Timber.tag(TAG).d("setupUiMode: ")

        when {
            isTablet() -> setupTabletMode(bundle)
            else -> setupDefaultMode(bundle)
        }
    }

    private fun setupTabletMode(bundle: Bundle?) {
        Timber.tag(TAG).d("setupTabletMode: ")

        when {
            isLandscape() -> setupTabletLandMode(bundle)
            else -> setupTabletPortMode(bundle)
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        Timber.tag(TAG).d("onSaveInstanceState: ")

        listFragment?.let {
            saveFragmentsState(MoviesListFragment.TAG, bundle)
        }

        detailsFragment?.let {
            saveFragmentsState(MoviesDetailsFragment.TAG, bundle)
        }
    }

    private fun setupDefaultMode(bundle: Bundle?) {
        Timber.tag(TAG).d("setupDefaultMode: ")

        setupListToolbar()
        restoreDefaultInstanceState(bundle)
        initListFragment()
        addDetailsFragmentInDefaultMode()
    }

    private fun setupTabletLandMode(bundle: Bundle?) {
        Timber.tag(TAG).d("setupTabletLandMode: ")

        setupListToolbar()
        restoreTabletLandInstanceState(bundle)

        initListFragment()
        if(selectedMovieLiveData.value == null) {
            showEmptyFragment()
        } else {
            addDetailsFragmentInTabletLandMode()
        }
    }

    private fun setupTabletPortMode(bundle: Bundle?) {
        Timber.tag(TAG).d("setupTabletPortMode: ")

        setupListToolbar()
        restoreTabletPortInstanceState(bundle)
        initListFragment()
    }

    private fun restoreDefaultInstanceState(bundle: Bundle?) {
        Timber.tag(TAG).d("restoreDefaultInstanceState: ")

        bundle?.let {
            listFragment = findFragmentByTag<MoviesListFragment>(bundle, MoviesListFragment.TAG)
            detailsFragment =
                findFragmentByTag<MoviesDetailsFragment>(bundle, MoviesDetailsFragment.TAG)
        }
    }

    private fun restoreTabletPortInstanceState(bundle: Bundle?) {
        Timber.tag(TAG).d("restoreTabletPortInstanceState: ")

        bundle?.let {
            listFragment = findFragmentByTag<MoviesListFragment>(bundle, MoviesListFragment.TAG)
        }
    }

    private fun restoreTabletLandInstanceState(bundle: Bundle?) {
        Timber.tag(TAG).d("restoreTabletLandInstanceState: ")
        restoreDefaultInstanceState(bundle)
    }

    private fun addDetailsFragmentInDefaultMode() {
        Timber.tag(TAG).d("addDetailsFragmentInDefaultMode: ")

        if (detailsFragment != null) {
            addOrShowFragment(
                container = R.id.fl_content,
                fragment = detailsFragment!!,
                tag = detailsFragment!!.fragmentTag(),
                addToBackStack = true
            )
        }
    }

    private fun addDetailsFragmentInTabletLandMode() {
        Timber.tag(TAG).d("addDetailsFragmentInTabletLandMode: ")

        if(detailsFragment == null) {
            detailsFragment = MoviesDetailsFragment.newInstance()
        }

        setDetailsLayoutVisibility(View.VISIBLE)

        replaceOrShowFragment(
            container = R.id.fl_details,
            fragment = detailsFragment!!,
            tag = detailsFragment!!.fragmentTag(),
            addToBackStack = false
        )
    }

    private fun addDetailsFragmentInTabletPortMode() {
        Timber.tag(TAG).d("addDetailsFragmentInTabletPortMode: ")

        setDetailsLayoutVisibility(View.VISIBLE)

        addOrShowFragment(
            container = R.id.fl_details,
            fragment = detailsFragment!!,
            tag = detailsFragment!!.fragmentTag(),
            addToBackStack = true
        )
    }

    private fun showDetailsFragmentInDefaultMode() {
        Timber.tag(TAG).d("showDetailsFragmentInDefaultMode: ")

        if (detailsFragment == null) {
            Timber.tag(TAG).d("create new detailsFragment")
            detailsFragment = MoviesDetailsFragment.newInstance()
        }
        addDetailsFragmentInDefaultMode()
        setupDetailsToolbar()
    }

    private fun showDetailsFragmentInTabletPortMode() {
        Timber.tag(TAG).d("showDetailsFragmentInTabletPortMode: ")

        Timber.tag(TAG).d("create new detailsFragment")
        detailsFragment = MoviesDetailsFragment.newInstance()
        addDetailsFragmentInTabletPortMode()
        setupDetailsToolbar()
    }

    private fun showDetailsFragmentInTabletLandMode() {
        Timber.tag(TAG).d("showDetailsFragmentInTabletLandMode: ")

        if (detailsFragment == null) {
            Timber.tag(TAG).d("create new detailsFragment")
            detailsFragment = MoviesDetailsFragment.newInstance()
        }
        setDetailsLayoutVisibility(View.VISIBLE)

        replaceOrShowFragment(
            container = R.id.fl_details,
            fragment = detailsFragment!!,
            tag = detailsFragment!!.fragmentTag(),
            addToBackStack = false
        )
    }

    private fun showEmptyFragment() {
        Timber.tag(TAG).d("showEmptyFragment: ")
        val emptyFragment = EmptyFragment.newInstance()
        replaceOrShowFragment(
            container = R.id.fl_details,
            fragment = emptyFragment,
            tag = emptyFragment.fragmentTag(),
            addToBackStack = false)
    }

    private fun initListFragment() {
        Timber.tag(TAG).d("initListFragment: ")

        if (listFragment == null) {
            // Create fragment only in case if is not created yet
            Timber.tag(TAG).d("create new listFragment")
            listFragment = MoviesListFragment.newInstance()
        }
        listFragment!!.setMovieClickListener { onMovieClicked(it) }

        replaceOrShowFragment(
            container = R.id.fl_content,
            fragment = listFragment!!,
            tag = listFragment!!.fragmentTag(),
            addToBackStack = false
        )
    }

    private fun listenForInternetConnectionChanges() {
        model.internetConnection.observe(this, Observer {
            onInternetConnectionStateChanged(it)
        })
    }

    private fun observeViewModel() {
        if(isTablet() && isLandscape()) {
            model.movie.observe(this, Observer { onMovieClicked(it) })
        }
    }

    fun onFirstMovieLoaded(movie: Movie) {
        if(isTablet() && isLandscape()) {
            onMovieClicked(movie)
        }
    }

    private fun onMovieClicked(movie: Movie) {
        Timber.tag(TAG).d("onMovieClicked")

        selectedMovieLiveData.postValue(movie)

        when {
            isTablet() -> {
                when {
                    isLandscape() -> showDetailsFragmentInTabletLandMode()
                    else -> showDetailsFragmentInTabletPortMode()
                }
            }
            else -> showDetailsFragmentInDefaultMode()
        }
    }

    private fun setDetailsLayoutVisibility(visibility: Int) {
        fl_details?.visibility = visibility
    }

    private fun setupListToolbar() {
        toolbar.navigationIcon = null
        toolbar.title = getString(R.string.pop_movies)
        toolbar.setNavigationOnClickListener(null)
    }

    private fun setupDetailsToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back2)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = getString(R.string.movie_details)
    }

    override fun onBackPressed() {
        Timber.tag(TAG).d("onBackPressed: ")
        if(isTablet() && isLandscape()) {
            finish()
        } else {
            when {
                supportFragmentManager.fragments.size > 1 -> {
                    super.onBackPressed()
                    removeFragment(MoviesDetailsFragment.TAG)
                    setupListToolbar()
                }
                supportFragmentManager.fragments.size == 1 -> {
                    super.onBackPressed()
                    initListFragment()
                    setupListToolbar()
                }
                else -> {
                    super.onBackPressed()
                }
            }
        }
    }

    companion object {

        const val TAG = "MoviesActivity"
    }
}
