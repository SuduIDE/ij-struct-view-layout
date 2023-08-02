package com.rri.lsvplugin.utils

import com.squareup.moshi.JsonClass

class JsonStructureSV {
    @JsonClass(generateAdapter = true)
    data class ElementInfo(
        val displayLevel: Int,
        val baseToken: String,
        val attributes: Map<String, List<String>>,
        val baseIcon: String,
        val text: List<String>,
        val description: List<String>
    ) {
        companion object {
            @Suppress("UNCHECKED_CAST")
            fun fromJson(map: Map<String, Any>) = object {
                private val displayLevel by map
                private val baseToken by map
                private val attributes by map
                private val baseIcon by map
                private val text by map
                private val description by map

                val elementInfo = ElementInfo(
                    (displayLevel as Double).toInt(),
                    baseToken as String,
                    attributes as Map<String, List<String>>,
                    baseIcon as String,
                    text as List<String>,
                    description as List<String>
                )
            }.elementInfo
        }
    }

    @JsonClass(generateAdapter = true)
    data class VisibilityFilterInfo(
        val elementType: List<String>?,
        val notElementType: List<String>?,
        val attributeKey: String?,
        val attributeValue: List<String>?,
        val notAttributeValue: List<String>?,
        val icon: String?
    ) {
        companion object {
            @Suppress("UNCHECKED_CAST")
            fun fromJson(map: Map<String, Any>)  = object {
                private val defaultMap = map.withDefault { null }
                private val elementType by defaultMap
                private val notElementType by defaultMap
                private val attributeKey by defaultMap
                private val attributeValue by defaultMap
                private val notAttributeValue by defaultMap
                private val icon by defaultMap

                val visibilityFilter = VisibilityFilterInfo(
                    elementType as? List<String>,
                    notElementType as? List<String>,
                    attributeKey as? String,
                    attributeValue as? List<String>,
                    notAttributeValue as? List<String>,
                    icon as? String
                )
            }.visibilityFilter

        }
    }

    @JsonClass(generateAdapter = true)
    data class PropertyInfo(
        val id: String,
        val token: String,
        val regexp: String?,
    ) {
        companion object {
            fun fromJson(map: Map<String, String>) = object {
                private val defaultMap = map.withDefault { null }
                private val id by map
                private val token by map
                private val regexp by defaultMap

                val property = PropertyInfo(id, token, regexp)
            }.property
        }
    }

    @JsonClass(generateAdapter = true)
    data class KeywordInfo(
        val id: String,
        val token: String,
        val sortValue: Any,
    )
}