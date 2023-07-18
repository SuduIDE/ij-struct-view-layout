package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class LambdaElement(langElement: PsiElement) : ClassBaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {

    }

}