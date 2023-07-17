package com.rri.lsvplugin.psi

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.builders.BaseElementStructureBuilder
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.IElementFactory
import com.rri.lsvplugin.psi.visitors.IElementVisitor


class ViewCreator(
    private val factory: IElementFactory,
    private val visitor: IElementVisitor,
    private val builder: BaseElementStructureBuilder
) {

    private val jsonUtil = JsonContainerUtil()
    fun createElement(langElement: PsiElement): BaseElement? {
        return when (jsonUtil.getMainKeywords(langElement)) {
            "class" -> factory.createClass(langElement)
            "method" -> factory.createMethod(langElement)
            "field" -> factory.createField(langElement)
            else -> null
        }
    }

    fun visitElement(element: BaseElement) {
        visitor.visitElement(element, builder, jsonUtil)
    }

}