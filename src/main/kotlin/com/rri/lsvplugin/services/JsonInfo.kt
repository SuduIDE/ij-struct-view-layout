package com.rri.lsvplugin.services

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

class JsonInfo {

    private var mapSV: MapTypeSV? = null
    val filenameJsonSV: File = File((".customSV.json"))

    fun reset() {
        mapSV =
            mutableMapOf(
                "JAVA" to
                        mutableMapOf(
                            "element" to
                                    mutableMapOf(
                                        "class" to JsonStructureSV.ElementInfo(
                                            1,
                                            "CLASS",
                                            mutableMapOf(
                                                "set" to listOf(),
                                                "unique" to listOf("modifiers", "name", "class_keyword")
                                            ),
                                            "default",
                                            listOf("name"),
                                            listOf()
                                        ),
                                        "method" to JsonStructureSV.ElementInfo(
                                            1,
                                            "METHOD",
                                            mutableMapOf(
                                                "set" to listOf(),
                                                "unqiue" to listOf("modifiers", "name", "parameters", "type")
                                            ),
                                            "default",
                                            listOf("name", "(", "parameters", ")", " : ", "type"),
                                            listOf()
                                        ),
                                        "field" to JsonStructureSV.ElementInfo(
                                            1,
                                            "FIELD",
                                            mutableMapOf(
                                                "set" to listOf(),
                                                "unique" to listOf("name", "type")
                                            ),
                                            "default",
                                            listOf("name", ":", "type"),
                                            listOf()
                                        ),
                                        "parameter" to JsonStructureSV.ElementInfo(
                                            0,
                                            "PARAMETER",
                                            mutableMapOf(
                                                "set" to listOf(),
                                                "unique" to listOf("name", "type")
                                            ),
                                            "default",
                                            listOf("type"),
                                            listOf()
                                        ),
                                    ),
                            "attribute" to
                                    mutableMapOf(
                                        "list" to mutableMapOf(
                                            "modifiers" to "MODIFIER_LIST",
                                            "parameters" to "PARAMETER_LIST",
                                        ),
                                        "properties" to mutableMapOf(
                                            "name" to "IDENTIFIER",
                                            "type" to "TYPE",
                                        ),
                                        "keywords" to mutableMapOf(
                                            "class_keyword" to "CLASS_KEYWORD",
                                            "private" to "PRIVATE_KEYWORD",
                                            "public" to "PUBLIC_KEYWORD",
                                            "final" to "FINAL_KEYWORD",
                                            "static" to "STATIC_KEYWORD",
                                            "abstract" to "ABSTRACT_KEYWORD"
                                        )
                                    ),
                            "filters" to
                                    mutableMapOf(
                                        "visibility" to mutableMapOf(
                                            "Fields" to mutableMapOf(
                                                "elementType" to "field",
                                            )
                                        ),
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