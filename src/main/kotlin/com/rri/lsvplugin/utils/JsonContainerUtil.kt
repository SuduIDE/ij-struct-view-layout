package com.rri.lsvplugin.utils

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl

class JsonContainerUtil {

    private fun getLanguage(langElement: PsiElement) = langElement.language.id

    private fun getLangElementsMap(langElement: PsiElement) =
        langElement.project.service<JsonSvContainerServiceImpl>().getMapSV()

    fun isELement(langElement: PsiElement): Boolean {
        val keywordsList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false
        return keywordsList[SvConstants.ELEMENTS]?.filterValues { (it as JsonStructureSV.ElementInfo).baseToken == langElement.elementType.toString() }
            ?.isNotEmpty() ?: false
    }

    private fun isAttribute(langElement: PsiElement): Boolean {
        return isListAttribute(langElement) || isKeywordAttribute(langElement)
    }

    private fun isListAttribute(langElement: PsiElement): Boolean {
        val attributeList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false

        return (attributeList[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS) as? MutableMap<*, *>)?.containsValue(
            langElement.elementType.toString()
        ) ?: false
    }

    private fun isKeywordAttribute(langElement: PsiElement): Boolean {
        val attributeList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false

        return (attributeList[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) as? MutableMap<*, *>)?.containsValue(
            langElement.elementType.toString()
        ) ?: false
    }

    private fun isPropertyAttribute(langElement: PsiElement): Boolean {
        val attributeList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false

        @Suppress("UNCHECKED_CAST")
        return (attributeList[SvConstants.ATTRIBUTES]?.get(SvConstants.PROPERTIES) as? List<JsonStructureSV.PropertyInfo>)?.any {
            it.token == langElement.elementType.toString()
        } ?: false
    }

    fun getElementNames(langElement: PsiElement): Set<String>? {
        if (!isELement(langElement))
            return null

        return getLangElementsMap(langElement)!![getLanguage(langElement)]!![SvConstants.ELEMENTS]!!
            .filterValues { (it as JsonStructureSV.ElementInfo).baseToken == langElement.elementType.toString() }.keys
    }

    fun getElementByName(langElement: PsiElement, name: String): JsonStructureSV.ElementInfo {
        return getLangElementsMap(langElement)!![getLanguage(langElement)]!![SvConstants.ELEMENTS]!![name] as JsonStructureSV.ElementInfo
    }

    fun getListAttribute(langElement: PsiElement): String? {
        if (!isListAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        return (getLangElementsMap(langElement)!![getLanguage(langElement)]!![SvConstants.ATTRIBUTES]!![SvConstants.LISTS]!! as MutableMap<String, *>)
            .filterValues { it == langElement.elementType.toString() }.keys.elementAt(0)
    }

    fun getKeywordAttribute(langElement: PsiElement): String? {
        if (!isKeywordAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        return (getLangElementsMap(langElement)!![getLanguage(langElement)]!![SvConstants.ATTRIBUTES]!![SvConstants.KEYWORDS]!! as MutableMap<String, *>)
            .filterValues { it == langElement.elementType.toString() }.keys.elementAt(0)
    }

    fun getPropertyAttribute(langElement: PsiElement): JsonStructureSV.PropertyInfo? {
        if (!isPropertyAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        return (getLangElementsMap(langElement)!![getLanguage(langElement)]!![SvConstants.ATTRIBUTES]!![SvConstants.PROPERTIES]!! as List<JsonStructureSV.PropertyInfo>)
            .firstOrNull { it.token == langElement.elementType.toString() && (it.regexp == null || Regex(it.regexp).matches(langElement.text)) }
    }

    fun getVisibilityFilters(langElement: PsiElement): Map<String, JsonStructureSV.VisibilityFilterInfo>? {
        @Suppress("UNCHECKED_CAST")
        return getLangElementsMap(langElement)?.get(getLanguage(langElement))?.get(SvConstants.FILTERS)
            ?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, JsonStructureSV.VisibilityFilterInfo>
    }
}