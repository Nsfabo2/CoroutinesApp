package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

/*
1- fetch data from  https://api.adviceslip.com/advice

2- the data fetching should be done in the background using coroutines when a button is clicked

3- display the advice in a text box
 */

class MainActivity : AppCompatActivity() {

    lateinit var AdvieTV: TextView
    lateinit var GetAdvicebtn: Button
    val Link = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AdvieTV = findViewById(R.id.AdviceTV)
        GetAdvicebtn = findViewById(R.id.GetAdvicebtn)

        GetAdvicebtn.setOnClickListener{
            GetAPI()
        }
    }//end onCreate

    fun GetAPI(){
        CoroutineScope(Dispatchers.IO).launch {
            val data = async {
                GetAdvice()
            }.await()

            if (data.isNotEmpty())
            {
                DiplayAdvice(data)
            }
        }
    }//end GetAPI

    fun GetAdvice(): String {
        var responseText =""
        try {
            responseText = URL(Link).readText(Charsets.UTF_8)
        }catch (e:Exception)
        {
            println("Error $e")
        }
        return responseText
    }//end GetAdvice

    suspend fun DiplayAdvice(data: String) {
        withContext(Dispatchers.Main)
        {
            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            AdvieTV.text = advice
        }
    }//end DisplayAdvice

}//end class