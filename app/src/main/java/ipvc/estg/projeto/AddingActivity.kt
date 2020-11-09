package ipvc.estg.projeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddingActivity : AppCompatActivity() {
    private lateinit var text1: EditText
    private lateinit var text2: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding)
        text1=findViewById(R.id.nome)
        text2=findViewById(R.id.descricao)
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(text1.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = text1.text.toString()
                val notes = text2.text.toString()


                replyIntent.putExtra(EXTRA_REPLY_MUSICA, title.toString())
                replyIntent.putExtra(EXTRA_REPLY_BANDA, notes.toString())



                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY_MUSICA= "ipvc.estg.projeto.musica"
        const val EXTRA_REPLY_BANDA = "ipvc.estg.projeto.banda"


    }
}