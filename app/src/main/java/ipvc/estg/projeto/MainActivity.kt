package ipvc.estg.projeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.adapters.TitleAdapter
import com.example.projeto.entities.Title
import com.example.projeto.viewModel.TitleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.nio.file.Files.delete

class MainActivity : AppCompatActivity() {

    private lateinit var titleViewModel: TitleViewModel
    private val newWordActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = TitleAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        titleViewModel = ViewModelProvider(this).get(TitleViewModel::class.java)
        titleViewModel.allTitles.observe(this, Observer { titles ->
            // Update the cached copy of the words in the adapter.
            titles?.let { adapter.setTitles(it) }

/////////////////////////////////////////////////////////////////////////////////////////////////////////
            val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                        override fun onMove(    // se quiser mexer de sitio
                                recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false            // Não quero!
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            adapter.getTitlesAt(viewHolder.getAdapterPosition())?.let {         // vai buscar a posicao ao adapter recyclerline
                                titleViewModel.delete(                  // remove da bd
                                        it
                                )
                            }
                        }
                    }


            // attaching the touch helper to recycler view
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        })
        //VIEW MODEL
///////////////////////////////////////////////////////////////////////////////////////////////////
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTitle::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val ptitle = data?.getStringExtra(AddTitle.EXTRA_REPLY_TITLE)
            val pnote = data?.getStringExtra(AddTitle.EXTRA_REPLY_NOTES)
            val pdate = data?.getStringExtra(AddTitle.EXTRA_REPLY_DATE)


            if (ptitle != null && pnote != null && pdate != null) {
                val note = Title(title = ptitle, notes = pnote, date = pdate)
                titleViewModel.insert(note)
            }
        }
        else {
            Toast.makeText(
                    applicationContext,
                    "Titulo vazio!",
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

}