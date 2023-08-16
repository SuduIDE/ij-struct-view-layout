package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

class AttributeSupplier : IAttributeSupplier {
    override fun addAttribute(curElement: BaseElement, attributeName: String, attributeValue: Any): Boolean {
        if (containsUniqueAttribute(curElement, attributeName)) {
            curElement.uniqueAttributes[attributeName] = attributeValue
            return true
        } else if (containsOptionalAttribute(curElement, attributeName)) {
            curElement.optionalAttributes[attributeName] = attributeValue
            return true
        } else if (curElement.setAttributes.containsKey(attributeName)) {
            @Suppress("UNCHECKED_CAST")
            if (curElement.setAttributes[attributeName] == null)
                curElement.setAttributes[attributeName] = mutableListOf(attributeValue)
            else
                (curElement.setAttributes[attributeName] as MutableList<Any>).add(attributeValue)
            return true
        } else if  (containsExclusiveAttribute(curElement, attributeName)) {
            curElement.exclusiveAttributes[attributeName] = attributeValue
            return true
        }
        return false
    }

    private fun containsUniqueAttribute(element: BaseElement, attributeName: String): Boolean {
        return element.uniqueAttributes.containsKey(attributeName) && element.uniqueAttributes[attributeName] == null
    }

    private fun containsOptionalAttribute(element: BaseElement, attributeName: String) : Boolean {
        return element.optionalAttributes.containsKey(attributeName) && element.optionalAttributes[attributeName] == null
    }

    private fun containsExclusiveAttribute(element: BaseElement, attributeName: String) : Boolean {
        return element.exclusiveAttributes.containsKey(attributeName) && element.exclusiveAttributes[attributeName] == null
    }

    override fun containsAttribute(element: BaseElement, attributeName: String): Boolean {
        return containsUniqueAttribute(element, attributeName) || containsOptionalAttribute(element, attributeName)
    }
}