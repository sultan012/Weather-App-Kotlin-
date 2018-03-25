package com.sultan.getsunrisetimeapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_wather_details.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WatherDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wather_details)
        val bundle = intent.extras
        val city = bundle.getString("city")
       // val url = bundle.getString("url")
        //val temp = bundle.getString("temp")
       // val city = etCityName.text.toString()
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22$city%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"


        try {
            MyAsyncTask().execute(url)
        }catch (ex:Exception){
            println(ex.message)
        }
    }




    inner class MyAsyncTask: AsyncTask<String, String, String>(){
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
            val degreeCelsius= " \u2103"

            // access UI
            val json= JSONObject(values[0])
            val query= json.getJSONObject("query")
            val results = query.getJSONObject("results")
            val channel = results.getJSONObject("channel")
            val astronomy = channel.getJSONObject("astronomy")
            val sunrise = astronomy.getString("sunrise")
            val sunset = astronomy.getString("sunset")
            val location = channel.getJSONObject("location")
            val city = location.getString("city")
          //  val region = location.getString("region")
            val country = location.getString("country")
            tvLocation.text= "$city, $country"

            val item = channel.getJSONObject("item")
            val condition = item.getJSONObject("condition")
            val temp = fahrenheiToCelsiusConverter(condition.getString("temp"))
            tvTempDetails.text=temp+degreeCelsius

            val forecastArray= item.getJSONArray("forecast")
            val obj1=forecastArray.getJSONObject(0)
            val day1 = obj1.getString("day")
            val date1 = obj1.getString("date")
            val high1 = obj1.getString("high")
            val low1 = obj1.getString("low")
            val text1 = obj1.getString("text")

            tvDay1.text = day1
            tvDate1.text=date1
            tvHigh1.text= fahrenheiToCelsiusConverter(high1)+degreeCelsius
            tvLow1.text=fahrenheiToCelsiusConverter(low1)+degreeCelsius
            tvText1.text=text1

            val obj2=forecastArray.getJSONObject(1)
            val day2 = obj2.getString("day")
            val date2 = obj2.getString("date")
            val high2 = obj2.getString("high")
            val low2 = obj2.getString("low")
            val text2 = obj2.getString("text")
            tvDay2.text = day2
            tvDate2.text=date2
            tvHigh2.text= fahrenheiToCelsiusConverter(high2)+degreeCelsius
            tvLow2.text=fahrenheiToCelsiusConverter(low2)+degreeCelsius
            tvText2.text=text2

            val obj3=forecastArray.getJSONObject(2)
            val day3 = obj3.getString("day")
            val date3 = obj3.getString("date")
            val high3 = obj3.getString("high")
            val low3 = obj3.getString("low")
            val text3 = obj3.getString("text")
            tvDay3.text = day3
            tvDate3.text=date3
            tvHigh3.text= fahrenheiToCelsiusConverter(high3)+degreeCelsius
            tvLow3.text=fahrenheiToCelsiusConverter(low3)+degreeCelsius
            tvText3.text=text3

            val obj4=forecastArray.getJSONObject(3)
            val day4 = obj4.getString("day")
            val date4 = obj4.getString("date")
            val high4 = obj4.getString("high")
            val low4 = obj4.getString("low")
            val text4 = obj4.getString("text")
            tvDay4.text = day4
            tvDate4.text=date4
            tvHigh4.text= fahrenheiToCelsiusConverter(high4)+degreeCelsius
            tvLow4.text=fahrenheiToCelsiusConverter(low4)+degreeCelsius
            tvText4.text=text4


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

    fun convertStraemToString(inputStream: InputStream):String{
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
        }catch (ex:Exception){}
        return allString
    }

}
