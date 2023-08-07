package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.utils.JsonContainerUtil
import com.rri.lsvplugin.utils.JsonStructureSV

class CustomizedElementCreator : IElementCreator {
    override fun createElement(
        langElement: PsiElement,
        typeElement: String,
        parent: BaseElement,
        jsonUtil: JsonContainerUtil
    ): BaseElement {
        val elementStructure = jsonUtil.getElementByName(langElement, typeElement)

        val element = BaseElement(langElement)
        element.displayLevel = elementStructure.displayLevel
        element.elementType = typeElement
        setElementStructure(element, elementStructure)
        setIconStructure(element, elementStructure.baseIcon, jsonUtil)
        element.presentableText = element.PresentableViewText(elementStructure.text, elementStructure.description)
        element.parent = parent

        return element
    }


    private fun setIconStructure(
        element: BaseElement,
        iconProperties: JsonStructureSV.IconProperties,
        jsonUtil: JsonContainerUtil
    ) {
        val baseIconId = jsonUtil.getIconInfo(element.langElement, iconProperties.iconId)
        element.baseIcon = BaseElement.IconStructure(
            baseIconId,
            iconProperties.attributeKey,
            iconProperties.attributeValue,
            jsonUtil.getIconInfo(element.langElement, iconProperties.alternativeIconId)
        )
    }

    private fun setElementStructure(
        element: BaseElement,
        elementStructure: JsonStructureSV.ElementInfo
    ) {
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
    }

}