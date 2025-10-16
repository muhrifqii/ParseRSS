package com.github.muhrifqii.parserss.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.muhrifqii.parserss.fuel.responseRss

class MainActivity : AppCompatActivity() {

    var adapter = RVAdapter(listOf())
    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }
    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        loadFeedUsingFuel()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadFeedUsingFuel() {
        Fuel.get("https://www.aljazeera.com/xml/rss/all.xml")
            .responseRss { result ->
                result.fold({
                    adapter.items = it.items
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }, {
                    print(it)
                })
            }
    }
}
