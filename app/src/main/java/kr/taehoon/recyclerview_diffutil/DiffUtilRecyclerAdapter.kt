package kr.taehoon.recyclerview_diffutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.taehoon.recyclerview_diffutil.databinding.ItemMainBinding

class DiffUtilRecyclerAdapter : RecyclerView.Adapter<DiffUtilRecyclerAdapter.MainViewHolder>() {

    private val dataSet = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return LayoutInflater.from(parent.context).let {
            DataBindingUtil.inflate<ItemMainBinding>(it, R.layout.item_main, parent, false)
        }.run {
            (parent.context as? LifecycleOwner)?.let {
                lifecycleOwner = it
            }
            MainViewHolder(this)
        }
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    fun updateDataSet(users: List<User>) {
        val newList = mutableListOf<User>().apply {
            addAll(users)
        }
        setNewDataSet(newList)
    }

    /**
     * data 추가
     * notifyItemInserted()()
     */
    fun addDataSet() {
        val newList = mutableListOf<User>().apply {
            addAll(dataSet)
        }
        val id = if (dataSet.isEmpty()) {
            1
        } else {
            dataSet.last().id + 1
        }
        newList.add(User(id, "Name $id"))
        setNewDataSet(newList)
    }

    /**
     * 마지막 인덱스 삭제
     * notifyItemRemoved()
     */
    fun removeDataSet() {
        val newList = mutableListOf<User>().apply {
            addAll(dataSet)
        }
        newList.removeAt(newList.lastIndex)
        setNewDataSet(newList)
    }

    /**
     * 모든 데이터 변경
     * notifyDataSetChanged()
     */
    private fun setNewDataSet(newList: List<User>) {
        MainDiffUtil(dataSet, newList).let {
            DiffUtil.calculateDiff(it)
        }.dispatchUpdatesTo(this)

        dataSet.run {
            clear()
            addAll(newList)
        }
    }

    inner class MainViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
            binding.executePendingBindings()
        }
    }

    inner class MainDiffUtil(private val oldItems: List<User>, private val newItems: List<User>) :
        DiffUtil.Callback() {
        /**
         * 같은 postion의 객체를 비교
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }
        /**
         * primary key 값을 비교하여 두 항목이 같은지 비교한다.
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }
    }
}