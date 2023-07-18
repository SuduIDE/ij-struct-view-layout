package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class ClassElement(private val langElement: PsiElement) : ClassBaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {

    }

    fun isRunnable() : Boolean {
        return getChildren().find {it.elementStructure.getName() == "main"} != null
    }

    override fun isPublic(): Boolean = true
}