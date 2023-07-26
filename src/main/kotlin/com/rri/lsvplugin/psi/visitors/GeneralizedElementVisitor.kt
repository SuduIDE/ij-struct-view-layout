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
                    if (curElement.structure.containsKey(it) && curElement.structure[it] == null) {
                        curElement.structure[it] = visitList(child, elementCreator, jsonUtil)
                    }
                } ?:
                jsonUtil.getKeywordAttribute(child)?.also {
                    if (curElement.structure.containsKey(it) && curElement.structure[it] == null)
                        curElement.structure[it] = it
                } ?:
                jsonUtil.getPropertyAttribute(child)?.also {
                    if (curElement.structure.containsKey(it) && curElement.structure[it] == null)
                        curElement.structure[it] = child.text
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

                        if (curElement.structure.containsKey(elementName) && curElement.structure[elementName] == null)
                            curElement.structure[elementName] = newBaseElement
                        else
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