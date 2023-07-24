package com.rri.lsvplugin.languageElements.factory
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.services.JsonInfo

interface IElementCreator {
    fun createElement(langElement: PsiElement, typeElement: String, elementStructure: JsonInfo.ElementInfo) : BaseElement
}