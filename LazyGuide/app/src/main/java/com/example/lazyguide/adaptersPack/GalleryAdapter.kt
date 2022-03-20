package com.example.lazyguide.adaptersPack

/*
Заполнение галереи
 */

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lazyguide.DataClass.Gallary_class
import com.example.lazyguide.DataClass.sight_class

class GalleryAdapter (fragment: FragmentActivity,gallarylist:MutableList<Gallary_class>) : FragmentStateAdapter(fragment) {

    var programNameList: MutableList<Gallary_class> = gallarylist


    //ок
    override fun getItemCount(): Int {
        return programNameList.size
    }
//хз
   //override fun createFragment(position: Int): Fragment {

   //    val fragment = NumberFragment(programNameList)
   //    fragment.arguments = Bundle().apply {
   //        putInt(ARG_OBJECT, position + 1)
   //    }

   //    return fragment
   //}

   override fun createFragment(position: Int): Fragment = NumberFragment(programNameList).apply {
       arguments = bundleOf(
           "programNameList" to programNameList[position].image_photo.data,
           "position" to position
       )
   }
}