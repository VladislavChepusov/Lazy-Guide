package com.example.lazyguide.Activity

/*
Для активити добавления достопримечательностей пользователем
Отправляет данные на почту
Работа с галереей
 */
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.lazyguide.R
import com.example.lazyguide.databinding.ActivityAddBinding

import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Error
import java.net.URL
class AddSight: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var AddBinding : ActivityAddBinding
   // private  var  photo1 : Uri? = null
    private  var  photo1:ArrayList<Uri>  = ArrayList<Uri>()
    private  var  photogalary : Uri? = null
    val cea = R.layout.spinner_style1
    val Rf_region = mutableListOf(
        "Выберите регион..",
        "Алтайский край (22)",
          "Амурская область (28)",
          "Архангельская область (29)",
          "Астраханская область (30)",
          "Белгородская область (31)",
          "Брянская область (32)",
          "Владимирская область (33)",
          "Волгоградская область (34)",
           "Вологодская область (35)",
           "Воронежская область (36)",
           "Еврейская автономная область (79)",
           "Забайкальский край (75)",
           "Ивановская область (37)",
           "Иркутская область (38)",
          "Кабардино-Балкарская Республика (7)",
           "Калининградская область (39)",
            "Калужская область (40)",
            "Камчатский край (41)",
            "Карачаево-Черкесская Республика (9)",
            "Кемеровская область/Кузбасс (42)",
            "Кировская область (43)",
            "Костромская область (44)",
            "Краснодарский край (23)",
            "Красноярский край (24)",
            "Курганская область (45)",
            "Курская область (46)",
            "Ленинградская область (47)",
            "Липецкая область (48)",
            "Магаданская область (49)",
            "Московская область (50)"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AddBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(AddBinding.root)
        //Для стелочки
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Свяжитесь с нами"

    }
    //Кнопка газад на action bar,чтобы вернуться в меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        return true
    }
//Загрузка спинера
    override fun onResume() {
        super.onResume()
        //Работа со спинером
        val zspiner = AddBinding.RegionSpinner
   // val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Rf_region)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, cea, Rf_region)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zspiner.setAdapter(adapter)
        zspiner.setOnItemSelectedListener(this);
    }
    ////////////////////////////////////////РАБОТА С ГАЛЕРЕЕЙ /////////////////////////////////////////////////////////////////////
    val Pick_image = 1
    fun onClickImageDowload(view:View){
        //Кол-во пикч
        val PICK_IMAGE = 1
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Выберите фото достопримечательности"), PICK_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Pick_image -> if (resultCode === RESULT_OK  && resultCode == Activity.RESULT_OK && null != data) {
                try {
                    //Получаем URI изображения, преобразуем его в Bitmap
                    //объект и отображаем в элементе ImageView нашего интерфейса:
                    val imageUri: Uri? = data?.getData()
                    val imageStream: InputStream? = imageUri?.let {
                        contentResolver.openInputStream(
                            it
                        )
                    }
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    if (imageUri != null) {
                        photo1.add(imageUri)
                        AddBinding.countFoto.setTextColor(Color.GRAY)
                        AddBinding.countFoto.text = "Загружено фотографий: ${photo1.size}"
                    }
                    AddBinding.imageButton.setImageBitmap(selectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
    //Отправка письма
    fun onClickMail(view:View){
        var MyEmail:String = "lazyguide2021@gmail.com"
        var Tema:String  = "!НОВАЯ ДОСТОПРИМЕЧАТЕЛЬНОСТЬ!"
        var name = (AddBinding.NameText)
        var region = AddBinding.RegionSpinner
        var address = AddBinding.addressText
        var description = AddBinding.discriptinSight
        var phone = AddBinding.phoneNumber
        var web = AddBinding.webText
        var time = AddBinding.timeworkText
        var price = AddBinding.priceText
        //регулярки
        val checkPhoneNumber = """(\+7|8)[\s(]*\d{3}[)\s]*\d{3}[\s-]?\d{2}[\s-]?\d{2}"""
        var pole = mutableListOf(name,address,description,phone,time,price,web)
        var check:Boolean = true
        //Проверка на пустоту
        for (i in pole ){
            if (i.text.toString().isEmpty()) {
                i.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,com.example.lazyguide.R.drawable.ic_baseline_close_24, 0)
                check = false
            }
            else {
                i.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check, 0)
            }
        }
        //Проверка наличия фото
        if (photo1.isEmpty()){
            AddBinding.countFoto.text = "Загружено фотографий: 0!!!"
            AddBinding.countFoto.setTextColor(Color.RED)
            check = false
        }
        val che = findViewById<TextView>(R.id.textspiner)
        //Проверка выбора региона
        if (region.selectedItem.toString() == "Выберите регион.."){
            che.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,com.example.lazyguide.R.drawable.ic_baseline_close_24, 0)
            check = false
            AddBinding.errorRegion.text="Пожалуйста,сделайте выбор"
        }else{
            che.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check, 0)
            AddBinding.errorRegion.text=""
        }
        //Проверка номера
        if (!phone.text.toString().matches(Regex(checkPhoneNumber))){
            phone.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,com.example.lazyguide.R.drawable.ic_baseline_close_24, 0)
            AddBinding.errorPhone.text ="Неверный формат номера телефона"
            check = false
        }else{
            phone.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
            AddBinding.errorPhone.text =""
        }
        //Проверка веб-адреса
        if (Patterns.WEB_URL.matcher(web.text.toString()).matches()){
            web.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
            AddBinding.errorWeb.text =""
        }
        else {
            web.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorWeb.text="Неверный формат веб адреса"
        }
        //Проверка названия
        if (name.text.toString().count() == 0){
            name.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorName.text="Название не может быть пустым"
        }else {
            AddBinding.errorName.text=""
            name.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
        }
        //Описание проверка (сколько символов?)
        if (description.text.toString().count() == 0){
            description.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorDirection.text="Описание не может быть пустым"
        }else {
            AddBinding.errorDirection.text=""
            description.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
        }
        if (price.text.toString().count() == 0){
            price.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorCost.text="Стоимость не может быть пустой"
        }else {
            AddBinding.errorCost.text=""
            price.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
        }
        if (time.text.toString().count() == 0){
            time.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorTime.text="Время посещения не может быть пустым"
        }else {
            AddBinding.errorTime.text=""
            time.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
        }
        if (address.text.toString().count() == 0){
            address.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,  0)
            check = false
            AddBinding.errorAddress.text="Адрес не может быть пустым"
        }else {
            AddBinding.errorAddress.text=""
            address.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.check,  0)
        }
        if ( !check ) {
           // AddBinding.scrollpage.scrollTo(0,0)
            AddBinding.scrollpage.fullScroll(ScrollView.FOCUS_UP);
        }
        else {
            var text: String = "*! ПОЖАЛУЙСТА,НЕ ИЗМЕНЯЙТЕ ПИСЬМО!* \n\n" +
                    "Название достопримечательности: ${name.text.toString()}\n\n" +
                    "Регион: ${region.selectedItem.toString()}\n\n" +
                    "Адрес: ${address.text.toString()} \n\n" +
                    "Описание: ${description.text.toString()}\n\n" +
                    "Контактный номер телефона: ${phone.text.toString()} \n\n" +
                    "Веб-сайт: ${web.text.toString()} \n\n" +
                    "Цена: ${price.text.toString()} \n\n" +
                    "Время работы: ${time.text.toString()} \n\n" +
                    "Фотографии:  \n "
            val email = Intent(Intent.ACTION_SEND_MULTIPLE)
            email.setType("text/email") //???
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(MyEmail))
            email.putExtra(Intent.EXTRA_SUBJECT, Tema)
            email.putExtra(Intent.EXTRA_TEXT, text)
            email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, photo1);
            //для того чтобы запросить email клиент устанавливаем тип
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Сделайте выбор почты:"))
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0) {
            val che = findViewById<TextView>(R.id.textspiner)
            che.setTextColor(Color.BLACK)
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}