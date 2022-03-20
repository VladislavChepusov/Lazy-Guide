package com.example.lazyguide.forServer

import com.example.lazyguide.DataClass.Gallary_class
import com.example.lazyguide.DataClass.sight_class

interface RevealGallaryCallbacks {
        fun onSuccess(value: MutableList<Gallary_class>)
        fun onError( )

}