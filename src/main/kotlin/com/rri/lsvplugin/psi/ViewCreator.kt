package com.rri.lsvplugin.psi

import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.languageElements.factory.filterCreator.IFilterCreator
import com.rri.lsvplugin.psi.visitors.IElementVisitor


class ViewCreator(
    private val visitor: IElementVisitor,
    private val elementCreator: IElementCreator,
    private val filterCreator: IFilterCreator
) {

    private val jsonUtil = JsonContainerUtil()

    fun visitElement(element: BaseElement) {
        visitor.clear()
        visitor.visitElement(element, elementCreator, jsonUtil)
    }

    fun createFilters(langElement: PsiElement) : List<Filter> {
        val filtersList = mutableListOf<Filter>()

        val visibilityFilters = jsonUtil.getVisibilityFilters(langElement) ?: return filtersList

        for ((key, value) in visibilityFilters.entries) {
            filtersList.add(filterCreator.createFilter(key, value))
        }

        return filtersList
    }


}