/*
Заполнение галереи
 */

package com.example.lazyguide.adaptersPack

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lazyguide.DataClass.Gallary_class
import com.example.lazyguide.R




class NumberFragment(gallarylist:MutableList<Gallary_class>) : Fragment() {

    var programNameList: MutableList<Gallary_class> = gallarylist

    //ок
    override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
    return inflater.inflate(R.layout.gallary_fragment, container, false)
 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let   {
            val image: ImageView = view.findViewById(R.id.onegalaryphoto)
            val photo =  programNameList[it.getInt("position")].image_photo.data
            val image1 = BitmapFactory.decodeByteArray(photo,0, photo.size)
            image.setImageBitmap(image1)
        }
    }

}