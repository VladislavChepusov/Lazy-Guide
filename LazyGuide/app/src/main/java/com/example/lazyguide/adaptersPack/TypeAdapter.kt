package com.example.lazyguide.adaptersPack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyguide.Activity.RegionSight
import com.example.lazyguide.R

class TypeAdapter (regionActivity: RegionSight, region_list: ArrayList<String>) : RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    var context: Context = regionActivity
    var TypeList: ArrayList<String> = region_list

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rowName1 : Button = itemView.findViewById(R.id.typebtnRv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.element_type_rv,parent,false)
        val viewHolder : ViewHolder = ViewHolder(view)
        return viewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.rowName1.text = TypeList[position]

    }

    //Количество значений
    override fun getItemCount(): Int {
        return TypeList.size
    }


}