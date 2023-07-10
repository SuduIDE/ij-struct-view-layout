package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.intellij.ui.RowIcon
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

class CallableClassElement(private val langElement: PsiElement) : ClassBaseElement(langElement) {
    override fun accept(visitor: IElementVisitor) {

    }

    override fun createPresentableView() {
        val iconManager = IconManager.getInstance()
        var icon = iconManager.createLayered(
            iconManager.getPlatformIcon(PlatformIcons.Class),
            iconManager.getPlatformIcon(PlatformIcons.RunnableMark)
            )
        icon = iconManager.createRowIcon(icon, iconManager.getPlatformIcon(PlatformIcons.Public))
        if (elementStructure.getModifiers()?.contains("final") == true) {
            icon = iconManager.createLayered(
                iconManager.getPlatformIcon(PlatformIcons.Class),
                iconManager.getPlatformIcon(PlatformIcons.FinalMark),
            )
            icon = iconManager.createRowIcon(icon, iconManager.getPlatformIcon(PlatformIcons.Local))
        }
        presentableView = PresentableView(elementStructure.getName(), icon)
    }
}