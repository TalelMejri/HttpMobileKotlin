package com.example.httpapi

data class offre (
    val code : Int? = null,
    val intitulé :String? = null,
    val specialité :String? = null,
    val sociéte:String? = null,
    val nbposte: Int? = null,
    val pays:String? = null,
    var isSelected:Boolean=false
);