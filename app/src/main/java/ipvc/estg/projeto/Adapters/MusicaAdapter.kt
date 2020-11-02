package ipvc.estg.projeto.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.projeto.entities.Musica
import ipvc.estg.projeto.R
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MusicaAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MusicaAdapter.TitleViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var Musicas = emptyList<Musica>() // Cached copy of words

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TextView1 : TextView = itemView.Musica
        val TextView2 : TextView = itemView.Banda

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)


        return TitleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val current = Musicas[position]

        holder.TextView1.text =current.Musica
        holder.TextView2.text=current.Banda


    }

    internal fun setTitles(titles: List<Musica>) {
        this.Musicas = titles
        notifyDataSetChanged()
    }

    override fun getItemCount() = Musicas.size
}
