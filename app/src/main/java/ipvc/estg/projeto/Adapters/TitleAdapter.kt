package ipvc.estg.projeto.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.R
import kotlinx.android.synthetic.main.recyclerline.view.*


class TitleAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {

    private var titles = emptyList<Title>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TextView1 : TextView = itemView.title
        val TextView2 : TextView = itemView.notes
        val TextView3 : TextView = itemView.date

    }

  
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)


        return TitleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val current = titles[position]

        holder.TextView1.text =current.title
        holder.TextView2.text=current.notes
        holder.TextView3.text = current.date

    }

    internal fun setTitles(titles: List<Title>) {
        this.titles = titles
        notifyDataSetChanged()
    }

    fun getTitlesAt(position: Int): Title? {
        return titles[position]
    }
    override fun getItemCount() = titles.size


}