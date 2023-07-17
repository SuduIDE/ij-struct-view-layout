package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.builders.BaseElementStructureBuilder
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.JsonContainerUtil

interface IElementVisitor {
    fun visitElement(element: BaseElement, builder: BaseElementStructureBuilder, jsonUtil: JsonContainerUtil)
}