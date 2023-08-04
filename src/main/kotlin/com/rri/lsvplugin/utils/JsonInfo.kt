package com.rri.lsvplugin.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.nio.file.Path

class JsonInfo {

    private var mapSV: MapTypeSV? = null
    val filenameJsonSV: File = File(".customSV.json")
    private val defaultJsonSv: Path = Path.of("defaultCustomSV/.defaultJavaCustomSV.json")
    private var iconsSV : MutableMap<String, Map<String, JsonStructureSV.IconInfo>> = mutableMapOf()

    fun reset() {
        val jsonSV = javaClass.classLoader?.getResourceAsStream(defaultJsonSv.toString())?.reader()?.readText()
        if (jsonSV != null) {
           setMapSV(convertJsonToMapSV(jsonSV))
        }
    }

    fun setMapSV(newMapSV: MapTypeSV?) {
        if (newMapSV != null) {
            mapSV = languageToLowerCase(newMapSV)
            loadIconsForLanguages()
        }
    }

    fun getMapSV(): MapTypeSV? = mapSV

    fun getJsonSV(): String {
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java).indent("    ")
            .toJson(mapSV)
    }


    fun loadAndConvertJson(fullPathToJsonSV: File?) {
        val updatedJsonSV = fullPathToJsonSV?.readText()?.let {
            convertJsonToMapSV(it)
        }
        setMapSV(updatedJsonSV)
    }

    private fun loadIconsForLanguages() {
        for ((languageName, languageStructure) in mapSV!!.entries) {
            @Suppress("UNCHECKED_CAST")
            val iconsList = languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as List<JsonStructureSV.IconInfo>
            iconsList.forEach { it.loadIcon() }
            iconsSV[languageName] = iconsList.associateBy { it.id }
        }
    }

    private fun convertJsonToMapSV(jsonSV: String) : MapTypeSV {
        @Suppress("UNCHECKED_CAST")
        val tmpUpdatedJsonSV = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
            .fromJson(jsonSV) as MapTypeSV

        for (languageStructure in tmpUpdatedJsonSV.values) {
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

            val icons = mutableListOf<JsonStructureSV.IconInfo>()
            (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.ICONS) as? List<*>)?.forEach {
                    icon ->
                @Suppress("UNCHECKED_CAST")
                icons.add(JsonStructureSV.IconInfo.fromJson(icon as Map<String, String>))
            }

            languageStructure[SvConstants.ATTRIBUTES] = mutableMapOf(
                SvConstants.LISTS to (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS) ?: mutableMapOf<String, String>()),
                SvConstants.PROPERTIES to properties,
                SvConstants.KEYWORDS to (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) ?: mutableMapOf<String, String>()),
                SvConstants.ICONS to icons
            )

            @Suppress("UNCHECKED_CAST")
            val visibilityFiltersMap = (languageStructure[SvConstants.FILTERS]?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, Map<String, Any>>)
            val visibilityFilters = mutableMapOf<String, JsonStructureSV.VisibilityFilterInfo>()
            visibilityFiltersMap?.entries?.forEach { visibilityFilter ->
                visibilityFilters[visibilityFilter.key] =
                    JsonStructureSV.VisibilityFilterInfo.fromJson(visibilityFilter.value)
            }

            languageStructure[SvConstants.FILTERS] = mutableMapOf(SvConstants.VISIBILITY_FILTERS to visibilityFilters)
        }
        return tmpUpdatedJsonSV
    }

    private fun languageToLowerCase(mapSV : MapTypeSV) : MapTypeSV {
        return  mapSV.mapKeys { it.key.lowercase() }.toMutableMap()
    }
}

typealias MapTypeSV = MutableMap<String, MutableMap<String, MutableMap<String, out Any>>>