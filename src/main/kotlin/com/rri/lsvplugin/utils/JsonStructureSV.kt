package com.rri.lsvplugin.utils

import com.intellij.psi.PsiElement
import com.squareup.moshi.JsonClass
import javax.swing.Icon

class JsonStructureSV {
    @JsonClass(generateAdapter = true)
    data class ElementInfo(
        val displayLevel: Int,
        val baseToken: String,
        val attributes: Map<String, List<String>>,
        val baseIcon: IconProperties,
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
                    IconProperties.fromJson(baseIcon as Map<String, Any>),
                    text as List<String>,
                    description as List<String>
                )
            }.elementInfo
        }
    }

    @JsonClass(generateAdapter = true)
    data class IconProperties(
            val iconId: String,
            val attributeKey: String?,
            val attributeValue: List<String>?,
            val alternativeIconId: String?
    ) {
        companion object {
            fun fromJson(map: Map<String, Any>) = object {
                private val defaultMap = map.withDefault { null }
                private val iconId by map
                private val attributeKey by defaultMap
                private val attributeValue by defaultMap
                private val alternativeIconId by defaultMap

                val iconProperties = IconProperties(iconId as String, attributeKey as String?, attributeValue as? List<String>?, alternativeIconId as String?)
            }.iconProperties
        }
    }

    @JsonClass(generateAdapter = true)
    data class PropertyInfo(
        val id: String,
        val token: String,
        val regexp: String?,
    ) {

        fun isNotPartialMatch(langElement: PsiElement) : Boolean {
            return regexp != null && Regex(regexp).find(langElement.text) == null
        }
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

    data class IconInfo (
        val id: String,
        val iconType : SvConstants.IconType?,
        val icon: String,
        var loadedIcon : Icon? = null
    ) {
        companion object {
            fun fromJson(map : Map<String, String>) = object {
                private val id by map
                private val iconType by map
                private val icon by map

                val iconInfo = IconInfo(id, SvConstants.IconType.values().firstOrNull { it.name == iconType }, icon)
            }.iconInfo

        }

        fun loadIcon() {
            loadedIcon = IconLoader.getIcon(icon)
        }
    }

    @JsonClass(generateAdapter = true)
    data class KeywordInfo(
        val id: String,
        val token: String,
        val sortValue: Int?,
        val iconId: String?
    ) {
        companion object {
            fun fromJson(map: Map<String, Any>) = object {
                private val defaultMap = map.withDefault { null }
                private val id by map
                private val token by map
                private val sortValue by defaultMap
                private val iconId by defaultMap

                val keywordInfo = KeywordInfo(id as String, token as String, (sortValue as? Double?)?.toInt(), iconId as? String)
            }.keywordInfo
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
    data class SortingFilterInfo(
        val sortBy: List<String>,
        val defaultValue: Int?,
        val icon: String
    ) {
        companion object {
            fun fromJson(map: Map<String, Any>) = object {
                val defaultMap = map.withDefault { null }
                private val sortBy by map
                private val defaultValue by defaultMap
                private val icon by map

                val sortingFilterInfo = SortingFilterInfo(sortBy as List<String>, (defaultValue as? Double?
                        
                        )?.toInt(), icon as String)
            }.sortingFilterInfo
        }
    }
}