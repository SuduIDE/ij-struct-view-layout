package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class FieldElement(private val langElement: PsiElement) : BaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {
    }

    override fun createPresentableView() {
        val printableText = StringBuilder()
        printableText.append(elementStructure.getName()).append(": ").append(elementStructure.getType())
        if (elementStructure.getValue() != null)
            printableText.append(" = ").append(elementStructure.getValue())

        presentableView = PresentableView(
            printableText.toString(), IconManager.getInstance().getPlatformIcon(
                PlatformIcons.Field
            )
        )
    }
}