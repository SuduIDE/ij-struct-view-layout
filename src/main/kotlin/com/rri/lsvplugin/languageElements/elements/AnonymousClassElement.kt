package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class AnonymousClassElement(private val langElement: PsiElement) : ClassBaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {
        TODO("Not yet implemented")
    }

    override fun createPresentableView() {
        TODO("Not yet implemented")
    }
}