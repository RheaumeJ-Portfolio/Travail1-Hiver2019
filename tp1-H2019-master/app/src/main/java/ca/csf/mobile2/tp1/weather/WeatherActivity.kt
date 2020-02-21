package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import ca.csf.mobile2.tp1.R
import android.view.View
import android.support.annotation.UiThread
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

const val WEATHER_VALUE = "WEATHER_VALUE"
const val IS_WEATHER_DOWNLOADED_VALUE="IS_WEATHER_DOWNLOADED_VALUE"

class WeatherActivity : AppCompatActivity() {

    private lateinit var progressBar : ProgressBar
    private lateinit var imageError:ImageView
    private lateinit var imageWeather:ImageView
    private lateinit var temperatureText:TextView
    private lateinit var errorText:TextView
    private lateinit var cityText:TextView
    private lateinit var retryButton:Button

    private lateinit var weather:Weather
    private var isWeatherDownloaded:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createView()
        isWeatherDownloaded=false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(outState!=null){
            outState.putParcelable(WEATHER_VALUE, weather)
            outState.putBoolean(IS_WEATHER_DOWNLOADED_VALUE,isWeatherDownloaded)
        }
    }

    override fun onResume() {
        super.onResume()
        setProgressBarInvisible()
        hideAppComponenent()
        checkWeatherDownloaded()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState!=null){
            weather = savedInstanceState.getParcelable(WEATHER_VALUE)!!
            isWeatherDownloaded=savedInstanceState.getBoolean(IS_WEATHER_DOWNLOADED_VALUE)
        }
    }

    fun createView(){
        setContentView(R.layout.activity_weather)
        progressBar=findViewById(R.id.progressBar)
        retryButton=findViewById(R.id.retryButton)
        imageError=findViewById(R.id.errorImageView)
        cityText=findViewById(R.id.cityTextView)
        errorText=findViewById(R.id.errorTextView)
        imageWeather=findViewById(R.id.weatherPreviewImageView)
        temperatureText=findViewById(R.id.temperatureTextView)
    }

    private fun setProgressBarInvisible() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setProgressBarVisible() {
        progressBar.visibility = View.VISIBLE
    }

    private fun startGetWeatherTask(){
        val weatherTask = GetWeatherTask(this::onSuccess,
        this::onConnectivityError,
        this::onServerError)
        setProgressBarVisible()
        weatherTask.execute()
        setProgressBarInvisible()
    }

    @UiThread
    protected fun onSuccess(weather:Weather){
        this.weather=weather
        temperatureText.text=weather.temperature.toString()+"°C"
        cityText.text=weather.city
        checkWeatherType(weather.weatherType)
        setWeatherVisible()
        isWeatherDownloaded=true
    }

    @UiThread
    protected fun onConnectivityError(){
        errorText.visibility=View.VISIBLE
        errorText.text="erreur de connexion"
        isWeatherDownloaded=false
    }

    @UiThread
    protected fun onServerError(){
        errorText.visibility=View.VISIBLE
        errorText.text="erreur du serveur"
        isWeatherDownloaded=false
    }

    fun checkWeatherType(type: WeatherType){
        if(type==WeatherType.SUNNY){
            imageWeather.setImageResource(R.drawable.ic_sunny)
        }
        else if(type==WeatherType.CLOUDLY){
            imageWeather.setImageResource(R.drawable.ic_cloudy)
        }
        else if(type==WeatherType.PARTLY){
            imageWeather.setImageResource(R.drawable.ic_partly_sunny)
        }
        else if(type==WeatherType.RAIN){
            imageWeather.setImageResource(R.drawable.ic_rain)
        }
        else if(type==WeatherType.SNOW){
            imageWeather.setImageResource(R.drawable.ic_snow)
        }
        else{
            onServerError()
        }
    }
     fun retryConnection(args:View){
        hideAppComponenent()

        //code pour tester l'affichage des données sans utiliser le
        //serveur en mettant en commentaire l'appel à startGetWeatherTask
        /*weather=Weather(WeatherType.SNOW,25,"rome")
        onSuccess(weather)*/

        startGetWeatherTask()
    }
    private fun hideAppComponenent(){
        imageError.visibility=View.INVISIBLE
        cityText.visibility=View.INVISIBLE
        errorText.visibility=View.INVISIBLE
        imageWeather.visibility=View.INVISIBLE
        temperatureText.visibility=View.INVISIBLE
    }
    private fun checkWeatherDownloaded(){
        if(isWeatherDownloaded){
            temperatureText.text=weather.temperature.toString()+"°C"
            cityText.text=weather.city
            checkWeatherType(weather.weatherType)
            setWeatherVisible()
        }
        else{
            startGetWeatherTask()
        }
    }
    private fun setWeatherVisible(){
        temperatureText.visibility=View.VISIBLE
        cityText.visibility=View.VISIBLE
        imageWeather.visibility=View.VISIBLE
    }
}