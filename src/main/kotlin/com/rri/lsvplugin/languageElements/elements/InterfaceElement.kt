package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class InterfaceElement(private val langElement: PsiElement) : BaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {
        TODO("Not yet implemented")
    }

}