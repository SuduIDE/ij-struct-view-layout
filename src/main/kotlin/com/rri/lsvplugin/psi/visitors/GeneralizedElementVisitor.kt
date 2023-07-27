package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.alsoIfNull
import com.rri.lsvplugin.languageElements.elements.*
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.psi.JsonContainerUtil
import java.util.*

class GeneralizedElementVisitor : IElementVisitor {
    private val queueChildren : Queue<Pair<BaseElement, PsiElement>> = LinkedList<Pair<BaseElement, PsiElement>>()
    override fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) {
        queueChildren.add(Pair(element, element.langElement))
        while(queueChildren.isNotEmpty()) {
            val (curElement, curLangElement) = queueChildren.poll()
            var child = curLangElement.firstChild
            while (child != null) {
                jsonUtil.getListAttribute(child)?.also {
                    val visitedList = visitList(child, elementCreator, jsonUtil)
                    addAttribute(curElement, it, visitedList)
                } ?:
                jsonUtil.getKeywordAttribute(child)?.also {
                    addAttribute(curElement, it, it)
                } ?:
                jsonUtil.getPropertyAttribute(child)?.also {
                    addAttribute(curElement, it, child.text)
                } ?:
                jsonUtil.getElementNames(child)?.also {
                    for (elementName in it) {
                        val newBaseElement = elementCreator.createElement(
                            child,
                            elementName,
                            jsonUtil.getElementByName(child, elementName)
                        )
                        visitElement(newBaseElement, elementCreator, jsonUtil)
                        if (!newBaseElement.isFull())
                            continue

                        if (!addAttribute(curElement, elementName, newBaseElement) && newBaseElement.displayLevel != 0)
                            curElement.children.add(newBaseElement)
                    }
                } ?: queueChildren.add(Pair(curElement, child))

                child = child.nextSibling
            }
        }
    }

    override fun clear() {
        queueChildren.clear()
    }

    private fun addAttribute(curElement : BaseElement, attributeName : String, attributeValue : Any) : Boolean {
        if (curElement.getUniqueAttributes().containsKey(attributeName) && curElement.getUniqueAttributes()[attributeName] == null) {
            curElement.getUniqueAttributes()[attributeName] = attributeValue
            return true
        } else if (curElement.getSetAttributes().containsKey(attributeName)) {
            if (curElement.getSetAttributes()[attributeName] == null)
                curElement.getSetAttributes()[attributeName] = mutableListOf(attributeValue)
            else
                (curElement.getSetAttributes()[attributeName] as MutableList<Any>).add(attributeValue)
            return true
        }
        return false
    }

    private fun visitList(langElement: PsiElement, elementCreator : IElementCreator, jsonUtil: JsonContainerUtil): List<Any> {
        val listOfAttr = mutableListOf<Any>()
        for (child in langElement.children) {
            jsonUtil.getElementNames(child)?.also {
                for (elementName in it) {
                    val newBaseElement = elementCreator.createElement(
                        child,
                        elementName,
                        jsonUtil.getElementByName(child, elementName)
                    )
                   visitElement(newBaseElement, elementCreator, jsonUtil)

                    if (!newBaseElement.isFull())
                        continue

                    listOfAttr.add(newBaseElement)

                }
            }
            jsonUtil.getKeywordAttribute(child)?.also { listOfAttr.add(it) }
            jsonUtil.getPropertyAttribute(child)?.also { listOfAttr.add(child.text) }
        }

        return listOfAttr
    }

}