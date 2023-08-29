package com.rri.lsvplugin.languageElements.factory.elementCreator

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.utils.JsonContainerUtil
import com.rri.lsvplugin.utils.JsonStructureSV

interface IElementCreator {
    fun createElement(
        langElement: PsiElement,
        elementStructure: JsonStructureSV.ElementInfo,
        parent: BaseElement?,
        jsonUtil: JsonContainerUtil
    ): BaseElement
}