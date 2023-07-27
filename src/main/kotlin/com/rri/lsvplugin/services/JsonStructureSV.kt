package com.rri.lsvplugin.services

import com.squareup.moshi.JsonClass

class JsonStructureSV {
        @JsonClass(generateAdapter = true)
        data class ElementInfo(
            val displayLevel : Int,
            val baseToken: String,
            val attributes: Map<String, List<String>>,
            val baseIcon: String,
            val text: List<String>,
            val description: List<String>
        ) {
            companion object {
                fun fromJsonToElementInfo(map: Map<String, Any>) = object {
                    private val displayLevel by map
                    private val baseToken by map
                    private val attributes by map
                    private val baseIcon by map
                    private val text by map
                    private val description by map

                    val elementInfo = ElementInfo(
                        (displayLevel as Double).toInt(),
                        baseToken as String,
                        @Suppress("UNCHECKED_CAST")
                        attributes as Map<String, List<String>>,
                        baseIcon as String,
                        @Suppress("UNCHECKED_CAST")
                        text as List<String>,
                        @Suppress("UNCHECKED_CAST")
                        description as List<String>
                    )
                }.elementInfo
            }
        }

    data class VisibilityFilterInfo(
        val elementType: List<String>?,
        val notElementType: List<String>?,
        val attributeKey : String?,
        val attributeValue: String?,
        val icon: String?
    )
}