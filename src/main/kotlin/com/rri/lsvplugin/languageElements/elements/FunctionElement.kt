package com.rri.lsvplugin.languageElements.elements

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class FunctionElement(private val langElement: PsiElement) : FunctionBaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {
    }

    override fun createPresentableView() {
        val presentableText = StringBuilder()
        presentableText.append(elementStructure.getName()).append(": ").append(elementStructure.getType())

        presentableView = PresentableView(
            presentableText.toString(), IconManager.getInstance().getPlatformIcon(
                PlatformIcons.Function
            )
        )
    }
}