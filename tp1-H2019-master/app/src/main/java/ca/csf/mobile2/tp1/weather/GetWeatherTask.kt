package ca.csf.mobile2.tp1.weather

import android.os.AsyncTask
import okhttp3.Request
import okhttp3.OkHttpClient
import java.io.IOException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class GetWeatherTask(private val onSuccess:(Weather) -> Unit,
                     private val onConnectivityError:() -> Unit,
                     private val onServerError:() -> Unit) : AsyncTask<Void, Void, Weather>(){

    var isServerError:Boolean=false
    var isConnectivityError:Boolean=false
    override fun doInBackground(vararg p0: Void?): Weather? {
        try {
            val request = Request.Builder().url("https://m2t1.csfpwmjv.tk/api/v1/weather").build()
            val response = OkHttpClient().newCall(request).execute()

            if (response.isSuccessful()) {
                val body = response.body().toString()
                val mapper = jacksonObjectMapper()
                return mapper.readValue(body,Weather::class.java)
            }
            //Problème du serveur
            else {
                isServerError=true
            }
        }
        //Problème du Wifi.
        //Note: L'émulateur m'empêche de me connecter au wifi alors cette erreur arrive toujours
        catch (e: IOException) {
            isConnectivityError=true
        }
        return null
    }

    override fun onPostExecute(result : Weather?){
    if(isConnectivityError){
        onConnectivityError()
    }
    else if(isServerError){
        onServerError()
    }
    else if(result!=null){
        onSuccess(result)
    }
    }
}