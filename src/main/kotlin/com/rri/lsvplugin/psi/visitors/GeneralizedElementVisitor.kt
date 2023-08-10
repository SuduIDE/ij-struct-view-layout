package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.utils.JsonContainerUtil
import com.rri.lsvplugin.utils.JsonStructureSV
import java.util.*

class GeneralizedElementVisitor : IElementVisitor {
    private val attributeSupplier: IAttributeSupplier = AttributeSupplier()
    override fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil) {
        val queueChildren: Queue<Pair<BaseElement, PsiElement>> = LinkedList<Pair<BaseElement, PsiElement>>()
        queueChildren.add(Pair(element, element.langElement))
        while (queueChildren.isNotEmpty()) {
            val (curElement, curLangElement) = queueChildren.poll()
            var child = curLangElement.firstChild
            while (child != null) {
                jsonUtil.getListAttribute(child)?.also {
                    val visitedList = visitList(curElement, child, elementCreator, jsonUtil)
                    attributeSupplier.addAttribute(curElement, it, visitedList)
                } ?: jsonUtil.getKeywordAttribute(child)?.also {
                    var icon : JsonStructureSV.IconInfo? = null
                    if (it.iconId != null)
                        icon = jsonUtil.getIconInfo(child, it.iconId)
                    attributeSupplier.addAttribute(curElement, it.id, BaseElement.KeywordStructure(it.id, child.text, it.sortValue, icon))
                } ?: jsonUtil.getPropertyAttribute(child)?.also {
                    if (curElement.displayLevel != 0 && it.isNotPartialMatch(child) && attributeSupplier.containsUniqueElement(
                            curElement,
                            it.id
                        )
                    )
                        curElement.displayLevel = -1

                    attributeSupplier.addAttribute(curElement, it.id, child.text)
                } ?: jsonUtil.getElementNames(child)?.also {
                    for (elementName in it) {
                        val newBaseElement = elementCreator.createElement(
                            child,
                            elementName,
                            curElement,
                            jsonUtil
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

    private fun visitList(
        curElement: BaseElement,
        langElement: PsiElement,
        elementCreator: IElementCreator,
        jsonUtil: JsonContainerUtil
    ): List<Any> {
        val listOfAttr = mutableListOf<Any>()
        for (child in langElement.children) {
            jsonUtil.getElementNames(child)?.also {
                for (elementName in it) {
                    val newBaseElement = elementCreator.createElement(
                        child,
                        elementName,
                        curElement,
                        jsonUtil
                    )
                    visitElement(newBaseElement, elementCreator, jsonUtil)

                    if (!newBaseElement.isFull())
                        continue

                    curElement.children.add(newBaseElement)
                    listOfAttr.add(newBaseElement)

                }
            }
            jsonUtil.getKeywordAttribute(child)?.also {
                var icon : JsonStructureSV.IconInfo? = null
                if (it.iconId != null)
                    icon = jsonUtil.getIconInfo(child, it.iconId)
                listOfAttr.add(BaseElement.KeywordStructure(it.id, child.text, it.sortValue, icon))
            }
            jsonUtil.getPropertyAttribute(child)?.also { listOfAttr.add(child.text) }
        }

        return listOfAttr
    }

}