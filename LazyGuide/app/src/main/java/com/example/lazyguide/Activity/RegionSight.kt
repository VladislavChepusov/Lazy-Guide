package com.example.lazyguide.Activity
/*Отвечает за страницу со списком достопримечательностей Используется SQL (подключение\отлючение и выкачкаданных Заполнение MutableList<sight_class> этими данными Отображение на RecyclerView*/
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyguide.DataClass.sight_class
import com.example.lazyguide.R
import com.example.lazyguide.adaptersPack.RedionSightAdapter
import com.example.lazyguide.adaptersPack.TypeAdapter
import com.example.lazyguide.adaptersPack.regionAdapter
import com.example.lazyguide.databinding.ActivityRegionSightBinding
import com.example.lazyguide.forServer.Common
import com.example.lazyguide.forServer.RetrofitServices
import com.example.lazyguide.forServer.RevealCourtPlaceCallbacks
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log
class RegionSight : AppCompatActivity()  {
    //Константа из предыдущего активити(номер региона)
    companion object{
        const val NUMBER = "0"
    }
    private val typelist:ArrayList<String> = arrayListOf(
        "Все",
        "Природная",
        "Историческая",
        "Религиозная",
        "Садово-парковая",
        "Военно-патриотическая",
        "Культурная",
        "Архитектурная",
        "Монументальная",
        "Контркультурная")
    //Для полкючения к серверу
    private lateinit var mService: RetrofitServices
    lateinit var dialog: AlertDialog
    //для RecyclerView
    private lateinit var RegionSight : ActivityRegionSightBinding
    private lateinit var recyclerview: RecyclerView
    private lateinit var layoutmanager: RecyclerView.LayoutManager
    //список достопримечательностей
    private val region_sight: MutableList<sight_class> = mutableListOf()
    private var backe: Button? = null
    //Функция запроса на сервер
    private fun getAllRegionSightList(id_region:Int,callMy:RevealCourtPlaceCallbacks) {
        dialog.show()
        //Вызов функции из regrofitservices
        mService.getRegionSightList(id_region).enqueue(object : Callback<MutableList<sight_class>> {
            override fun onFailure(call: Call<MutableList<sight_class>>, t: Throwable) {
                Log.d("Mylog ","Это печально ")
                callMy.onError()
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<MutableList<sight_class>>, response: Response<MutableList<sight_class>>) {
               if (response.isSuccessful) {
                    val check = response.body() as MutableList<sight_class>
                   callMy.onSuccess(check)
               }
                    dialog.dismiss()
            }
        })
        dialog.dismiss()
    }
    //Кнопка назад на action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //Для формы
        RegionSight = ActivityRegionSightBinding.inflate(layoutInflater)
        setContentView(RegionSight.root)
        //Для стелочки
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val id_region = intent.getStringExtra(NUMBER).toInt()
        mService = Common.retrofitService
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()
        getAllRegionSightList(id_region,  object:RevealCourtPlaceCallbacks{
            override fun onSuccess(value: MutableList<sight_class>){
                region_sight.addAll(value)
            }
            override fun onError(){
            }
        })


    }
    fun OnClickTypeChose(view:View){
        if (backe == null){ }
        else
        { backe!!.setBackgroundResource(R.drawable.oblick) }
        val b: Button = view as Button
        backe = b
         b.setBackgroundResource(R.drawable.buttonadd)
        val test = convert(region_sight,b.text.toString())
        val adapter = RedionSightAdapter(this, test)
        recyclerview.adapter = adapter
    }
    //Функия сортиртивки записей по типу достопримечательности
   fun convert(all_list:MutableList<sight_class>,type:String):MutableList<sight_class> {
       return if (all_list.isEmpty()){
           all_list
       } else {
           val result: MutableList<sight_class> = mutableListOf()
           if (type == "Все") {
               for (i in all_list) {
                   if ((i.name_sight !in (result.map { it.name_sight }))) {
                       result.add(i)
                   }
               }
           }
           else{
               for (i in all_list) {
                   if ((i.name_sight !in (result.map { it.name_sight })) && i.name_type.toString() ==type) {
                       result.add(i)
                   }
               }
           }
           return result
       }
   }
    override fun onResume() {
        super.onResume()
            recyclerview = RegionSight.RegionSightrv
            recyclerview.setHasFixedSize(true)
            layoutmanager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanager
        Handler().postDelayed(
            {
                if (region_sight.isEmpty() ) {
                    title = "Error 404"
                } else {
                    title = region_sight[0].Name_region
                    val recyclerview1:RecyclerView = RegionSight.typerv
                    recyclerview1.setHasFixedSize(true)
                    val layoutmanager1 = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
                    recyclerview1.setLayoutManager(layoutmanager1)
                    val type_adapter = TypeAdapter(this,typelist)
                    recyclerview1.setAdapter(type_adapter)
                }
                val test = convert(region_sight,"Все")
                val adapter = RedionSightAdapter(this, test)
                recyclerview.adapter = adapter
            }, 1800
        )
    }
    //Оотправляет на страницу досторпримечательности
    fun GoSight(view: View){
        val b: ImageView = view as ImageView
        val key = b.tag.toString()
        val next = Intent(this, Sight::class.java)
        next.putExtra(Sight.ID,key)
        startActivity(next)
        finish()
    }
}