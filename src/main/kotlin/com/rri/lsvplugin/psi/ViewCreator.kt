package com.rri.lsvplugin.psi

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.visitors.IElementVisitor


class ViewCreator(
    private val visitor: IElementVisitor,
) {

    private val jsonUtil = JsonContainerUtil()

    fun visitElement(element: BaseElement) {
        visitor.visitElement(element, jsonUtil)
    }


}