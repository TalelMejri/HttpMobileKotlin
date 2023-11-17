package com.example.httpapi

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

lateinit var recyle:RecyclerView
private lateinit var adapter: OffreAdapter
lateinit var empty_card:CardView
var offres:MutableList<offre>? = null
lateinit var btnDelete:FloatingActionButton
lateinit var EditBtn:FloatingActionButton
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyle=findViewById(R.id.recyle);
        recyle.layoutManager=LinearLayoutManager(this);
        empty_card=findViewById(R.id.empty_card);
        btnDelete=findViewById(R.id.delete);
        EditBtn=findViewById(R.id.Update)
        this.fetchData();
    }


    fun fetchData(){
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            try{
                val response = ApiClient.apiService.getOffres();
                if (response.isSuccessful && response.body() != null) {
                    Log.i("",response.body().toString())
                    offres=response.body()!!;
                    adapter= OffreAdapter(offres!!);
                    recyle.adapter=adapter;
                    if (offres!!.isEmpty()) {
                        empty_card.visibility = CardView.VISIBLE
                        recyle.visibility = RecyclerView.GONE
                        btnDelete.isEnabled = false
                        EditBtn.isEnabled = false
                    } else {
                        empty_card.visibility = CardView.GONE
                        recyle.visibility = RecyclerView.VISIBLE
                        btnDelete.isEnabled = true
                        EditBtn.isEnabled = true
                    }
                }else{
                    Log.e("Error",response.message())
                    btnDelete.isEnabled = false
                    EditBtn.isEnabled = false
                }
            } catch (e: Exception) {
                Log.e("Error",e.message.toString())
                btnDelete.isEnabled = false
                EditBtn.isEnabled = false
            }
        }
    }
    fun AddOffre(v:View){
        val dialogA=AlertDialog.Builder(this)
        var dialogView=layoutInflater.inflate(R.layout.formulaire,null);
        dialogA.setView(dialogView)
        dialogA.setPositiveButton("Added") { _, _ ->
            val intitulé = dialogView.findViewById<EditText>(R.id.intitulé)
            val specialité = dialogView.findViewById<EditText>(R.id.specialité)
            val Sociéte = dialogView.findViewById<EditText>(R.id.Sociéte)
            val nbposte = dialogView.findViewById<EditText>(R.id.nbposte)
            val pays = dialogView.findViewById<EditText>(R.id.pays)
            val code=dialogView.findViewById<EditText>(R.id.Code)
            val offre=offre(code.text.toString().toInt(),intitulé.text.toString(),specialité.text.toString(),
                Sociéte.text.toString(),nbposte.text.toString().toInt(),pays.text.toString());
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                try{
                    val response = ApiClient.apiService.getOffreById(code.text.toString().toInt());
                    if (response.isSuccessful && response.body() != null) {
                        Snackbar.make(
                            v,code.text.toString()+" Already Exist",Snackbar.LENGTH_LONG
                        ).setAction("close", View.OnClickListener {  })
                            .setBackgroundTint(Color.RED)
                            .show();
                    }else{
                        ApiClient.apiService.AddOffre(offre);
                        offres?.add(offre)
                        adapter.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    //Log.e("Error", e.message.toString())
                    Snackbar.make(
                        v,e.message.toString(),Snackbar.LENGTH_LONG
                    ).setAction("close", View.OnClickListener {  })
                        .setBackgroundTint(Color.RED)
                        .show();
                }
            }
            this.fetchData()
            Snackbar.make(
                v,"Added With Success",Snackbar.LENGTH_LONG
            ).setAction("close", View.OnClickListener {  }).show();
        }
        dialogA.setNegativeButton("No",{dialogin:DialogInterface,i:Int->});
        dialogA.show()
    }


    fun Update(v:View){
        val position= adapter.GetSelectedItem();
        if(position==RecyclerView.NO_POSITION){
            Toast.makeText(this,"Offre Not Selected ",Toast.LENGTH_LONG).show();
        }else{
            val dialogA = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.formulaire, null)
            dialogA.setView(dialogView);
            var intut=dialogView.findViewById<EditText>(R.id.intitulé);
            intut.setText(offres?.get(position)?.intitulé);
            var spec=dialogView.findViewById<EditText>(R.id.specialité);
            spec.setText(offres?.get(position)?.specialité)
            var Sociéte=dialogView.findViewById<EditText>(R.id.Sociéte);
            Sociéte.setText((offres?.get(position)?.sociéte))
            var pays=dialogView.findViewById<EditText>(R.id.pays);
            pays.setText(offres?.get(position)?.pays)
            var nbposte=dialogView.findViewById<EditText>(R.id.nbposte);
            nbposte.setText(offres?.get(position)?.nbposte.toString())
            var Code=dialogView.findViewById<EditText>(R.id.Code);
            Code.setText(offres?.get(position)?.code.toString())
            dialogA.setPositiveButton("Updated"){alert,Wich->
                val offre=offre(Code.text.toString().toInt(),intut.text.toString(),spec.text.toString(),
                    Sociéte.text.toString(),nbposte.text.toString().toInt(),pays.text.toString());
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                        ApiClient.apiService.UpdateOffre(Code.text.toString().toInt(),offre);
                        offres?.set(position,offre);
                        adapter.notifyItemChanged(position);
                       adapter.notifyDataSetChanged()
                }
                Snackbar.make(
                    v,"Update With Success",Snackbar.LENGTH_LONG
                ).setAction("close", View.OnClickListener {  }).show();
              //  this.fetchData()
            }
            dialogA.setNegativeButton("No"){alert,Wich->alert.cancel()}
            dialogA.show();
        }
    }

    fun Delete(v:View){
        val position= adapter.GetSelectedItem();
        if(position==RecyclerView.NO_POSITION){
            Toast.makeText(this,"Offre Not Selected ",Toast.LENGTH_LONG).show();
        }else{
        var alert=AlertDialog.Builder(this);
        alert.setTitle("Confirm")
        alert.setMessage("Do You Wanna Delete ${offres?.get(position)?.code}");
        alert.setPositiveButton("Ok"){alert,Wich->
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                offres?.get(position)?.code?.let { ApiClient.apiService.deleteById(it) };
                offres?.removeAt(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged()
            }
            this.fetchData();
            Toast.makeText(this,"Deleted With Success",Toast.LENGTH_LONG).show();
        }
        alert.setNegativeButton("No"){alert,Wich->alert.cancel()}
        alert.show();

        }
    }

}