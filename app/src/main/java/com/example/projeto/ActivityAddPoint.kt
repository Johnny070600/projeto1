package com.example.projeto

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.projeto.api.EndPoints
import com.example.projeto.api.OutputPost
import com.example.projeto.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_add_point.*
import retrofit2.Call
import retrofit2.Response

class ActivityAddPoint : AppCompatActivity() {
    private var userid : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point)

        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        userid = token.getInt("id_login_atual", 0)

        var latitude = intent.getStringExtra("latitude")
        var longitude = intent.getStringExtra("longitude")

        findViewById<TextView>(R.id.latitude).setText(latitude)
        findViewById<TextView>(R.id.longitude).setText(longitude)
        findViewById<TextView>(R.id.user_id).setText(userid.toString())



    }

    fun add(view: View) {
        val descr = descr.text.toString().trim()
        val latitude = latitude.text.toString().trim()
        val longitude = longitude.text.toString().trim()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postAddPoint(descr,latitude,longitude,userid)

        call.enqueue(object : retrofit2.Callback<OutputPost> {

            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                if (response.isSuccessful) {
                    val intent = Intent(this@ActivityAddPoint, MapsActivity::class.java)
                    Toast.makeText(this@ActivityAddPoint, "Novo point inserido com sucesso", Toast.LENGTH_SHORT).show()
                    intent.putExtra("userid",userid)
                    startActivity(intent)

                }
            }
            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@ActivityAddPoint, "Erro na inserção", Toast.LENGTH_SHORT).show()
            }
        })
    }

}