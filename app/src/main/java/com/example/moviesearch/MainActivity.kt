package com.example.moviesearch
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    private lateinit var userinput: EditText
    private lateinit var image: ImageView
    private lateinit var name: TextView
    private lateinit var plot: TextView
    private lateinit var search: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appnetwork = BasicNetwork(HurlStack())
        val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        requestQueue = RequestQueue(appcache, appnetwork).apply {
            start()
        }
        userinput = findViewById(R.id.userinput)
        name = findViewById(R.id.name)
        plot = findViewById(R.id.plot)
        image = findViewById(R.id.image)
        search = findViewById(R.id.search)


        search.setOnClickListener {
            var input = userinput.text.toString()
            fetchData(input)
        }


    }

    fun fetchData( input: String){
        val url = "https://www.omdbapi.com/?t=${input}&apikey=d2993595"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if(response.get("Response")=="False"){
                    name.text = "Incorrect detail"
                }else {
                    Glide.with(this).load(response.getString("Poster")).into(image)
                    plot.text = response.getString("Plot")
                    name.text = response.getString("Title")+"\n\n"+"Writer: "+
                            response.getString("Writer")+"\n\n"+"Year:"+response.getString("Year")+
                            "\n\n"+"Genre:"+response.getString("Genre")+
                            "\n\n"+"Actors:"+response.getString("Actors")+
                            "\n\n"+"imdbRating:"+response.getString("imdbRating")

                }
            },
            { error ->
                Log.d("vol",error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}