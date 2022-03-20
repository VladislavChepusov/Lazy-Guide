package com.example.lazyguide.Activity
/*
Для активити main(они же регионы)
ОТображение списка регонов через RecyclerView
Переход на активти достопримечательности регоинов
Переход на активити добавить дотсопримечаетльность
 */
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyguide.adaptersPack.regionAdapter
import com.example.lazyguide.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    //Для взаимодействия с формой
    private lateinit var MainBinding : ActivityMainBinding
    //Для взаимодействия с RecyclerView и ее хаполнения
    lateinit var recyclerview: RecyclerView
    lateinit var layoutmanager:RecyclerView.LayoutManager
    lateinit var adapter: regionAdapter
    //Список регонов который отправится в RecyclerView
    var region_list =arrayListOf<String>()
    //Список регионов с их индексами(чтобы лишний раз не подключаться к БД)
    val RF_region = mapOf(
        22 to "Алтайский край",
        28 to  "Амурская область",
        29 to "Архангельская область",
        30 to "Астраханская область",
        31 to "Белгородская область",
        32 to "Брянская область",
        33 to "Владимирская область",
        34 to "Волгоградская область",
        35 to "Вологодская область",
        36 to "Воронежская область",
        79 to "Еврейская автономная область",
        75 to "Забайкальский край",
        37 to "Ивановская область",
        38 to "Иркутская область",
        7 to "Кабардино-Балкарская Республика",
        39 to "Калининградская область",
        40 to "Калужская область",
        41 to "Камчатский край",
        9 to "Карачаево-Черкесская Республика",
        42 to "Кемеровская область/Кузбасс",
        43 to "Кировская область",
        44 to "Костромская область",
        23 to "Краснодарский край",
        24 to "Красноярский край",
        45 to "Курганская область",
        46 to "Курская область",
        47 to "Ленинградская область",
        48 to "Липецкая область",
        49 to "Магаданская область",
        50 to "Московская область"
    )
    //Выполняется перед стартом активити, когда активити переходит из состояния INITIALIZED к состоянию CREATED
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(MainBinding.root)
    }
    //По нажатию на регион открывает новое активити с достопримечательностями
    fun onClickGoRegionSight(view:View) {
        val b: TextView = view as TextView
        val TextView: String = b.getText().toString()
        //Мб регулярку
        val key = RF_region.filterValues { it == TextView }.keys.toString().replace("[","").replace("]","")
        val next = Intent(this, RegionSight::class.java)
        next.putExtra(RegionSight.NUMBER,key)
        startActivity(next)
        finish()
    }
    //Переход на форму "добивать достопримечательность"
    fun onClickAdd(view:View) {
        startActivity(Intent(this, AddSight::class.java))
        finish()
    }
    //recyclerview заполняется
    override fun onResume() {
        super.onResume()
        //Список регионов
        region_list = ArrayList<String>(RF_region.values)
        //Заполнение recyclerview
        recyclerview = MainBinding.rvRegionList
        recyclerview.setHasFixedSize(true)
        layoutmanager = LinearLayoutManager(this)
        recyclerview.setLayoutManager(layoutmanager)
        adapter = regionAdapter(this,region_list)
        recyclerview.setAdapter(adapter)
        //Для изменения списка регионов(Поиск по названию)
        val editText:EditText = MainBinding.edittext
        editText.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        } )
    }
    //Функция для поиска региона по названию//Изменяет список отображаемыый recyclerview
    private fun filter(text:String){
        val filterList:ArrayList<String> = arrayListOf<String>()
        for(item:String in  region_list){
            if(item.toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        adapter.filterList(filterList)
    }
}