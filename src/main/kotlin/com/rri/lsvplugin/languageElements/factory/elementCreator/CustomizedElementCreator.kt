package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.Attributes
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
        element.displayLevel = elementStructure.displayLevel ?: 1
        element.displayOnlyLevel = elementStructure.displayOnlyLevel ?: 0
        element.elementType = typeElement
        getElementStructure(elementStructure).clone(element)
        element.baseIcon = getIconStructure(element, elementStructure.baseIcon, jsonUtil)
        element.presentableText = element.PresentableViewText(elementStructure.text, elementStructure.description)
        element.parent = parent
        element.defaultAttributes = getDefaultAttributes(element, elementStructure, jsonUtil)

        return element
    }


    private fun getIconStructure(
        element: BaseElement,
        iconProperties: JsonStructureSV.IconProperties,
        jsonUtil: JsonContainerUtil
    ): BaseElement.IconStructure {
        val baseIconId = jsonUtil.getIconInfo(element.langElement, iconProperties.iconId)
        return BaseElement.IconStructure(
            baseIconId,
            iconProperties.attributeKey,
            iconProperties.attributeValue,
            jsonUtil.getIconInfo(element.langElement, iconProperties.alternativeIconId)
        )
    }

    private fun getElementStructure(
        elementStructure: JsonStructureSV.ElementInfo
    ): Attributes {
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

        val optionalAttributes = mutableMapOf<String, Any?>()
        if (elementStructure.attributes.containsKey("optional")) {
            for (attr in elementStructure.attributes["optional"]!!) {
                optionalAttributes[attr] = null
            }
        }

        val exclusiveAttributes = mutableMapOf<String, Any?>()
        if (elementStructure.attributes.containsKey("exclusive")) {
            for (attr in elementStructure.attributes["exclusive"]!!) {
                exclusiveAttributes[attr] = null
            }
        }

        return Attributes(setAttributes, uniqueAttributes, optionalAttributes, exclusiveAttributes)
    }

    private fun getDefaultAttributes(
        element: BaseElement,
        elementStructure: JsonStructureSV.ElementInfo,
        jsonUtil: JsonContainerUtil
    ) : Attributes.DefaultAttributes {
        val defaultAttributesRelatedParent = elementStructure.defaultAttributes?.parent
        val defaultAttributesRelatedChildren = elementStructure.defaultAttributes?.children
        return Attributes.DefaultAttributes(
            fillDefaultAttributeMap(element, defaultAttributesRelatedParent, jsonUtil),
            fillDefaultAttributeMap(element, defaultAttributesRelatedChildren, jsonUtil)
        )
    }

    private fun fillDefaultAttributeMap(
        element: BaseElement,
        defaultAttributes : Map<String, List<String>>?,
        jsonUtil: JsonContainerUtil
    ) : Map<String, List<Attributes.KeywordStructure>>? {
        if (defaultAttributes == null)
            return null

        val defaultAttributeElementMap = mutableMapOf<String, MutableList<Attributes.KeywordStructure>>()
        for ((parentType, keywordList) in defaultAttributes) {
            defaultAttributeElementMap[parentType] = mutableListOf()
            for (keywordName in keywordList) {
                val keyword = jsonUtil.getKeywordAttributeByName(keywordName, element.langElement)
                if (keyword != null) {
                    var icon: JsonStructureSV.IconInfo? = null
                    if (keyword.iconId != null)
                        icon = jsonUtil.getIconInfo(element.langElement,  keyword.iconId)
                    defaultAttributeElementMap[parentType]
                        ?.add(Attributes.KeywordStructure(keyword.id, "", keyword.sortValue, icon))
                }
            }
        }

        return defaultAttributeElementMap
    }
}