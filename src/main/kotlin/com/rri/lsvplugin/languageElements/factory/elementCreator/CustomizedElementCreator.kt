package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.services.JsonStructureSV

class CustomizedElementCreator : IElementCreator {
    override fun createElement(langElement: PsiElement, typeElement: String, elementStructure: JsonStructureSV.ElementInfo) : BaseElement {
        val element = BaseElement(langElement)
        element.displayLevel = elementStructure.displayLevel
        element.elementType = typeElement
        val setAttributes = mutableMapOf<String, MutableList<*>?>()
        if (elementStructure.attributes.containsKey("set")) {
            for (attr in elementStructure.attributes["set"]!!) {
                setAttributes[attr] = null
            }
        }

        val uniqueAttributes = mutableMapOf<String, Any?>()
        if (elementStructure.attributes.containsKey("unique")) {
            for (attr in elementStructure.attributes["unique"]!!) {
                uniqueAttributes[attr] = null
            }
        }

        element.structure = BaseElement.ElementStructure(setAttributes, uniqueAttributes)
        element.baseIcon = elementStructure.baseIcon
        element.presentableText = BaseElement.ElementPresentableText(elementStructure.text, elementStructure.description)


        return element
    }

}