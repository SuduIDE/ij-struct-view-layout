package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.IElementCreator
import com.rri.lsvplugin.utils.JsonContainerUtil

interface IElementVisitor {
    fun visitElement(element: BaseElement, elementCreator: IElementCreator, jsonUtil: JsonContainerUtil)
}