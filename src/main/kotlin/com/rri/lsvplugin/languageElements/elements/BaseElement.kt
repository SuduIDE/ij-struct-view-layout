package com.rri.lsvplugin.languageElements.elements

import com.intellij.psi.PsiElement

open class BaseElement(val langElement: PsiElement) {
    open var structure : MutableMap<String, Any> = mutableMapOf()
    
}