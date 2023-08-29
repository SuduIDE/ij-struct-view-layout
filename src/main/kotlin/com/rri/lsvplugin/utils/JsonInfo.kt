package com.rri.lsvplugin.utils

import com.intellij.openapi.project.Project
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.nio.file.Path

class JsonInfo(private val project: Project) {

    private var languagesSV: MapTypeSV? = null
    val filenameJsonSV: File = File(".customSV.json")
    private val defaultJsonSv: Path = Path.of("defaultCustomSV/.defaultJavaCustomSV.json")
    private var iconsSV: MutableMap<String, Map<String, JsonStructureSV.IconInfo>> = mutableMapOf()

    fun reset() {
        val jsonSV = javaClass.classLoader?.getResourceAsStream(defaultJsonSv.toString())?.reader()?.readText()
        if (jsonSV != null) {
            setMapSV(convertJsonToMapSV(jsonSV))
        }
    }

    fun getDefaultVersionJson(): String? =
        javaClass.classLoader?.getResourceAsStream(defaultJsonSv.toString())?.reader()?.readText()

    fun setMapSV(newLanguagesSV: MapTypeSV?): Boolean {
        if (newLanguagesSV != null) {
            languagesSV = languageToLowerCase(newLanguagesSV)
            loadIconsForLanguages()
            return true
        } else
            languagesSV = null
        return false
    }

    fun getMapSV(): MapTypeSV? = languagesSV

    fun getJsonSV(): String {
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java).indent("    ")
            .toJson(languagesSV)
    }


    fun loadAndConvertJson(fullPathToJsonSV: File?): Boolean {
        val updatedJsonSV = fullPathToJsonSV?.readText()?.let {
            convertJsonToMapSV(it)
        }
        return setMapSV(updatedJsonSV)
    }

    private fun loadIconsForLanguages() {
        for (languageStructure in languagesSV!!.values) {
            @Suppress("UNCHECKED_CAST")
            val iconsMap =
                languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as Map<String, JsonStructureSV.IconInfo>
            iconsMap.values.forEach { it.loadIcon(project) }
        }
    }

    private fun convertJsonToMapSV(jsonSV: String): MapTypeSV? {
        try {
            @Suppress("UNCHECKED_CAST")
            val tmpUpdatedJsonSV = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
                .fromJson(jsonSV) as List<LanguageStructureType>
            val languagesSV = mutableMapOf<String, MutableMap<String, MutableMap<String, out Any>>>()
            for (languageStructure in tmpUpdatedJsonSV) {
                val newElements = mutableMapOf<String, MutableList<JsonStructureSV.ElementInfo>>()
                languageStructure[SvConstants.ELEMENTS]?.forEach { element ->
                    @Suppress("UNCHECKED_CAST")
                    val elementStructure = JsonStructureSV.ElementInfo.fromJson(element.key, element.value as Map<String, Any>)
                    if (!newElements.containsKey(elementStructure.baseToken))
                        newElements[elementStructure.baseToken] = mutableListOf(elementStructure)
                    else
                        newElements[elementStructure.baseToken]!!.add(elementStructure)
                }
                languageStructure[SvConstants.ELEMENTS] = newElements

                val properties = mutableMapOf<String, MutableList<JsonStructureSV.PropertyInfo>>()
                (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.PROPERTIES) as? List<*>)?.forEach { property ->
                    @Suppress("UNCHECKED_CAST")
                    val propertyStructure = JsonStructureSV.PropertyInfo.fromJson(property as Map<String, String>)
                    if (properties.containsKey(propertyStructure.token))
                        properties[propertyStructure.token]!!.add(propertyStructure)
                    else
                        properties[propertyStructure.token] = mutableListOf(propertyStructure)

                }

                val keywords = mutableMapOf<String, JsonStructureSV.KeywordInfo>()
                (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) as? List<*>)?.forEach { keyword ->
                    @Suppress("UNCHECKED_CAST")
                    val keywordStructure = JsonStructureSV.KeywordInfo.fromJson(keyword as Map<String, String>)
                    if (!keywords.containsKey(keywordStructure.token))
                        keywords[keywordStructure.token] = keywordStructure
                }

                val icons = mutableMapOf<String, JsonStructureSV.IconInfo>()
                (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as? List<*>)?.forEach { icon ->
                    @Suppress("UNCHECKED_CAST")
                    val iconStructure = JsonStructureSV.IconInfo.fromJson(icon as Map<String, String>)
                    icons[iconStructure.id] = iconStructure
                }

                languageStructure[SvConstants.ATTRIBUTES] = mutableMapOf(
                    SvConstants.LISTS to (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS)
                        ?: mutableMapOf<String, String>()),
                    SvConstants.PROPERTIES to properties,
                    SvConstants.KEYWORDS to keywords,
                    SvConstants.ICONS to icons
                )

                @Suppress("UNCHECKED_CAST")
                val visibilityFiltersMap =
                    (languageStructure[SvConstants.FILTERS]?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, Map<String, Any>>)
                val visibilityFilters = mutableMapOf<String, JsonStructureSV.VisibilityFilterInfo>()
                visibilityFiltersMap?.entries?.forEach { visibilityFilter ->
                    visibilityFilters[visibilityFilter.key] =
                        JsonStructureSV.VisibilityFilterInfo.fromJson(visibilityFilter.value)
                }

                @Suppress("UNCHECKED_CAST")
                val sortingFilterMap =
                    (languageStructure[SvConstants.FILTERS]?.get(SvConstants.SORTING_FILTERS) as? Map<String, Map<String, Any>>)
                val sortingFilters = mutableMapOf<String, JsonStructureSV.SortingFilterInfo>()
                sortingFilterMap?.entries?.forEach { sortingFilter ->
                    sortingFilters[sortingFilter.key] =
                        JsonStructureSV.SortingFilterInfo.fromJson(sortingFilter.value)
                }

                languageStructure[SvConstants.FILTERS] = mutableMapOf(
                    SvConstants.VISIBILITY_FILTERS to visibilityFilters,
                    SvConstants.SORTING_FILTERS to sortingFilters
                )

                //set the language structure for each language in list
                for (language in languageStructure[SvConstants.SETTINGS]?.get(SvConstants.LANGUAGES) as List<String>) {
                    languagesSV[language] = languageStructure
                }
            }
            return languagesSV
        } catch (error: Exception) {
            ErrorNotification.notifyError(project, "Incorrect config format")
        }
        return null
    }

    private fun languageToLowerCase(languagesSV: MapTypeSV): MapTypeSV {
        return languagesSV.mapKeys { it.key.lowercase() }
    }
}

typealias LanguageStructureType = MutableMap<String, MutableMap<String, out Any>>
typealias MapTypeSV = Map<String, LanguageStructureType>