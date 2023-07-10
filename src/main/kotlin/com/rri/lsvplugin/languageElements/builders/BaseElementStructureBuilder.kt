package com.rri.lsvplugin.languageElements.builders

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elementUtils.ElementStructure

interface BaseElementStructureBuilder {

    fun reset(otherStructure: ElementStructure = ElementStructure())
    fun setName(name: String)
    fun setType(type: String)

    fun setInitializedValue(value: String)

    fun setParams(args: List<String>)

    fun setModifiers(modifiers: List<String>)

    fun addModifier(modifier: String)

    fun getElementStructure() : ElementStructure
}