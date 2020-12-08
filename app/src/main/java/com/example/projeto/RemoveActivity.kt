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
import kotlinx.android.synthetic.main.activity_remove.*
import retrofit2.Call
import retrofit2.Response

class RemoveActivity : AppCompatActivity() {
    private var userid : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove)



    }

    fun remove(view: View) {
        userid = remover.text.toString().toInt()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.deleteProblema(userid)

        call.enqueue(object : retrofit2.Callback<OutputPost> {

            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {

                if (response.isSuccessful) {
                    val intent = Intent(this@RemoveActivity, MapsActivity::class.java)
                    Toast.makeText(this@RemoveActivity, "Point removido com sucesso", Toast.LENGTH_SHORT).show()
                    intent.putExtra("userid",userid)
                    startActivity(intent)

                }
            }
            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@RemoveActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}