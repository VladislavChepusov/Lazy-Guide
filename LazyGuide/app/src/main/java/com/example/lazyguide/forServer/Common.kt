package com.example.lazyguide.forServer
//Дляподключения к вебсерверу
object Common {
    private val BASE_URL = "http://192.168.76.108:3000/"  //ip дом
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}