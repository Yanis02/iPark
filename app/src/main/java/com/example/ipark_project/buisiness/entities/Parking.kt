package com.example.ipark_project.buisiness.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Parking (
    val id: String,
    val name:String,
    val address:String,
    val description:String,
    val latitude:String,
    val longitude:String,
    val images:List<Images>,
    val price :String
): Parcelable

