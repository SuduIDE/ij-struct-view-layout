package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

interface IAttributeSupplier {
    fun addAttribute(curElement : BaseElement, attributeName : String, attributeValue : Any) : Boolean

    fun containsAttribute(element: BaseElement, attributeName: String) : Boolean
}