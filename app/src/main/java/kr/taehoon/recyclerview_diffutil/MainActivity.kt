package kr.taehoon.recyclerview_diffutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.taehoon.recyclerview_diffutil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
            }

        recyclerView = binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = DiffUtilRecyclerAdapter()
        }

        val sampleList = mutableListOf<User>().apply {
            for (i in 1..20) {
                add(User(i, "Name $i"))
            }
        }
        (recyclerView.adapter as DiffUtilRecyclerAdapter).updateDataSet(sampleList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = recyclerView.adapter as DiffUtilRecyclerAdapter
        when (item.itemId) {
            R.id.add -> {
                adapter.addDataSet()
                return true
            }
            R.id.remove -> {
                adapter.removeDataSet()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}