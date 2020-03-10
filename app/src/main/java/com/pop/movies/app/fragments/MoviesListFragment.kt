package com.pop.movies.app.fragments

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pop.movies.app.R
import com.pop.movies.app.activities.MoviesActivity
import com.pop.movies.app.adapters.MoviesAdapter
import com.pop.movies.app.base.AbstractBaseFragment
import com.pop.movies.app.ext.hide
import com.pop.movies.app.ext.isVisible
import com.pop.movies.app.models.Movie
import com.pop.movies.app.viewmodels.MoviesListViewModel
import kotlinx.android.synthetic.main.fragment_movies_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MoviesListFragment : AbstractBaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun fragmentTag() = TAG

    override val contentViewId: Int = R.layout.fragment_movies_list

    private val model: MoviesListViewModel by viewModel()

    private var clickListener: ((movie: Movie) -> Unit)? = null

    private var moviesAdapter: MoviesAdapter? = null

    override fun initViews() {
        Timber.tag(TAG).d("initViews: ")
        setupAdapter()
        observeViewModel()
        setupRefreshListener()
    }

    private fun setupAdapter() {
        Timber.tag(TAG).d("setupAdapter: ")
        moviesAdapter = MoviesAdapter()
        moviesAdapter?.clickEvent?.observe(viewLifecycleOwner,
            Observer { pair -> onMovieClicked(pair) })
        rv_movies?.adapter = moviesAdapter
    }

    private fun observeViewModel() {
        Timber.tag(TAG).d("observeViewModel: ")
        model.movies.observe(viewLifecycleOwner, Observer {
            showMoviesList(it)
        })
        model.firstMovieEvent.observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? MoviesActivity)?.onFirstMovieLoaded(it)
            }
        })
        model.error.observe(viewLifecycleOwner, Observer { handleError(it) })
    }

    private fun showMoviesList(movies: PagedList<Movie>) {
        Timber.tag(TAG).d("showMoviesList: ${movies.size}")
        dismissRefresh()
        dismissLoader()
        moviesAdapter?.submitList(movies)
    }

    private fun setupRefreshListener() {
        Timber.tag(TAG).d("setupRefreshListener: ")
        srl_movies.setOnRefreshListener(this)
    }

    private fun dismissRefresh() {
        Timber.tag(TAG).d("dismissRefresh: ")
        if(srl_movies.isRefreshing) srl_movies.isRefreshing = false
    }

    private fun dismissLoader() {
        Timber.tag(TAG).d("dismissLoader: ")
        if(loader.isVisible()) loader.hide()
    }

    override fun onRefresh() {
        Timber.tag(TAG).d("onRefresh: ")
        model.reloadData()
    }

    private fun onMovieClicked(movie: Movie) {
        Timber.tag(TAG).d("onMovieClicked: $movie")
        clickListener?.invoke(movie)
    }

    fun setMovieClickListener(movieClickListener: (movie: Movie) -> Unit) {
        Timber.tag(TAG).d("setMovieClickListener: ")
        clickListener = movieClickListener
    }

    companion object {

        const val TAG = "MoviesListFr"

        fun newInstance() = MoviesListFragment()
    }
}