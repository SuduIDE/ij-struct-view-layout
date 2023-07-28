package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.alsoIfNull
import com.rri.lsvplugin.languageElements.elements.*
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.psi.JsonContainerUtil
import java.util.*

class GeneralizedElementVisitor : IElementVisitor {
    private val attributeSupplier : IAttributeSupplier = AttributeSupplier()
    override fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) {
        val queueChildren : Queue<Pair<BaseElement, PsiElement>> = LinkedList<Pair<BaseElement, PsiElement>>()
        queueChildren.add(Pair(element, element.langElement))
        while(queueChildren.isNotEmpty()) {
            val (curElement, curLangElement) = queueChildren.poll()
            var child = curLangElement.firstChild
            while (child != null) {
                jsonUtil.getListAttribute(child)?.also {
                    val visitedList = visitList(curElement, child, elementCreator, jsonUtil)
                    attributeSupplier.addAttribute(curElement, it, visitedList)
                } ?:
                jsonUtil.getKeywordAttribute(child)?.also {
                    attributeSupplier.addAttribute(curElement, it, it)
                } ?:
                jsonUtil.getPropertyAttribute(child)?.also {
                    attributeSupplier.addAttribute(curElement, it, child.text)
                } ?:
                jsonUtil.getElementNames(child)?.also {
                    for (elementName in it) {
                        val newBaseElement = elementCreator.createElement(
                            child,
                            elementName,
                            jsonUtil.getElementByName(child, elementName),
                            curElement
                        )

                        visitElement(newBaseElement, elementCreator, jsonUtil)
                        if (!newBaseElement.isFull())
                            continue

                        if (!attributeSupplier.addAttribute(curElement, elementName, newBaseElement))
                           curElement.children.add(newBaseElement)
                    }
                } ?: queueChildren.add(Pair(curElement, child))

                child = child.nextSibling
            }
        }
    }
    private fun visitList(curElement: BaseElement, langElement: PsiElement, elementCreator : IElementCreator, jsonUtil: JsonContainerUtil): List<Any> {
        val listOfAttr = mutableListOf<Any>()
        for (child in langElement.children) {
            jsonUtil.getElementNames(child)?.also {
                for (elementName in it) {
                    val newBaseElement = elementCreator.createElement(
                        child,
                        elementName,
                        jsonUtil.getElementByName(child, elementName),
                        curElement
                    )
                   visitElement(newBaseElement, elementCreator, jsonUtil)

                    if (!newBaseElement.isFull())
                        continue


                    curElement.children.add(newBaseElement)
                    listOfAttr.add(newBaseElement)

                }
            }
            jsonUtil.getKeywordAttribute(child)?.also { listOfAttr.add(it) }
            jsonUtil.getPropertyAttribute(child)?.also { listOfAttr.add(child.text) }
        }

        return listOfAttr
    }

}