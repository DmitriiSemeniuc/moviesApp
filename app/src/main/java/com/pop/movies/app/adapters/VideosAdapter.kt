package com.pop.movies.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pop.movies.app.R
import com.pop.movies.app.models.Video
import kotlinx.android.synthetic.main.item_video.view.*

class VideosAdapter(
    val clickEvent: MutableLiveData<Video> = MutableLiveData()
) : ListAdapter<Video, VideosAdapter.VideoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video: Video? = getItem(position)
        video?.let {
            holder.bindTo(video)
            holder.itemView.cl_container?.setOnClickListener { clickEvent.postValue(video) }
        }
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(video: Video) {
            itemView.tv_video_name?.text = video.name
            itemView.tv_video_type?.text = video.type
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {

            override fun areItemsTheSame(old: Video, new: Video) = old.id == new.id

            override fun areContentsTheSame(old: Video, new: Video) = old == new
        }
    }
}