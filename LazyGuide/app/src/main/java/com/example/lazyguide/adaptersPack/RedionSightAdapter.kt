package com.example.lazyguide.adaptersPack
/*
Адаптер для заполнения RecyclerView связанного со списком достопримечательностей
 */
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyguide.R
import com.example.lazyguide.Activity.RegionSight
import com.example.lazyguide.DataClass.sight_class
import kotlin.reflect.typeOf
class RedionSightAdapter(regionSight: RegionSight, region_list:MutableList<sight_class>) : RecyclerView.Adapter<RedionSightAdapter.ViewHolder>() {
    var context: Context = regionSight
    var programNameList: MutableList<sight_class> = region_list

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var rowName1 :ImageView = itemView.findViewById(R.id.photoSightrv)//Фото
         var rowName2 : TextView = itemView.findViewById(R.id.sightphotorv) //Имя
         var rowName3 : TextView = itemView.findViewById(R.id.directionsightrv)//описание
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.activity_regionsight_rv,parent,false)
        val viewHolder : ViewHolder = ViewHolder(view)
        return viewHolder
    }
    override fun getItemCount(): Int {

        if (programNameList.size == 0){
            return 1
        }
        return programNameList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (programNameList.size == 0) {
            holder.rowName2.setText("Простите, данных нет!")
            holder.rowName1.setImageResource(R.drawable.ic_baseline_sick_24)
            holder.rowName3.alpha = 0.0f
            holder.rowName2.isClickable =false
            holder.rowName1.isClickable =false
            holder.rowName3.isClickable =false

        }
        else {
            holder.rowName2.text = programNameList[position].name_sight
            val image = BitmapFactory.decodeByteArray(programNameList[position].face_photo.data,0, programNameList[position].face_photo.data.size)
            holder.rowName1.setImageBitmap(image)
            holder.rowName3.setText(programNameList[position].description?.take(80) + "..")
            holder.rowName1.tag = programNameList[position].Id_sight
       }
    }

}