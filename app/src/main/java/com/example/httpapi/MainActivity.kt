package com.example.httpapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

lateinit var recyle:RecyclerView
private lateinit var adapter: OffreAdapter
lateinit var empty_card:CardView
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var offres:MutableList<offre>
        recyle=findViewById(R.id.recyle);
        recyle.layoutManager=LinearLayoutManager(this);
        empty_card=findViewById(R.id.empty_card);
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            try{
                // val testResponseId = ApiClient.apiService.deleteById(4);
                //val offre=offre(4,"testAddedUpdated","dsii","societe",8,"Pays");
                //val responseAdded=ApiClient.apiService.AddOffre(offre);
               //val responseUpdated=ApiClient.apiService.UpdateOffre(4,offre);
                val response = ApiClient.apiService.getOffres();
                //val response = ApiClient.apiService.getOffreById(4);
                if (response.isSuccessful && response.body() != null) {
                             //Log.i("",response.body().toString())
                             offres=response.body()!!;
                             //Log.i("",offres.toString())
                             adapter= OffreAdapter(offres);
                             recyle.adapter=adapter;
                            if (offres.isEmpty()) {
                                empty_card.visibility = CardView.VISIBLE
                                recyle.visibility = RecyclerView.GONE
                            } else {
                                empty_card.visibility = CardView.GONE
                                recyle.visibility = RecyclerView.VISIBLE
                            }
                }else{
                    Log.e("Error",response.message())
                }
            } catch (e: Exception) {
                Log.e("Error",e.message.toString())
            }
        }
    }
}