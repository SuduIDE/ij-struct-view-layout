package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.Attributes
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.utils.JsonContainerUtil
import com.rri.lsvplugin.utils.JsonStructureSV
import java.util.*

class GeneralizedElementVisitor : IElementVisitor {
    private val attributeSupplier: IAttributeSupplier = AttributeSupplier()
    private var elementCounter = 0
    override fun visitElement(element: List<BaseElement>, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) {
        val queueChildren: Queue<Pair<List<BaseElement>, PsiElement>> = LinkedList<Pair<List<BaseElement>, PsiElement>>()
        queueChildren.add(Pair(element, element.get(0).langElement))
        while (queueChildren.isNotEmpty()) {
            val (curElement, curLangElement) = queueChildren.poll()
            var child = curLangElement.firstChild
            while (child != null) {
                jsonUtil.getListAttribute(child)?.also {
                    val visitedList = visitList(curElement, child, elementCreator, jsonUtil)
                    attributeSupplier.addAttribute(curElement, it, visitedList)
                } ?: jsonUtil.getKeywordAttribute(child)?.also {
                   visitKeyword(curElement, child, it, jsonUtil)
                } ?: jsonUtil.getPropertyAttribute(child)?.also {
                   visitProperty(curElement, child, it, jsonUtil)
                } ?: jsonUtil.getElementNames(child)?.also {
                    createElement(curElement, child, it, elementCreator, jsonUtil)
                } ?: queueChildren.add(Pair(curElement, child))

                child = child.nextSibling
            }
        }
    }

    private fun createElement(curElements: List<BaseElement>, langElement: PsiElement, elementInfoList: List<JsonStructureSV.ElementInfo>, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) : List<BaseElement>{
        val newElementsList = mutableListOf<BaseElement>()
        for (elementName in elementInfoList) {
            val newBaseElement = elementCreator.createElement(
                langElement,
                elementName,
                null,
                jsonUtil
            )
            newElementsList.add(newBaseElement)
        }

        visitElement(newElementsList, elementCreator, jsonUtil)

        val createdElements = mutableListOf<BaseElement>()
        for (newElement in newElementsList) {
            if (!newElement.isFull()) {
                continue
            }

            attributeSupplier.addAttribute(curElements, newElement.elementType!!, newElement)
            curElements.forEach {
                if (it.displayLevel != 0)
                    it.children.add(newElement)
            }
            if (newElement.displayLevel != 0) {
                newElement.children.forEach {
                    it.parent = newElement
                }
            }
            elementCounter++
            createdElements.add(newElement)
        }

        return createdElements
    }

    private fun visitKeyword(curElement: List<BaseElement>, langElement: PsiElement, keyword: JsonStructureSV.KeywordInfo, jsonUtil: JsonContainerUtil) {
        var icon: JsonStructureSV.IconInfo? = null
        if (keyword.iconId != null)
            icon = jsonUtil.getIconInfo(langElement, keyword.iconId)
        attributeSupplier.addAttribute(
            curElement,
            keyword.id,
            Attributes.KeywordStructure(keyword.id, langElement.text, keyword.sortValue, icon)
        )
    }

    private fun visitProperty(curElements: List<BaseElement>, langElement: PsiElement, properties: List<JsonStructureSV.PropertyInfo>, jsonUtil: JsonContainerUtil) {
        for (property in properties) {
            for (curElement in curElements) {
                if (curElement.displayLevel != 0
                    &&
                    attributeSupplier.containsAttribute(curElement, property.id)
                    &&
                    property.isNotPartialMatch(langElement)
                ) {
                    curElement.displayLevel = property.notMatchedDisplayLevel ?: 0
                    if (curElement.displayLevel > 0)
                        curElement.displayLevel = 0
                }
            }

            attributeSupplier.addAttribute(
                curElements,
                property.id,
                Attributes.PropertyStructure(property.id, langElement.text)
            )
        }
    }

    private fun visitList(
        curElements: List<BaseElement>,
        langElement: PsiElement,
        elementCreator: IElementCreator,
        jsonUtil: JsonContainerUtil
    ): List<Any> {
        val listOfAttr = mutableListOf<Any>()
        var child = langElement.firstChild
        while(child != null) {
            jsonUtil.getElementNames(child)?.also {
                listOfAttr.addAll(createElement(curElements, child, it, elementCreator, jsonUtil))
            }
            jsonUtil.getKeywordAttribute(child)?.also {
                var icon : JsonStructureSV.IconInfo? = null
                if (it.iconId != null)
                    icon = jsonUtil.getIconInfo(child, it.iconId)
                listOfAttr.add(Attributes.KeywordStructure(it.id, child.text, it.sortValue, icon))
            }
            jsonUtil.getPropertyAttribute(child)?.also { listOfAttr.add(child.text) }

            child = child.nextSibling
        }

        return listOfAttr
    }

}