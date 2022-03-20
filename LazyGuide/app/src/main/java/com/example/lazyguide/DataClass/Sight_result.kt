package com.example.lazyguide.DataClass
import android.graphics.Bitmap
/*Класс чтобы можно было хранить информацию о достопримечательности*/
data class Sight_result(
    val id_sight: Int,
    val id_region: Int,
    val name_sight: String,
    val adress:String,
    val dicription:String,
    val cost:String,
    val website:String,
    val contact:String,
    val time_work:String,
    val photo_sight: Bitmap

)
