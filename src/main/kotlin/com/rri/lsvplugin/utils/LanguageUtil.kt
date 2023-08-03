package com.rri.lsvplugin.utils

import com.intellij.lang.Language

class LanguageUtil {
    companion object {
        private val languageMap = createMapLanguageIdToLowerCase()
        private fun createMapLanguageIdToLowerCase() : Map<String, String> {
            return Language.getRegisteredLanguages().associate { it.id.lowercase() to it.id }
        }

        fun getLanguageIdByLowercaseName(languageName: String) = languageMap[languageName]
    }
}