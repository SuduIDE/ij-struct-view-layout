package com.rri.lsvplugin.languageElements.elementUtils

import com.rri.lsvplugin.languageElements.elements.BaseElement

class ElementStructure {
    var structure = mutableMapOf<String, Any?>(
        "name" to null,
        "type" to null,
        "modifiers" to ArrayList<String>(),
        "value" to null,
        "parameters" to ArrayList<String>(),
    )

    val children = ArrayList<BaseElement>()

    fun getName(): String? = structure["name"] as? String

    fun getType(): String? = structure["type"] as? String

    fun getValue(): String? = structure["value"] as? String

    fun getModifiers(): MutableList<String>? = structure["modifiers"] as? MutableList<String>

    fun getParameters(): MutableList<String>? = structure["parameters"] as? MutableList<String>
}