package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.utils.JsonContainerUtil

interface IElementCreator {
    fun createElement(
        langElement: PsiElement,
        typeElement: String,
        parent: BaseElement,
        jsonUtil: JsonContainerUtil
    ): BaseElement
}