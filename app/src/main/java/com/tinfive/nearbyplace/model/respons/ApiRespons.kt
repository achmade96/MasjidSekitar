package com.tinfive.nearbyplace.model.respons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tinfive.nearbyplace.model.MasjidModel

class ApiRespons {
    data class MosqueRespon(
        @SerializedName("current_page")
        @Expose
        val currentPage: Int,
        @SerializedName("data")
        @Expose
        val data: List<MasjidModel>,
        @SerializedName("first_page_url")
        @Expose
        val firstPageUrl: String,
        @SerializedName("from")
        @Expose
        val from: Int,
        @SerializedName("last_page")
        @Expose
        val lastPage: Int,
        @SerializedName("last_page_url")
        @Expose
        val lastPageUrl: String,
        @SerializedName("next_page_url")
        @Expose
        val nextPageUrl: String,
        @SerializedName("path")
        @Expose
        val path: String,
        @SerializedName("per_page")
        @Expose
        val perPage: Int,
        @SerializedName("prev_page_url")
        @Expose
        val prevPageUrl: String,
        @SerializedName("to")
        @Expose
        val to: Int,
        @SerializedName("total")
        @Expose
        val total: Int
    )
}