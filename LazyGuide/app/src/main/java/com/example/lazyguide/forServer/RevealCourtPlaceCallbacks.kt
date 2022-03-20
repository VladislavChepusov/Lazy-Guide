package com.example.lazyguide.forServer


import com.example.lazyguide.DataClass.sight_class

interface  RevealCourtPlaceCallbacks {
     fun onSuccess(value: MutableList<sight_class>)
     fun onError( )
}