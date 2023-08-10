package com.rri.lsvplugin.utils

import com.squareup.moshi.JsonClass

class SvConstants {
    companion object {
        const val ELEMENTS = "element"
        const val FILTERS = "filters"
        const val ATTRIBUTES = "attribute"
        const val VISIBILITY_FILTERS = "visibility"
        const val SORTING_FILTERS = "sorts"
        const val PROPERTIES = "properties"
        const val LISTS = "lists"
        const val KEYWORDS = "keywords"
        const val ICONS = "icons"
    }

    @JsonClass(generateAdapter = true)
    enum class IconType {
        Base, Mark, Offset
    }
}