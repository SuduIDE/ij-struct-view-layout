package com.rri.lsvplugin.languageElements.factory.elementCreator
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.utils.JsonInfo
import com.rri.lsvplugin.utils.JsonStructureSV

interface IElementCreator {
    fun createElement(langElement: PsiElement, typeElement: String, elementStructure: JsonStructureSV.ElementInfo, parent: BaseElement) : BaseElement
}