package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.JsonContainerUtil

interface IElementVisitor {
    fun visitElement(element: BaseElement, jsonUtil: JsonContainerUtil)
}