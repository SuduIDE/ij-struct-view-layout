package com.rri.lsvplugin.languageElements.elements

import com.rri.lsvplugin.utils.JsonStructureSV

open class Attributes(
    var setAttributes: MutableMap<String, MutableList<*>?> = mutableMapOf(),
    var uniqueAttributes: MutableMap<String, Any?> = mutableMapOf(),
    var optionalAttributes: MutableMap<String, Any?> = mutableMapOf(),
) {
    data class KeywordStructure(
        val id: String,
        val text: String,
        val sortValue: Int?,
        val icon: JsonStructureSV.IconInfo?
    ) {
        override fun toString(): String {
            return if (text.isEmpty()) id else text
        }
    }

    data class DefaultAttributes(
        val parent: Map<String, List<KeywordStructure>>?,
        val children: Map<String, List<KeywordStructure>>?
    )

    var defaultAttributes: DefaultAttributes? = null

    /**
     * Return list of keywords for all type default attributes
     * @return List of keywords for parent and children default attributes
     */
    open fun getDefaultAttributes(): List<KeywordStructure>? = null

    fun getAttribute(attributeName: String): Any? {
        if (setAttributes[attributeName] is List<*>)
            return setAttributes[attributeName] as List<*>
        else if (uniqueAttributes[attributeName] is List<*>)
            return uniqueAttributes[attributeName] as List<*>
        else if (uniqueAttributes.containsKey(attributeName))
            return uniqueAttributes[attributeName]
        else if (optionalAttributes.containsKey(attributeName))
            return optionalAttributes[attributeName]

        return getDefaultAttributes()?.firstOrNull { it.toString() == attributeName }
    }

    fun isOptional(attributeName: String) : Boolean = optionalAttributes.containsKey(attributeName)

    override fun equals(other: Any?): Boolean {
        if (other !is Attributes)
            return false
        return other.uniqueAttributes == uniqueAttributes && other.setAttributes == setAttributes && other.optionalAttributes == optionalAttributes
    }

    fun clone(other: Attributes) {
        other.uniqueAttributes = uniqueAttributes
        other.setAttributes = setAttributes
        other.optionalAttributes = optionalAttributes
        other.defaultAttributes = defaultAttributes
    }


}