package com.rri.lsvplugin.languageElements.elements

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.psi.visitors.IElementVisitor

import com.intellij.ui.IconManager

class FileElement(private val langElement: PsiElement): BaseElement(langElement) {
    override fun createPresentableView() {
       presentableView = PresentableView(langElement.containingFile.name, IconManager.getInstance().getPlatformIcon(PlatformIcons.CustomFileType))
    }

    override fun accept(visitor: IElementVisitor) {

    }
}