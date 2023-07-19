package com.rri.lsvplugin.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class JsonInfo {
    private lateinit var mapSV: MapTypeSV

    init {
        reset()
    }

    val filenameJsonSV: File = File((".customSV.json"))

    fun reset() {
        mapSV = mutableMapOf(
            "Java" to
                    mutableMapOf(
                        "element" to
                                mutableMapOf(
                                    "class" to "CLASS",
                                    "method" to "METHOD",
                                    "field" to "FIELD",
                                ),
                        "attribute" to
                                mutableMapOf(
                                    "modifiers" to "MODIFIER_LIST",
                                    "parameters" to "PARAMETER_LIST",
                                    "parameter" to "PARAMETER",
                                    "name" to "IDENTIFIER",
                                    "type" to "TYPE",
                                    "private" to "PRIVATE_KEYWORD",
                                    "public" to "PUBLIC_KEYWORD",
                                    "final" to "FINAL_KEYWORD",
                                    "static" to "STATIC_KEYWORD",
                                    "abstract" to "ABSTRACT_KEYWORD"
                                ),
                    )
        )
    }

    fun setMapSV(newMapSV: MapTypeSV) {
        mapSV = newMapSV
    }

    fun getMapSV(): MapTypeSV = mapSV

    fun getJsonSV(): String {
        val format = Json { prettyPrint = true }
        return format.encodeToString(mapSV)
    }
}

typealias MapTypeSV = MutableMap<String, MutableMap<String, MutableMap<String, String>>>