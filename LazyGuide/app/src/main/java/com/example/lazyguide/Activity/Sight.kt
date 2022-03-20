package com.example.lazyguide.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.lazyguide.DataClass.Gallary_class
import com.example.lazyguide.DataClass.Sight_result
import com.example.lazyguide.DataClass.sight_class
import com.example.lazyguide.R
import com.example.lazyguide.adaptersPack.GalleryAdapter
import com.example.lazyguide.databinding.ActivitySightBinding
import com.example.lazyguide.forServer.Common
import com.example.lazyguide.forServer.RetrofitServices
import com.example.lazyguide.forServer.RevealCourtPlaceCallbacks
import com.example.lazyguide.forServer.RevealGallaryCallbacks
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.*


class Sight : AppCompatActivity(), OnMapReadyCallback {
    //Константа из предыдущего активити
    companion object{
        const val ID = "0"
    }
    //Данные о достопримечательности
    private val sight_data: MutableList<Sight_result> = mutableListOf()
    //Для формы
    private lateinit var sigthact:ActivitySightBinding
    //Для подключения к sql
    private val ip = "192.168.76.108"  //Мой ip
    // private val ip = "192.168.43.224" //ip телефона
    private val port = "1433"
    private val Classes = "net.sourceforge.jtds.jdbc.Driver"
    // private val database = "testDatabase"
    private val database = "DiplomDatabase"
    private val username = "test"
    private val password = "test"
    private val url = "jdbc:jtds:sqlserver://$ip:$port/$database"
    private var connection: Connection? = null
    //Для полкючения к серверу
    private lateinit var mService: RetrofitServices
    lateinit var dialog: AlertDialog
    //список галереи
    private val gallary: MutableList<Gallary_class> = mutableListOf()
    //Для карты
    private lateinit var mMap: GoogleMap
    private var geocoder: Geocoder? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mapFragment: SupportMapFragment? = null
    //Для галереи
    private lateinit var adapter: GalleryAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var nameADDRESS:String
    private  lateinit var place:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Активация формы
        sigthact = ActivitySightBinding.inflate(layoutInflater)
        setContentView(sigthact.root)
        //Для стелочки
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //Идентификатор
        val id_sight = intent.getStringExtra(ID).toInt()
        //Для sql
        openSQL()
        getData(id_sight)
        closeSQL()
        mService = Common.retrofitService
        dialog = SpotsDialog.Builder().setCancelable(true).setContext(this).build()
        getAllSightList(id_sight,  object:RevealGallaryCallbacks{
            override fun onSuccess(value: MutableList<Gallary_class>){
                gallary.addAll(value)
            }
            override fun onError(){
            }
        })


    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        geocoder =  Geocoder(this)
        var addresses: List<Address>? = null
        try {
             addresses = geocoder!!.getFromLocationName(nameADDRESS,1)
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                place = LatLng(address.latitude, address.longitude)
                val markerOptions = MarkerOptions()
                    .position(place)
                    .title(nameADDRESS)
                mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 13f))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    //На большую карту
    fun fullmapView(view:View){
        val next = Intent(this, FullMaps::class.java)
        next.putExtra(FullMaps.coordination,place.toString().replace("lat/lng: (","").replace(")",""))
        startActivity(next)
    }

    //Функция запроса изображений
    // onResponse асинхронная
    private fun getAllSightList(id_sight:Int,callMy: RevealGallaryCallbacks) {
        dialog.show()
        //Вызов функции из regrofitservices
        mService.getSightList(id_sight).enqueue(object : Callback<MutableList<Gallary_class>> {
            override fun onFailure(call: Call<MutableList<Gallary_class>>, t: Throwable) {
                Log.d("Mylog ","Это печально ")
                callMy.onError()
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<MutableList<Gallary_class>>, response: Response<MutableList<Gallary_class>>) {
                if (response.isSuccessful) {
                    val check = response.body() as MutableList<Gallary_class>
                    callMy.onSuccess(check)
                }
                dialog.dismiss()
            }
        })
        dialog.dismiss()
    }





    override fun onResume() {
        super.onResume()
        //Заполнение формы
        title = sight_data[0].name_sight
        nameADDRESS = sight_data[0].adress
        sigthact.facePhoto.setImageBitmap(sight_data[0].photo_sight)
        sigthact.directionAll.setText(sight_data[0].dicription)
        sigthact.adress.text = sight_data[0].adress
        sigthact.web.text = sight_data[0].website
        sigthact.phoneTextView.text = sight_data[0].contact
        sigthact.timeTextview.text = sight_data[0].time_work
        sigthact.costTextview.text = sight_data[0].cost
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.addressMap) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        //Для галереи
        Handler().postDelayed(
            {
        adapter = GalleryAdapter(this,gallary)
                viewPager = findViewById(R.id.gallaryPager2)
                viewPager.adapter = adapter
            }, 2000
        )
    }
    //Кнопка назад на action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            val next = Intent(this, RegionSight::class.java)
            next.putExtra(RegionSight.NUMBER,sight_data[0].id_region.toString())
            startActivity(next)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openSQL(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), PackageManager.PERMISSION_GRANTED);
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
        }catch (e: ClassNotFoundException){
            e.printStackTrace();
        }catch (e: SQLException) {
            e.printStackTrace();
        }
    }
    fun closeSQL(){
        connection?.close()
    }
    fun getData(id_sight:Int){
        if (connection != null) {
            var statement: Statement? = null
            try {
                statement = connection!!.createStatement()
               val resultSet: ResultSet = statement.executeQuery("Select * from sight where id_sight=${id_sight};")
                while (resultSet.next()) {
                    val textttt = resultSet.getBinaryStream(10)
                    sight_data.add(
                        Sight_result( resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        BitmapFactory.decodeStream(resultSet.getBinaryStream(10))))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } else {
        }
    }
}