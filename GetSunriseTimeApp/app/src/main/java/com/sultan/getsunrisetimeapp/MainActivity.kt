package com.sultan.getsunrisetimeapp

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun buGetWatherEvent(view:View){

        val city = etCityName.text.toString()
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

        imageViewTemp.setOnClickListener{
            val intent = Intent (this,WatherDetails::class.java)
            intent.putExtra("city",city)
            //intent.putExtra("url",url)
           // intent.putExtra("temp",tvTemp.text)
            startActivity(intent)
        }
        try {
           MyAsyncTask().execute(url)
       }catch (ex:Exception){
           println(ex.message)
       }

    }

    inner class MyAsyncTask:AsyncTask<String,String,String>(){
        override fun onPreExecute() {
            // befor run in background

        }

        override fun doInBackground(vararg p0: String?): String {
            // can not access to UI
            try {
                val url= URL (p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=700

                val dataJsonAsStr= convertStraemToString(urlConnect.inputStream)
                publishProgress(dataJsonAsStr)

            }catch (ex:Exception){
                println(ex.message)
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            // access UI
            val json= JSONObject(values[0])
              val query= json.getJSONObject("query")
                val results = query.getJSONObject("results")
                    val channel = results.getJSONObject("channel")
                        val astronomy = channel.getJSONObject("astronomy")
                            val sunrise = astronomy.getString("sunrise")
                            val sunset = astronomy.getString("sunset")
                                tvSunriseTime.text = sunrise
                                tvSunsetTime.text = sunset

                        val item = channel.getJSONObject("item")
                            val condition = item.getJSONObject("condition")
                                 val temp = fahrenheiToCelsiusConverter(condition.getString("temp"))

                       val forecastArray= item.getJSONArray("forecast")
                                val obj1=forecastArray.getJSONObject(0)
                                val high1 =fahrenheiToCelsiusConverter( obj1.getString("high"))
                                val low1 = fahrenheiToCelsiusConverter(obj1.getString("low"))

                                val text = condition.getString("text")
                                val DegreeCelsius= " \u2103"

                                    tvTemp.text = temp + DegreeCelsius
                                    tvTempHighLow.text= high1 +DegreeCelsius+ " / " + low1+DegreeCelsius

                                    imageViewSunrise.visibility = View.VISIBLE
                                    imageViewSunset.visibility = View.VISIBLE
                                    imageViewTemp.visibility = View.VISIBLE

        }

        fun fahrenheiToCelsiusConverter(tempF:String):String{
            var temp:Double = ((tempF.toDouble()-32.0)/1.8)
            var tempC:String = "%.0f".format(temp)
            return tempC

        }
        override fun onPostExecute(result: String?) {
            // when done process
        }


    }

    fun convertStraemToString(inputStream:InputStream):String{
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""
        try {
              do {
                line = bufferReader.readLine()
                if (line!=null){
                    allString+=line
                  }
                    }while (line!=null)
                 bufferReader.close()
             }catch (ex:Exception){
            println(ex.message)
            println(allString)
        }
            return allString
    }
}
