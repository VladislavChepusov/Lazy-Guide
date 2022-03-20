package com.example.lazyguide.DataClass
/*
Класс чтобы можно было хранить информацию о достопримечательностях
(В разделе список достопримечательностей)
 */
data class sight_class(
    val Id_sight:Int? = null,
    val name_sight: String? = null,
    val description:String? = null,
    val Name_region:String? = null,
    val name_type:String? = null,
    val face_photo: BufferJS
)

