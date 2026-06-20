package br.ufpr.oscarmobile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.ufpr.oscarmobile.R
import br.ufpr.oscarmobile.model.Filme
import com.squareup.picasso.Picasso

class FilmeAdapter(
    private val filmes: List<Filme>,
    private val onClick: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    inner class FilmeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val tvNome:   TextView  = view.findViewById(R.id.tvNome)
        val tvGenero: TextView  = view.findViewById(R.id.tvGenero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filme, parent, false)
        return FilmeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = filmes[position]
        holder.tvNome.text   = filme.nome
        holder.tvGenero.text = filme.genero

        Picasso.get()
            .load(filme.foto)
            .placeholder(R.drawable.oscar_trofeu)   // exibe troféu enquanto carrega
            .error(R.drawable.oscar_trofeu)          // exibe troféu se falhar
            .into(holder.ivPoster)

        holder.itemView.setOnClickListener { onClick(filme) }
    }

    override fun getItemCount() = filmes.size
}