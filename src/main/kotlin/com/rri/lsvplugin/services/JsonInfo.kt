package com.rri.lsvplugin.services

import com.google.gson.JsonElement
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.File

class JsonInfo {
    @JsonClass(generateAdapter = true)
    data class ElementInfo(
        val baseToken: String,
        val attributes: List<String>,
        val baseIcon: String,
        val text: List<String>
    ) {
        companion object {
            fun from(map:  Map<String, Any>) = object {
                val baseToken by map
                val attributes by map
                val baseIcon by map
                val text by map

                val elementInfo = ElementInfo(baseToken as String, attributes as List<String>, baseIcon as String, text as List<String>)
            }.elementInfo
        }
    }



    private var mapSV: MapTypeSV? = null
    val filenameJsonSV: File = File((".customSV.json"))

    fun reset() {
        mapSV =
            mutableMapOf(
                "JAVA" to
                        mutableMapOf(
                            "element" to
                                    mutableMapOf(
                                        "class" to ElementInfo("CLASS", listOf("modifiers","name","class_keyword"), "default", listOf("name")),
                                        "method" to ElementInfo("METHOD", listOf(""), "default", listOf()),
                                        "field" to ElementInfo("FIELD", listOf(""), "default", listOf()),
                                        "parameter" to ElementInfo("PARAMETER", listOf(""), "default", listOf()),
                                    ),
                            "attribute" to
                                    mutableMapOf(
                                        "list" to mutableMapOf(
                                            "modifiers" to "MODIFIER_LIST",
                                            "parameters" to "PARAMETER_LIST",
                                        ),
                                        "keywords" to mutableMapOf(
                                            "name" to "IDENTIFIER",
                                            "type" to "TYPE",
                                            "class_keyword" to "CLASS_KEYWORD",
                                            "private" to "PRIVATE_KEYWORD",
                                            "public" to "PUBLIC_KEYWORD",
                                            "final" to "FINAL_KEYWORD",
                                            "static" to "STATIC_KEYWORD",
                                            "abstract" to "ABSTRACT_KEYWORD"
                                        )
                                    ),
                        )
            )
    }

    fun setMapSV(newMapSV: MapTypeSV) {
        mapSV = newMapSV
    }

    fun getMapSV(): MapTypeSV? = mapSV

    fun getJsonSV(): String {
        return Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java).indent("    ")
            .toJson(mapSV)
    }
}

typealias MapTypeSV = MutableMap<String, MutableMap<String, MutableMap<String, out Any>>>