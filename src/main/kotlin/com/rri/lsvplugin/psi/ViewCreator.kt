package com.rri.lsvplugin.psi

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.builders.BaseElementStructureBuilder
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.IElementFactory
import com.rri.lsvplugin.languageElements.factory.IPresentableViewFactory
import com.rri.lsvplugin.psi.visitors.IElementVisitor


class ViewCreator(
    private val factoryElement: IElementFactory,
    private val factoryPresentableView: IPresentableViewFactory,
    private val visitor: IElementVisitor,
    private val builder: BaseElementStructureBuilder
) {

    private val jsonUtil = JsonContainerUtil()
    fun createElement(langElement: PsiElement): BaseElement? {
        return when (jsonUtil.getMainKeywords(langElement)) {
            "class" -> factoryElement.createClass(langElement)
            "interface" -> factoryElement.createInterface(langElement)
            "method" -> factoryElement.createMethod(langElement)
            "field" -> factoryElement.createField(langElement)
            else -> null
        }
    }

    fun visitElement(element: BaseElement) {
        visitor.visitElement(element, builder, jsonUtil)
    }

    fun createPresentableViewElement(element: BaseElement) : PresentableView {
        return factoryPresentableView.createPresentableView(element)
    }

}