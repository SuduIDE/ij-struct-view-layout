package com.rri.lsvplugin.psi

import com.intellij.lang.Language
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.rri.lsvplugin.services.JsonInfo
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl

class JsonContainerUtil {

    private fun getLanguage(langElement: PsiElement) = langElement.language.id

    private fun getLangElementsMap(langElement: PsiElement) =
        langElement.project.service<JsonSvContainerServiceImpl>().getMapSV()

    fun isELement(langElement: PsiElement): Boolean {
        val keywordsList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false
        return keywordsList["element"]?.filterValues { (it as JsonInfo.ElementInfo).baseToken == langElement.elementType.toString() }
            ?.isNotEmpty() ?: false
    }

    private fun isAttribute(langElement: PsiElement): Boolean {
        return isListAttribute(langElement) || isKeywordAttribute(langElement)
    }


    private fun isListAttribute(langElement: PsiElement): Boolean {
        val attributeList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false

        return (attributeList["attribute"]?.get("list") as MutableMap<*, *>).containsValue(langElement.elementType.toString())
    }

    fun isKeywordAttribute(langElement: PsiElement): Boolean {
        val attributeList = getLangElementsMap(langElement)?.get(getLanguage(langElement)) ?: return false

        return (attributeList["attribute"]?.get("keywords") as MutableMap<*, *>).containsValue(langElement.elementType.toString())
    }

    fun getElementNames(langElement: PsiElement): Set<String>? {
        if (!isELement(langElement))
            return null

        return getLangElementsMap(langElement)!![getLanguage(langElement)]!!["element"]!!
            .filterValues { (it as JsonInfo.ElementInfo).baseToken == langElement.elementType.toString() }.keys
    }

    fun getElementByName(langElement: PsiElement, name: String): JsonInfo.ElementInfo {
        return getLangElementsMap(langElement)!![getLanguage(langElement)]!!["element"]!![name] as JsonInfo.ElementInfo
    }

    fun getListAttrubute(langElement: PsiElement): String? {
        if (!isListAttribute(langElement))
            return null

        return (getLangElementsMap(langElement)!![getLanguage(langElement)]!!["attribute"]!!["list"]!! as MutableMap<String, *>)
            .filterValues { it == langElement.elementType.toString() }.keys.elementAt(0)
    }

    fun getKeywordAttribute(langElement: PsiElement): String? {
        if (!isKeywordAttribute(langElement))
            return null

        return (getLangElementsMap(langElement)!![getLanguage(langElement)]!!["attribute"]!!["keywords"]!! as MutableMap<String, *>)
            .filterValues { it == langElement.elementType.toString() }.keys.elementAt(0)
    }
}