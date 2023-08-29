package com.rri.lsvplugin.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.squareup.moshi.JsonClass
import javax.swing.Icon

class JsonStructureSV {

    @JsonClass(generateAdapter = true)
    data class DefaultAttributes (
        val parent : Map<String, List<String>>?,
        val children : Map<String, List<String>>?
    ) {
        companion object {
            fun fromJson(map: Map<String, Map<String, List<String>>>?) = object {
                private val parent = map?.get("parent")
                private val children = map?.get("children")

                val defaultAttribute = DefaultAttributes(parent, children)
            }.defaultAttribute
        }
    }

    @JsonClass(generateAdapter = true)
    data class ElementInfo(
        val elementId: String,
        val displayLevel: Int?,
        val displayOnlyLevel: Int?,
        val baseToken: String,
        val attributes: Map<String, List<String>>,
        val baseIcon: IconProperties,
        val text: List<String>,
        val description: List<String>,
        val defaultAttributes : DefaultAttributes?
    ) {
        companion object {
            @Suppress("UNCHECKED_CAST")
            fun fromJson(elementId: String, map: Map<String, Any>) = object {
                private val defaultMap = map.withDefault { null }
                private val displayLevel by defaultMap
                private val displayOnlyLevel by defaultMap
                private val baseToken by map
                private val attributes by map
                private val baseIcon by map
                private val text by map
                private val description by map
                private val defaultAttributes by defaultMap

                val elementInfo = ElementInfo(
                    elementId,
                    (displayLevel as? Double)?.toInt(),
                    (displayOnlyLevel as? Double)?.toInt(),
                    baseToken as String,
                    attributes as Map<String, List<String>>,
                    IconProperties.fromJson(baseIcon as Map<String, Any>),
                    text as List<String>,
                    description as List<String>,
                    DefaultAttributes.fromJson(defaultAttributes as? Map<String, Map<String, List<String>>>?)
                )
            }.elementInfo
        }
    }

    @Suppress("UNCHECKED_CAST")
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
        var regexp: String?,
        val notMatchedDisplayLevel: Int?
    ) {
        fun isNotPartialMatch(langElement: PsiElement) : Boolean {
            try {
                if (regexp != null && !regexMap.containsKey(regexp))
                    regexMap[regexp!!] = Regex(regexp!!, RegexOption.IGNORE_CASE)

                return regexp != null && regexMap[regexp]!!.find(langElement.text) == null
            } catch (error: Exception) {
                regexp = null
                ErrorNotification.notifyError(langElement.project, "Regular expression is incorrectly specified, regexp search disabled ")
            }
            return false
        }
        companion object {
            val regexMap : MutableMap<String, Regex> = mutableMapOf()
            fun fromJson(map: Map<String, Any>) = object {
                private val defaultMap = map.withDefault { null }
                private val id by map
                private val token by map
                private val regexp by defaultMap
                private val notMatchedDisplayLevel by defaultMap

                val property = PropertyInfo(id as String, token as String, regexp as? String, (notMatchedDisplayLevel as? Double)?.toInt())
            }.property
        }
    }

    @JsonClass(generateAdapter = true)
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

        fun loadIcon(project: Project) {
            loadedIcon = IconLoader.getIcon(icon, project)
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
        val iconId: String?
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
                private val iconId by defaultMap

                val visibilityFilter = VisibilityFilterInfo(
                    elementType as? List<String>,
                    notElementType as? List<String>,
                    attributeKey as? String,
                    attributeValue as? List<String>,
                    notAttributeValue as? List<String>,
                    iconId as? String
                )
            }.visibilityFilter

        }
    }

    @JsonClass(generateAdapter = true)
    data class SortingFilterInfo(
        val sortBy: List<String>,
        val defaultValue: Int?,
        val iconId: String
    ) {
        companion object {
            fun fromJson(map: Map<String, Any>) = object {
                val defaultMap = map.withDefault { null }
                private val sortBy by map
                private val defaultValue by defaultMap
                private val iconId by map

                @Suppress("UNCHECKED_CAST")
                val sortingFilterInfo = SortingFilterInfo(sortBy as List<String>, (defaultValue as? Double?
                        
                        )?.toInt(), iconId as String)
            }.sortingFilterInfo
        }
    }
}