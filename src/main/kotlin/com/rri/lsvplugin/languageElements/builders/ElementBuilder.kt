package com.rri.lsvplugin.languageElements.builders

import com.rri.lsvplugin.languageElements.elementUtils.ElementStructure

class ElementBuilder : BaseElementStructureBuilder {
    private var elementStructure: ElementStructure = ElementStructure()

    override fun reset(otherStructure: ElementStructure) {
        if (otherStructure != elementStructure)
            elementStructure = otherStructure
    }

    override fun setName(name: String) {
        elementStructure.structure["name"] = name
    }

    override fun setType(type: String) {
        elementStructure.structure["type"] = type
    }

    override fun setInitializedValue(value: String) {
        elementStructure.structure["value"] = value
    }

    override fun setParams(args: List<String>) {
        elementStructure.structure["parameters"] = args
    }

    override fun setModifiers(modifiers: List<String>) {
        elementStructure.structure["modifiers"] = modifiers
    }

    override fun addModifier(modifier: String) {
        elementStructure.getModifiers()?.add(modifier)
    }

    override fun getElementStructure(): ElementStructure = elementStructure
}