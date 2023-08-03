package com.rri.lsvplugin.psi

import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.psi.structure.filters.VisibilityFilter
import com.rri.lsvplugin.psi.visitors.IElementVisitor
import com.rri.lsvplugin.utils.JsonContainerUtil
import java.util.*


class ViewCreator(
    private val visitor: IElementVisitor,
    private val elementCreator: IElementCreator,
) {

    private val jsonUtil = JsonContainerUtil()

    fun visitElement(element: BaseElement) {
        visitor.visitElement(element, elementCreator, jsonUtil)
    }

    fun createFilters(langElement: PsiElement) : List<Filter> {
        val filtersList = mutableListOf<Filter>()

        val visibilityFilters = jsonUtil.getVisibilityFilters(langElement) ?: return filtersList

        for ((key, value) in visibilityFilters.entries) {
            filtersList.add(VisibilityFilter(key, value))
        }

        return filtersList
    }

    fun adjustDisplayLevel(element : BaseElement) {
        val queueChildren : Queue<BaseElement> = LinkedList()
        queueChildren.add(element)
        while(queueChildren.isNotEmpty()) {
            val curElement = queueChildren.poll()
            for (child in curElement.children)
                queueChildren.add(child)

            curElement.children.clear()

            if (curElement.displayLevel > 0) {
                var i = 1
                while (i < curElement.displayLevel && curElement.parent?.parent != null) {
                    curElement.parent = curElement.parent?.parent
                    ++i
                }

                while (curElement.parent?.parent != null && curElement.parent?.displayLevel == -1) {
                    curElement.parent = curElement.parent?.parent
                }

                curElement.parent?.children?.add(curElement)
            }

        }
    }


}