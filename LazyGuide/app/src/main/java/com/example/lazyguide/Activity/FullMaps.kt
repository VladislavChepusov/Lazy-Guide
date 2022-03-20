package com.example.lazyguide.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.BLUE
import android.graphics.Color.RED
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lazyguide.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import com.example.lazyguide.databinding.ActivityFullMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class FullMaps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //Константа из предыдущего активити
    companion object{
        const val coordination = "0"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityFullMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    //Начальная точка точка
    private  lateinit var MyPoint:LatLng
    //Конечная точка
    private lateinit var LastPoint:LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Для стелочки назад
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Карта"
        //ФОрма
        binding = ActivityFullMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Координата конечной точки
         val place = intent.getStringExtra(coordination).split(",")
            LastPoint = LatLng(place[0].toDouble(),place[1].toDouble())
        // Инициализцаия карты
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //для определения местоплодожения
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    //Кнопка газад на action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
            return true
    }

    //Приведенный  код проверяет, предоставлено ли приложению разрешение ACCESS_FINE_LOCATION . Если нет, то запросите это у пользователя.
    private fun setUpMap(type:String) {
        MyPoint = LatLng(11.0,11.0)
        mMap.clear()
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                MyPoint = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MyPoint, 16f))
                // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MyPoint, 10f))
            }
            mMap!!.addMarker(MarkerOptions().position(LastPoint).title("Пункт назначения"))
            val path: MutableList<List<LatLng>> = ArrayList()
            //https://maps.googleapis.com/maps/api/directions/json?origin=48.77551,44.8059467&destination=48.726683,44.5375819&key=AIzaSyA8ct-v9jPY0pZDq-cRxsU9uQQbcqsYjk4&mode=Driving
          //  val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=${MyPoint.latitude},${MyPoint.longitude}&destination=${LastPoint.latitude},${LastPoint.longitude}&key=AIzaSyA8ct-v9jPY0pZDq-cRxsU9uQQbcqsYjk4 &mode=Driving"
            val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=${MyPoint.latitude}," +
                    "${MyPoint.longitude}&destination=${LastPoint.latitude},${LastPoint.longitude}" +
                    "&key=AIzaSyA8ct-v9jPY0pZDq-cRxsU9uQQbcqsYjk4&mode=${type}}"
            val cvet:Int
            if (type=="walking"){
                cvet = RED
            }else{
                cvet = BLUE
            }
            Log.d("Mylog",urlDirections.toString())
            val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
                    response ->
                val jsonResponse = JSONObject(response)
                // Get routes
                val routes = jsonResponse.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    mMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(cvet))
                }
            }, Response.ErrorListener {
                    _ ->
            }){}
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(directionsRequest)
        }
    }

    fun car_travelmode(view: View){
        findViewById<ImageButton>(R.id.car_mode).setBackgroundResource(R.drawable.map_button_on)
        findViewById<ImageButton>(R.id.walking_mode).setBackgroundResource(R.drawable.map_button_off)
         setUpMap("driving")
    }
    fun walking_travelmode(view: View){
        findViewById<ImageButton>(R.id.car_mode).setBackgroundResource(R.drawable.map_button_off)
        findViewById<ImageButton>(R.id.walking_mode).setBackgroundResource(R.drawable.map_button_on)
        setUpMap("walking")
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap("driving")
    }
    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.title = "Пункт назначения"
        return false
    }
}