package com.example.oompaloompahr.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "current")
    val current: Int,
    @Json(name = "total")
    val total: Int,
    @Json(name = "results")
    val results: List<OompaLoompa>

) : Parcelable