package com.rri.lsvplugin.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.nio.file.Path

class JsonInfo {

    private var mapSV: MapTypeSV? = null
    val filenameJsonSV: File = File(".customSV.json")
    private val defaultJsonSv: Path = Path.of("defaultCustomSV/.defaultJavaCustomSV.json")

    fun reset() {
        val jsonSV = javaClass.classLoader?.getResourceAsStream("defaultCustomSV/.defaultJavaCustomSV.json")?.reader()?.readText()
        if (jsonSV != null) {
           setMapSV(convertJsonToMapSV(jsonSV))
        }
    }

    fun setMapSV(newMapSV: MapTypeSV?) {
        if (newMapSV != null)
            mapSV = newMapSV
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

    private fun convertJsonToMapSV(jsonSV: String) : MapTypeSV {
        @Suppress("UNCHECKED_CAST")
        val tmpUpdatedJsonSV = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
            .fromJson(jsonSV) as MapTypeSV

        for (language in tmpUpdatedJsonSV.values) {
            val newElements = mutableMapOf<String, JsonStructureSV.ElementInfo>()
            language[SvConstants.ELEMENTS]?.forEach { element ->
                newElements[element.key] = JsonStructureSV.ElementInfo.fromJson(element.value as Map<String, Any>)
            }
            language[SvConstants.ELEMENTS] = newElements

            val properties = mutableListOf<JsonStructureSV.PropertyInfo>()
            (language[SvConstants.ATTRIBUTES]?.get(SvConstants.PROPERTIES) as? List<*>)?.forEach {
                    property -> properties.add(JsonStructureSV.PropertyInfo.fromJson(property as Map<String, String>))
            }
            language[SvConstants.ATTRIBUTES] = mutableMapOf(
                SvConstants.LISTS to (language[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS) ?: mutableMapOf<String, String>()),
                SvConstants.PROPERTIES to properties,
                SvConstants.KEYWORDS to (language[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) ?: mutableMapOf<String, String>())
            )

            val visibilityFiltersMap = (language[SvConstants.FILTERS]?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, Map<String, Any>>)
            val visibilityFilters = mutableMapOf<String, JsonStructureSV.VisibilityFilterInfo>()
            visibilityFiltersMap?.entries?.forEach { visibilityFilter ->
                visibilityFilters[visibilityFilter.key] =
                    JsonStructureSV.VisibilityFilterInfo.fromJson(visibilityFilter.value)
            }

            language[SvConstants.FILTERS] = mutableMapOf(SvConstants.VISIBILITY_FILTERS to visibilityFilters)
        }
        return tmpUpdatedJsonSV
    }
}

typealias MapTypeSV = MutableMap<String, MutableMap<String, MutableMap<String, out Any>>>