package com.pop.movies.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pop.movies.app.R
import com.pop.movies.app.api.ApiClient
import com.pop.movies.app.ext.delayedAction
import com.pop.movies.app.models.Movie
import com.pop.movies.app.utils.GlideHelper
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter(
    val clickEvent: MutableLiveData<Movie> = MutableLiveData()
) : PagedListAdapter<Movie, MoviesAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie? = getItem(position)
        holder.bindTo(movie)
        movie?.let { m ->
            holder.getClickableView()?.setOnClickListener {
                clickEvent.postValue(m)
            }
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(movie: Movie?) {
            setLoaderVisibility(itemView.iv_thumbnail?.drawable == null)
            setThumbnail(movie?.posterPath)
        }

        private fun setLoaderVisibility(visible: Boolean) {
            itemView.loader?.visibility = if (visible) View.VISIBLE else View.GONE
        }

        private fun setThumbnail(imagePath: String?) {
            imagePath?.let {
                val url = ApiClient.THUMBNAIL_URL + it
                GlideHelper.urlToImage(
                    imageView = itemView.iv_thumbnail,
                    imageUrl = url,
                    placeHolder = R.drawable.ic_popcorn,
                    onLoadFailed = { setLoaderVisibility(visible = false) },
                    onResourceReady = {
                        delayedAction(1_400) {
                            setLoaderVisibility(visible = false)
                        }
                    })
            } ?: run {
                itemView.iv_thumbnail?.setImageDrawable(null)
            }
        }

        fun getClickableView(): View? = itemView.cl_content
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id

            override fun areContentsTheSame(old: Movie, new: Movie) = old == new
        }
    }
}