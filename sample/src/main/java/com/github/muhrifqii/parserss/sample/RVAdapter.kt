package com.github.muhrifqii.parserss.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.muhrifqii.parserss.RSSItem

class RVAdapter(var items: List<RSSItem>) : RecyclerView.Adapter<RVHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_rss, null)
        return RVHolder((view))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.title.text = items[position].title
        holder.subtitle.text = items[position].description
    }

}

class RVHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.titleTV)
    val subtitle: TextView = view.findViewById(R.id.subtitleTV)
}