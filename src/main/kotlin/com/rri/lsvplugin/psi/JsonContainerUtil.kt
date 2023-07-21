package com.rri.lsvplugin.psi

import com.intellij.lang.Language
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl

class JsonContainerUtil {
    fun getLangElementsMap(langElement: PsiElement) = langElement.project.service<JsonSvContainerServiceImpl>().getMapSV()

    fun isBelongLang(langElement: PsiElement): Boolean {
        return getLangElementsMap(langElement)
        ?.containsKey(Language.findLanguageByID(langElement.language.id).toString()) == true
    }
//
//    fun isMainElement(langElement: PsiElement): Boolean {
//        if (!isBelongLang(langElement))
//            return false
//
//        val keywordsList = getLangElementsMap(langElement)[langElement.language.displayName]
//
//        return keywordsList?.get("element")?.containsValue(langElement.elementType.toString()) ?: false
//    }
//
//    fun isSubElement(langElement: PsiElement): Boolean {
//        if (!isBelongLang(langElement))
//            return false
//
//        val keywordsList = getLangElementsMap(langElement)[langElement.language.displayName]
//        return keywordsList?.get("attribute")?.containsValue(langElement.elementType.toString()) ?: false
//    }
//
//    fun getMainKeywords(langElement: PsiElement): String? {
//        if (!isMainElement(langElement))
//            return null
//
//        val mainKeywords = getLangElementsMap(langElement)[langElement.language.displayName]?.get("element")
//        return mainKeywords?.filterValues { it == langElement.elementType.toString() }?.keys?.first()
//    }
//
//    fun getSubKeywords(langElement: PsiElement): String? {
//        if (!isSubElement(langElement))
//            return null
//
//        val subKeywords = getLangElementsMap(langElement)[langElement.language.displayName]?.get("attribute")
//        return subKeywords?.filterValues { it == langElement.elementType.toString() }?.keys?.first()
//    }
}