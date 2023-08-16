package com.rri.lsvplugin.utils

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.jetbrains.rd.util.firstOrNull
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl

class JsonContainerUtil {

    private fun getLanguage(langElement: PsiElement) = langElement.language.id.lowercase()

    private fun getLangElementsMap(langElement: PsiElement) =
        langElement.project.service<JsonSvContainerServiceImpl>().getMapSV()

    private fun getLanguageStructure(langElement: PsiElement) : Map<String, Map<String, Any>>? {
        val languageStructures = getLangElementsMap(langElement)
        if (languageStructures != null) {
            for (languageStructure in languageStructures) {
                val languagesList = (languageStructure as Map<String, Map<String, Any>>)[SvConstants.SETTINGS]?.get(SvConstants.LANGUAGES) as? List<String>
                if (languagesList?.contains(getLanguage(langElement)) == true)
                    return languageStructure
            }
        }
        return null
    }

    fun isELement(langElement: PsiElement): Boolean {
        val languageStructure = getLanguageStructure(langElement) ?: return false
        return languageStructure[SvConstants.ELEMENTS]?.filterValues { (it as JsonStructureSV.ElementInfo).baseToken == langElement.elementType.toString() }
            ?.isNotEmpty() ?: false
    }

    private fun isAttribute(langElement: PsiElement): Boolean {
        return isListAttribute(langElement) || isKeywordAttribute(langElement)
    }

    private fun isListAttribute(langElement: PsiElement): Boolean {
        val languageStructure = getLanguageStructure(langElement) ?: return false

        return (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.LISTS) as? MutableMap<*, *>)?.containsValue(
            langElement.elementType.toString()
        ) ?: false
    }

    private fun isKeywordAttribute(langElement: PsiElement): Boolean {
        val languageStructure =  getLanguageStructure(langElement) ?: return false

        return (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.KEYWORDS) as? List<JsonStructureSV.KeywordInfo>)?.any {
            it.token == langElement.elementType.toString()
        } ?: false
    }

    private fun isPropertyAttribute(langElement: PsiElement): Boolean {
        val languageStructure = getLanguageStructure(langElement) ?: return false

        @Suppress("UNCHECKED_CAST")
        return (languageStructure[SvConstants.ATTRIBUTES]?.get(SvConstants.PROPERTIES) as? List<JsonStructureSV.PropertyInfo>)?.any {
            it.token == langElement.elementType.toString()
        } ?: false
    }

    fun getElementNames(langElement: PsiElement): Set<String>? {
        if (!isELement(langElement))
            return null

        return getLanguageStructure(langElement)!![SvConstants.ELEMENTS]!!
            .filterValues { (it as JsonStructureSV.ElementInfo).baseToken == langElement.elementType.toString() }.keys
    }

    fun getElementByName(langElement: PsiElement, name: String): JsonStructureSV.ElementInfo {
        return getLanguageStructure(langElement)!![SvConstants.ELEMENTS]!![name] as JsonStructureSV.ElementInfo
    }

    fun getListAttribute(langElement: PsiElement): String? {
        if (!isListAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)!![SvConstants.ATTRIBUTES]!![SvConstants.LISTS]!! as MutableMap<String, *>)
            .filterValues { it == langElement.elementType.toString() }.keys.elementAt(0)
    }

    fun getKeywordAttribute(langElement: PsiElement): JsonStructureSV.KeywordInfo? {
        if (!isKeywordAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)!![SvConstants.ATTRIBUTES]!![SvConstants.KEYWORDS]!! as List<JsonStructureSV.KeywordInfo>)
            .firstOrNull { it.token == langElement.elementType.toString() }
    }

    fun getKeywordAttributeByName(keywordName: String, langElement: PsiElement) : JsonStructureSV.KeywordInfo? {
        val languageStructure = getLanguageStructure(langElement)?: return null
        @Suppress("UNCHECKED_CAST")
        return (languageStructure[SvConstants.ATTRIBUTES]!![SvConstants.KEYWORDS]!! as List<JsonStructureSV.KeywordInfo>)
            .firstOrNull { it.id == keywordName }
    }

    fun getPropertyAttribute(langElement: PsiElement): List<JsonStructureSV.PropertyInfo>? {
        if (!isPropertyAttribute(langElement))
            return null

        @Suppress("UNCHECKED_CAST")
        val keywordList = (getLanguageStructure(langElement)!![SvConstants.ATTRIBUTES]!![SvConstants.PROPERTIES]!! as List<JsonStructureSV.PropertyInfo>)
            .filter { it.token == langElement.elementType.toString() }
        if (keywordList.isEmpty())
            return null

        return keywordList
    }

    fun getVisibilityFilters(langElement: PsiElement): Map<String, JsonStructureSV.VisibilityFilterInfo>? {
        @Suppress("UNCHECKED_CAST")
        return getLanguageStructure(langElement)?.get(SvConstants.FILTERS)
            ?.get(SvConstants.VISIBILITY_FILTERS) as? Map<String, JsonStructureSV.VisibilityFilterInfo>
    }

    fun getSortingFilters(langElement: PsiElement): Map<String, JsonStructureSV.SortingFilterInfo>? {
        @Suppress("UNCHECKED_CAST")
        return getLanguageStructure(langElement)?.get(SvConstants.FILTERS)
            ?.get(SvConstants.SORTING_FILTERS) as? Map<String, JsonStructureSV.SortingFilterInfo>
    }

    fun getIconInfo(langElement: PsiElement, baseIconName: String?): JsonStructureSV.IconInfo? {
        return (getLanguageStructure(langElement)?.get(SvConstants.ATTRIBUTES)
            ?.get(SvConstants.ICONS) as List<JsonStructureSV.IconInfo>).firstOrNull { it.id == baseIconName }
    }
}