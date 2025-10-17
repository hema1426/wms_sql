package com.winapp.KHDelivery.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoriesCopy : Serializable {
    @SerializedName("item")
    @Expose
    var item: ArrayList<Item>? = null

    class Item {
        @SerializedName("category_id")
        @Expose
        var categoryId: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("parent_id")
        @Expose
        var parentId: String? = null

        @SerializedName("top")
        @Expose
        var top: String? = null

        @SerializedName("column")
        @Expose
        var column: String? = null

        @SerializedName("sort_order")
        @Expose
        var sortOrder: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("date_added")
        @Expose
        var dateAdded: String? = null

        @SerializedName("date_modified")
        @Expose
        var dateModified: String? = null

        @SerializedName("secondary_image")
        @Expose
        var secondaryImage: String? = null

        @SerializedName("alternative_image")
        @Expose
        var alternativeImage: String? = null

        @SerializedName("is_featured")
        @Expose
        var isFeatured: String? = null

        @SerializedName("language_id")
        @Expose
        var languageId: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("meta_title")
        @Expose
        var metaTitle: String? = null

        @SerializedName("meta_description")
        @Expose
        var metaDescription: String? = null

        @SerializedName("meta_keyword")
        @Expose
        var metaKeyword: String? = null
    }
}
