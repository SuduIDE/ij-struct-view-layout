package com.rri.lsvplugin.languageElements.factory

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.services.JsonInfo

class CustomizedElementCreator : IElementCreator {
    override fun createElement(langElement: PsiElement, typeElement: String, elementStructure: JsonInfo.ElementInfo) : BaseElement {
        val element = BaseElement(langElement)
        element.typeElement = typeElement
        for (attr in elementStructure.attributes) {
            element.structure[attr] = null
        }

        element.baseIcon = elementStructure.baseIcon
        element.presentableTextList = elementStructure.text
        return element
    }

}