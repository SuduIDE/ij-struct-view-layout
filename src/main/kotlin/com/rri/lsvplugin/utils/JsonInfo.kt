package com.rri.lsvplugin.utils

import com.intellij.openapi.project.Project
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.nio.file.Path

class JsonInfo(private val project: Project){

    private var languagesSV: MapTypeSV? = null
    val filenameJsonSV: File = File(".customSV.json")
    private val defaultJsonSv: Path = Path.of("defaultCustomSV/.defaultJavaCustomSV.json")
    private var iconsSV : MutableMap<String, Map<String, JsonStructureSV.IconInfo>> = mutableMapOf()

    fun reset() {
        val jsonSV = javaClass.classLoader?.getResourceAsStream(defaultJsonSv.toString())?.reader()?.readText()
        if (jsonSV != null) {
           setMapSV(convertJsonToMapSV(jsonSV))
        }
    }

    fun getDefaultVersionJson() : String? = javaClass.classLoader?.getResourceAsStream(defaultJsonSv.toString())?.reader()?.readText()

    fun setMapSV(newLanguagesSV: MapTypeSV?) {
        if (newLanguagesSV != null) {
            languagesSV = languageToLowerCase(newLanguagesSV)
            loadIconsForLanguages()
        }
    }

    fun getMapSV(): MapTypeSV? = languagesSV

    fun getJsonSV(): String {
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java).indent("    ")
            .toJson(languagesSV)
    }


    fun loadAndConvertJson(fullPathToJsonSV: File?) {
        val updatedJsonSV = fullPathToJsonSV?.readText()?.let {
            convertJsonToMapSV(it)
        }
        setMapSV(updatedJsonSV)
    }

    private fun loadIconsForLanguages() {
        for (languageStructure in languagesSV!!) {
            @Suppress("UNCHECKED_CAST")
            val iconsList = languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as List<JsonStructureSV.IconInfo>
            iconsList.forEach { it.loadIcon(project) }

            val languageNameList = languageStructure[SvConstants.SETTINGS]?.get(SvConstants.LANGUAGES) as? List<String>
            languageNameList?.forEach { languageName -> iconsSV[languageName] = iconsList.associateBy { it.id } }
        }
    }

    private fun convertJsonToMapSV(jsonSV: String) : MapTypeSV {
        @Suppress("UNCHECKED_CAST")
        val tmpUpdatedJsonSV = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
            .fromJson(jsonSV) as MapTypeSV

        for (languageStructure in tmpUpdatedJsonSV) {
            val newElements = mutableMapOf<String, JsonStructureSV.ElementInfo>()
            languageStructure[SvConstants.ELEMENTS]?.forEach { element ->
                @Suppress("UNCHECKED_CAST")
                newElements[element.key] = JsonStructureSV.ElementInfo.fromJson(element.value as Map<String, Any>)
            }
            languageStructure[SvConstants.ELEMENTS] = newElements

            val properties = mutableListOf<JsonStructureSV.PropertyInfo>()
            (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.PROPERTIES) as? List<*>)?.forEach {
                    property ->
                @Suppress("UNCHECKED_CAST")
                properties.add(JsonStructureSV.PropertyInfo.fromJson(property as Map<String, String>))
            }

            val keywords = mutableListOf<JsonStructureSV.KeywordInfo>()
            (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) as? List<*>)?.forEach {
                    keyword ->
                @Suppress("UNCHECKED_CAST")
                keywords.add(JsonStructureSV.KeywordInfo.fromJson(keyword as Map<String, String>))
            }

            val icons = mutableListOf<JsonStructureSV.IconInfo>()
            (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as? List<*>)?.forEach {
                    icon ->
                @Suppress("UNCHECKED_CAST")
                icons.add(JsonStructureSV.IconInfo.fromJson(icon as Map<String, String>))
            }

            languageStructure[SvConstants.ATTRIBUTES] = mutableMapOf(
                SvConstants.LISTS to (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS) ?: mutableMapOf<String, String>()),
                SvConstants.PROPERTIES to properties,
                SvConstants.KEYWORDS to keywords,
                SvConstants.ICONS to icons
            )

            @Suppress("UNCHECKED_CAST")
            val visibilityFiltersMap = (languageStructure[SvConstants.FILTERS]?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, Map<String, Any>>)
            val visibilityFilters = mutableMapOf<String, JsonStructureSV.VisibilityFilterInfo>()
            visibilityFiltersMap?.entries?.forEach { visibilityFilter ->
                visibilityFilters[visibilityFilter.key] =
                    JsonStructureSV.VisibilityFilterInfo.fromJson(visibilityFilter.value)
            }

            @Suppress("UNCHECKED_CAST")
            val sortingFilterMap = (languageStructure[SvConstants.FILTERS]?.get(SvConstants.SORTING_FILTERS) as? Map<String, Map<String, Any>>)
            val sortingFilters = mutableMapOf<String, JsonStructureSV.SortingFilterInfo>()
            sortingFilterMap?.entries?.forEach { sortingFilter ->
                sortingFilters[sortingFilter.key] =
                    JsonStructureSV.SortingFilterInfo.fromJson(sortingFilter.value)
            }

            languageStructure[SvConstants.FILTERS] = mutableMapOf(
                SvConstants.VISIBILITY_FILTERS to visibilityFilters,
                SvConstants.SORTING_FILTERS to sortingFilters
            )
        }
        return tmpUpdatedJsonSV
    }

    private fun languageToLowerCase(languagesSV : MapTypeSV) : MapTypeSV {
        var languagesList : List<String>
        for (languageSV in languagesSV) {
            languagesList = (languageSV[SvConstants.SETTINGS]!![SvConstants.LANGUAGES] as? MutableList<String>)?.map { it.lowercase() } ?: mutableListOf()
            languageSV[SvConstants.SETTINGS] = mutableMapOf("showFile" to languageSV[SvConstants.SETTINGS]!!["showFile"] as Boolean, SvConstants.LANGUAGES to languagesList)
        }
        return languagesSV
    }
}

typealias MapTypeSV = MutableList<MutableMap<String, MutableMap<String, out Any>>>