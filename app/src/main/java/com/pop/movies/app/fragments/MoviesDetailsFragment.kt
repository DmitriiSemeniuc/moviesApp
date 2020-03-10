package com.pop.movies.app.fragments

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.pop.movies.app.R
import com.pop.movies.app.adapters.VideosAdapter
import com.pop.movies.app.base.AbstractBaseFragment
import com.pop.movies.app.ext.hide
import com.pop.movies.app.ext.toDate
import com.pop.movies.app.ext.toYear
import com.pop.movies.app.models.Movie
import com.pop.movies.app.models.Video
import com.pop.movies.app.utils.GlideHelper
import com.pop.movies.app.viewmodels.MoviesDetailsViewModel
import kotlinx.android.synthetic.main.layout_movie_details.*
import kotlinx.android.synthetic.main.layout_movie_header.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MoviesDetailsFragment : AbstractBaseFragment() {

    private val model: MoviesDetailsViewModel by viewModel()

    override fun fragmentTag() = TAG

    override val contentViewId: Int = R.layout.fragment_movies_details

    private var videosAdapter: VideosAdapter? = null

    override fun initViews() {
        Timber.tag(TAG).d("initViews")
        setupAdapter()
        observeViewModel()
    }

    private fun observeViewModel() {
        Timber.tag(TAG).d("observeViewModel")
        model.movie.observe(viewLifecycleOwner, Observer { it?.let { showMovie(it) } })
        model.videos.observe(viewLifecycleOwner, Observer { showVideosList(it) })
    }

    private fun setupAdapter() {
        Timber.tag(TAG).d("setupAdapter: ")
        videosAdapter = VideosAdapter()
        videosAdapter?.clickEvent?.observe(viewLifecycleOwner,
            Observer { pair -> onVideoClicked(pair) })
        rv_movies_videos?.adapter = videosAdapter
    }

    private fun onVideoClicked(video: Video) {
        Timber.tag(TAG).d("onVideoClicked: $video")
        activity?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideLink()))
            if (intent.resolveActivity(it.packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    private fun showMovie(movie: Movie) {
        Timber.tag(TAG).d("showMovie: $movie")
        setTitle(movie.title)
        setBackDropImage(movie.getBackDropPath())
        setPosterImage(movie.getFullPosterPath())
        setOverview(movie.overview)
        setReleaseDate(movie.releaseDate)
        setRating(movie.voteAverage)
    }

    private fun showVideosList(videos: List<Video>?) {
        Timber.tag(TAG).d("showVideosList: ${videos?.size}")
        if(videos.isNullOrEmpty()) {
              hideVideoList()
        } else {
            showVideoList(videos)
        }
    }

    private fun setTitle(title: String?) {
        tv_title?.text = title
    }

    private fun setBackDropImage(path: String?) {
        GlideHelper.urlToImage(iv_backdrop, path)
    }

    private fun setPosterImage(path: String?) {
        GlideHelper.urlToImage(iv_logo, path, R.drawable.ic_popcorn)
    }

    private fun setOverview(text : String?) {
        tv_overview?.text = text
    }

    private fun setReleaseDate(date: String?) {
        tv_year?.text = date?.toDate()?.toYear()
    }

    private fun setRating(voteAverage: String?) {
        val rating = "${voteAverage}/10"
        tv_rating?.text = rating
    }

    private fun hideVideoList() {
        videosAdapter?.submitList(null)
        loader?.hide()
    }

    private fun showVideoList(videos : List<Video>) {
        loader?.hide()
        videosAdapter?.submitList(videos)
    }

    companion object {

        const val TAG = "MoviesDetailsFr"
        fun newInstance() = MoviesDetailsFragment()
    }
}