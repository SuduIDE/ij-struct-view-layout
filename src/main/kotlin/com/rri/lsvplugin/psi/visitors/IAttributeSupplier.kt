package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

interface IAttributeSupplier {
    fun addAttribute(curElement : List<BaseElement>, attributeName : String, attributeValue : Any)

    fun containsAttribute(element: BaseElement, attributeName: String) : Boolean
}