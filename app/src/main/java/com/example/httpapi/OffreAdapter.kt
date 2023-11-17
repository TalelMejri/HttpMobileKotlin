package com.example.httpapi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OffreAdapter(private val data: MutableList<offre>):RecyclerView.Adapter<OffreAdapter.MyViewHolder>(){

    fun onItemClick(position: Int) {
        this.selectedPosition=position;
        data[selectedPosition].isSelected=selectedPosition==position;
        notifyDataSetChanged()
    }

    private var selectedPosition=RecyclerView.NO_POSITION;

    inner class MyViewHolder(itemview:View):RecyclerView.ViewHolder(itemview),View.OnClickListener{
        val code:TextView=itemview.findViewById(R.id.code);
        val intitulé:TextView=itemview.findViewById(R.id.intut);
        val specialité:TextView=itemview.findViewById(R.id.spec);
        init {
            itemview.setOnClickListener{
                onItemClick(bindingAdapterPosition);
            }
        }
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
        /*val société:TextView=itemview.findViewById(R.id.soc);
        val nbpostes:TextView=itemview.findViewById(R.id.nb)
        val pays:TextView=itemview.findViewById(R.id.pays)*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout=LayoutInflater.from(parent.context).inflate(R.layout.ligne,parent,false);
        return  MyViewHolder(layout);
    }

    override fun getItemCount(): Int {
        return  data.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item=data[position];
        if(position==selectedPosition){
            holder.itemView.setBackgroundColor(Color.RED);
            holder.code.setTextColor(Color.WHITE)
            holder.intitulé.setTextColor(Color.WHITE)
            holder.specialité.setTextColor(Color.WHITE)
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.code.setTextColor(Color.BLACK)
            holder.intitulé.setTextColor(Color.BLACK)
            holder.specialité.setTextColor(Color.BLACK)
        }
        holder.code.setText(item.code.toString())
        //holder.pays.setText(item.pays)
        holder.intitulé.setText(item.intitulé)
        //holder.nbpostes.setText(item.nbposte.toString());
        holder.specialité.setText(item.specialité);
       // holder.société.setText(item.sociéte);
    }

    fun GetSelectedItem():Int{
        return this.selectedPosition;
    }

}