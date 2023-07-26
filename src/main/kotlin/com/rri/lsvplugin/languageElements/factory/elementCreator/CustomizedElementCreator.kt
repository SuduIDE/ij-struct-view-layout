package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.services.JsonStructureSV

class CustomizedElementCreator : IElementCreator {
    override fun createElement(langElement: PsiElement, typeElement: String, elementStructure: JsonStructureSV.ElementInfo) : BaseElement {
        val element = BaseElement(langElement)
        element.elementType = typeElement
        for (attr in elementStructure.attributes) {
            element.structure[attr] = null
        }

        element.baseIcon = elementStructure.baseIcon
        element.presentableTextList = elementStructure.text
        element.presentableDescriptionList = elementStructure.description

        return element
    }

}