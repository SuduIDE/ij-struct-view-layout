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
        element.structure = getElementStructure(elementStructure)
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
    ): BaseElement.ElementStructure {
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

        return BaseElement.ElementStructure(setAttributes, uniqueAttributes)
    }

    private fun getDefaultAttributes(
        element: BaseElement,
        elementStructure: JsonStructureSV.ElementInfo,
        jsonUtil: JsonContainerUtil
    ) : BaseElement.DefaultAttributes {
        val defaultAttributesRelatedParent = elementStructure.defaultAttributes?.parent ?: return BaseElement.DefaultAttributes(null)
        val defaultAttributeRelatedParentElementMap = mutableMapOf<String, MutableList<BaseElement.KeywordStructure>>()
        for ((parentType, keywordList) in defaultAttributesRelatedParent) {
            defaultAttributeRelatedParentElementMap[parentType] = mutableListOf<BaseElement.KeywordStructure>()
            for (keywordName in keywordList) {
                val keyword = jsonUtil.getKeywordAttributeByName(keywordName, element.langElement)
                if (keyword != null) {
                    var icon: JsonStructureSV.IconInfo? = null
                    if (keyword.iconId != null)
                        icon = jsonUtil.getIconInfo(element.langElement,  keyword.iconId)
                    defaultAttributeRelatedParentElementMap[parentType]?.add(BaseElement.KeywordStructure(keyword.id, "", keyword.sortValue, icon))
                }
            }
        }
        return BaseElement.DefaultAttributes(defaultAttributeRelatedParentElementMap)
    }
}