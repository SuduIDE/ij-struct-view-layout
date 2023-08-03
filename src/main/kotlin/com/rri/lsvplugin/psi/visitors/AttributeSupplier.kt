package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

class AttributeSupplier : IAttributeSupplier {
    override fun addAttribute(curElement: BaseElement, attributeName: String, attributeValue: Any): Boolean {
        if (containsUniqueElement(curElement, attributeName)) {
            curElement.getUniqueAttributes()[attributeName] = attributeValue
            return true
        } else if (curElement.getSetAttributes().containsKey(attributeName)) {
            if (curElement.getSetAttributes()[attributeName] == null)
                curElement.getSetAttributes()[attributeName] = mutableListOf(attributeValue)
            else
                (curElement.getSetAttributes()[attributeName] as MutableList<Any>).add(attributeValue)
            return true
        }
        return false
    }

    override fun containsUniqueElement(element: BaseElement, attributeName: String): Boolean {
        return element.getUniqueAttributes().containsKey(attributeName) && element.getUniqueAttributes()[attributeName] == null
    }

}