package com.youverify.agent_app_android.features.verification.areas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youverify.agent_app_android.R
import com.youverify.agent_app_android.databinding.CheckboxDropdownBinding

class CheckBoxAdapter(
    private val checkListener: (String) -> Unit,
    private val uncheckListener: (String) -> Unit)
    : ListAdapter<String, CheckBoxViewHolder>(DiffUtilCalback()){

    private val checkList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : CheckboxDropdownBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.checkbox_dropdown, parent, false)
        return CheckBoxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
        holder.bind(checkList[position], checkListener, uncheckListener)
    }

    override fun getItemCount(): Int = checkList.size

    fun setItems(checkItems: List<String>){
        checkList.clear()
        checkList.addAll(checkItems)
    }
}

class CheckBoxViewHolder(val binding: CheckboxDropdownBinding): RecyclerView.ViewHolder(binding.root){
     fun bind(lga: String, checkListener: (String) -> Unit, uncheckListener: (String) -> Unit){
         binding.checkBox.text = lga
         binding.checkBox.id = lga.hashCode()

         binding.checkBox.setOnCheckedChangeListener { _, checked ->
             if(checked){
                 checkListener(lga)
             }else{
                 uncheckListener(lga)
             }
         }
     }
}

class DiffUtilCalback: DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}