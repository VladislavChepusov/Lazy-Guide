package com.example.lazyguide.adaptersPack
/*
Адаптер для заполнения регионов RecyclerView
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyguide.Activity.MainActivity
import com.example.lazyguide.R


public class regionAdapter(mainActivity: MainActivity, region_list: ArrayList<String>) : RecyclerView.Adapter<regionAdapter.ViewHolder>() {

     var context:Context = mainActivity
     var programNameList: ArrayList<String> = region_list

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rowName1 :TextView = itemView.findViewById(R.id.textRegionrv)
        //var rowName2 :Button = itemView.findViewById(R.id.btnRegionrv)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.activity_region_rv,parent,false)
        val viewHolder : ViewHolder = ViewHolder(view)
        return viewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.rowNamerowName.setText(programNameList[position])
        holder.rowName1.setText(programNameList[position])
        //holder.rowName2.setTag(position);
       // holder.rowName2.setText(programNameList[position])
       // holder.rowName2.setTag(position);
    }

    //Количество значений
    override fun getItemCount(): Int {
       return programNameList.size
    }

    //Перезалив данных на основе фильтрации
    fun filterList(filteredList: ArrayList<String>) {
        programNameList = filteredList
        notifyDataSetChanged()
    }

//inner class regionViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)

}