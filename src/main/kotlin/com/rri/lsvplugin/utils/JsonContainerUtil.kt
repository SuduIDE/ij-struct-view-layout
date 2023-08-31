package com.rri.lsvplugin.utils

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl

class JsonContainerUtil {
    companion object {
        val projectServicesMap = mutableMapOf<Project, JsonSvContainerServiceImpl>()
    }

    private fun getLanguage(langElement: PsiElement) = langElement.language.id.lowercase()

    private fun getLangElementsMap(langElement: PsiElement) : MapTypeSV? {
        if (!projectServicesMap.containsKey(langElement.project))
            projectServicesMap[langElement.project] = langElement.project.service<JsonSvContainerServiceImpl>()

        return projectServicesMap[langElement.project]!!.getMapSV()
    }

    private fun getLanguageStructure(langElement: PsiElement): LanguageStructureType? {
        val languageStructures = getLangElementsMap(langElement)
        return (languageStructures as MapTypeSV)[getLanguage(langElement)]
    }

    fun getElementNames(langElement: PsiElement): List<JsonStructureSV.ElementInfo>? {
        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)?.get(SvConstants.ELEMENTS)?.get(langElement.elementType.toString()) as? List<JsonStructureSV.ElementInfo>)
    }

    fun getListAttribute(langElement: PsiElement): String? {
        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)?.get(SvConstants.ATTRIBUTES)?.get(SvConstants.LISTS) as? Map<String, String>)
            ?.entries?.firstOrNull { listToken ->  listToken.value == langElement.elementType.toString() }?.key
    }

    fun getKeywordAttribute(langElement: PsiElement): JsonStructureSV.KeywordInfo? {
        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)?.get(SvConstants.ATTRIBUTES)?.get(SvConstants.KEYWORDS) as? Map<String, JsonStructureSV.KeywordInfo>)?.get(langElement.elementType.toString())
    }

    fun getKeywordAttributeByName(keywordName: String, langElement: PsiElement): JsonStructureSV.KeywordInfo? {
        val languageStructure = getLanguageStructure(langElement) ?: return null
        @Suppress("UNCHECKED_CAST")
        return (languageStructure[SvConstants.ATTRIBUTES]!![SvConstants.KEYWORDS]!! as Map<String, JsonStructureSV.KeywordInfo>).values
            .firstOrNull { it.id == keywordName }
    }

    fun getPropertyAttribute(langElement: PsiElement): List<JsonStructureSV.PropertyInfo>? {
        @Suppress("UNCHECKED_CAST")
        val keywordList =
            (getLanguageStructure(langElement)?.get(SvConstants.ATTRIBUTES)?.get(SvConstants.PROPERTIES) as? Map<String, List<JsonStructureSV.PropertyInfo>>)
                ?.get(langElement.elementType.toString())
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
        @Suppress("UNCHECKED_CAST")
        return (getLanguageStructure(langElement)?.get(SvConstants.ATTRIBUTES)
            ?.get(SvConstants.ICONS) as Map<String, JsonStructureSV.IconInfo>)[baseIconName]
    }

    fun isShowFile(file: PsiElement): Boolean {
        return (getLanguageStructure(file)?.get(SvConstants.SETTINGS)?.get("showFile") as? Boolean) ?: false
    }
}