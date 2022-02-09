package com.example.oompaloompahr.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class OompaLoompa(
    @Json(name = "first_name")
    val first_name: String,
    @Json(name = "last_name")
    val last_name: String,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "profession")
    val profession: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "age")
    val age: Int,
    @Json(name = "country")
    val country: String,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name="favorite")
    val favorite: Map<String, String> = mutableMapOf()

) : Parcelable