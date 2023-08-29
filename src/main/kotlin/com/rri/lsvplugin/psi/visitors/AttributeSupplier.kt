package com.rri.lsvplugin.psi.visitors

import com.rri.lsvplugin.languageElements.elements.BaseElement

class AttributeSupplier : IAttributeSupplier {
    override fun addAttribute(curElement: List<BaseElement>, attributeName: String, attributeValue: Any) {
        for (element in curElement) {
            if (containsUniqueAttribute(element, attributeName)) {
                element.uniqueAttributes[attributeName] = attributeValue
            } else if (containsOptionalAttribute(element, attributeName)) {
                element.optionalAttributes[attributeName] = attributeValue
            } else if (element.setAttributes.containsKey(attributeName)) {
                @Suppress("UNCHECKED_CAST")
                if (element.setAttributes[attributeName] == null)
                    element.setAttributes[attributeName] = mutableListOf(attributeValue)
                else
                    (element.setAttributes[attributeName] as MutableList<Any>).add(attributeValue)
            } else if (containsExclusiveAttribute(element, attributeName)) {
                element.exclusiveAttributes[attributeName] = attributeValue
            }
        }
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