package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.*
import com.rri.lsvplugin.languageElements.factory.CustomizedElementCreator
import com.rri.lsvplugin.languageElements.factory.IElementCreator
import com.rri.lsvplugin.psi.JsonContainerUtil

class GeneralizedElementVisitor : IElementVisitor {
    override fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) {
        for (child in element.langElement.children) {
            when (val elementNames = jsonUtil.getElementNames(child)) {
                null -> null
                else -> {
                    for (elementName in elementNames) {
                        val newBaseElement = elementCreator.createElement(child, elementName, jsonUtil.getElementByName(child, elementName))
                        visitElement(newBaseElement, elementCreator, jsonUtil)
                        if (!newBaseElement.isFull())
                            continue

                        if (element.structure.containsKey(elementName))
                            element.structure[elementName] = newBaseElement
                        else
                            element.children.add(newBaseElement)
                    }
                }
            }

            when (val listAttribute = jsonUtil.getListAttrubute(child)) {
                null -> null
                else -> {
                    if (element.structure.containsKey(listAttribute) && element.structure[listAttribute] == null) {
                        element.structure[listAttribute] = visitList(child, elementCreator, jsonUtil)
                    }
                }
            }

            when (val keywordAttribute = jsonUtil.getKeywordAttribute(child)) {
                null -> null
                else -> {
                    if (element.structure.containsKey(keywordAttribute) && element.structure[keywordAttribute] == null)
                        element.structure[keywordAttribute] = child.text
                }
            }

        }
    }

    private fun visitList(langElement: PsiElement,elementCreator : IElementCreator, jsonUtil: JsonContainerUtil): List<Any> {
        val listOfAttr = mutableListOf<String>()
        for (child in langElement.children) {
            when (val elementsName = jsonUtil.getElementNames(langElement)) {
                null -> null
                else -> {
                    val newBaseElement = elementCreator.createElement(child, elementName, jsonUtil.getElementByName(child, elementName))
                    visitElement(newBaseElement, elementCreator, jsonUtil)
                    listOfAttr.add(newBaseElement)
                }
            }
            if (jsonUtil.isKeywordAttribute(child))
                listOfAttr.add(child.text)
        }

        return listOfAttr
    }

}