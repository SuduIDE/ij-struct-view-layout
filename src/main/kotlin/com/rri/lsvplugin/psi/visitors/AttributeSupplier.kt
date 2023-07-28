package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

class AttributeSupplier : IAttributeSupplier {
    override fun addAttribute(curElement: BaseElement, attributeName: String, attributeValue: Any): Boolean {
        if (curElement.getUniqueAttributes().containsKey(attributeName) && curElement.getUniqueAttributes()[attributeName] == null) {
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

}