package com.rri.lsvplugin.languageElements.elements

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class LambdaElement(langElement: PsiElement) : ClassBaseElement(langElement) {
    override fun createPresentableView() {

    }

    override fun accept(visitor: IElementVisitor) {

    }

}