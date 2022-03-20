package com.example.lazyguide.forServer

import com.example.lazyguide.DataClass.Gallary_class
import com.example.lazyguide.DataClass.Sight_result
import com.example.lazyguide.DataClass.sight_class
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("getRegionSight")
    fun getRegionSightList(
        @Query("test")  test:Int
    ): Call<MutableList<sight_class>>
    @GET("getSight")
    fun getSightList(
        @Query("test")  test:Int
    ): Call<MutableList<Gallary_class>>

}