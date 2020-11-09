package ipvc.estg.projeto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.projeto.Adapters.MusicaAdapter
import ipvc.estg.projeto.entities.Musica
import ipvc.estg.projeto.viewModel.MusicaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recyclerview_item.*
import java.nio.file.Files.delete


class MainActivity : AppCompatActivity() {

    private lateinit var musicaViewModel: MusicaViewModel
    private val newWordActivityRequestCode = 1


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MusicaAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        musicaViewModel = ViewModelProvider(this).get(MusicaViewModel::class.java)
        musicaViewModel.allTitles.observe(this, Observer { titles ->
            titles?.let { adapter.setTitles(it) }



        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    adapter.getMusicaAt(viewHolder.getAdapterPosition())?.let {
                        musicaViewModel.delete(
                            it
                        )
                    }

                }


            }

// attaching the touch helper to recycler view
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddingActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.remover -> {
                Toast.makeText(this, "Para Remover dê Swipe para a esquerda no item que deseja eliminar", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.editar -> {
                Toast.makeText(this, "Para Editar dê Swipe para a direita no item que deseja editar", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
           }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val pMusica = data?.getStringExtra(AddingActivity.EXTRA_REPLY_MUSICA)
            val pBanda = data?.getStringExtra(AddingActivity.EXTRA_REPLY_BANDA)


            if (pMusica != null && pBanda != null) {
                val note = Musica(Musica = pMusica, Banda = pBanda)
                musicaViewModel.insert(note)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Titulo vazio!",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}
