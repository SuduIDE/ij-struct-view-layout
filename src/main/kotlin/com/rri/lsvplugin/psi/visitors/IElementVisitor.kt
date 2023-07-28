package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.psi.JsonContainerUtil

interface IElementVisitor {
    fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil)
}