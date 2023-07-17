package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.builders.BaseElementStructureBuilder
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.JsonContainerUtil

class JavaElementLangVisitor : IElementVisitor {
    lateinit var jsonUtil: JsonContainerUtil
    override fun visitElement(element: BaseElement, builder: BaseElementStructureBuilder, util: JsonContainerUtil) {
        jsonUtil = util

        builder.reset()
        for (child in element.getLangElement().children) {
            if (jsonUtil.isSubElement(child)) {
                when (jsonUtil.getSubKeywords(child)) {
                    "modifiers" -> builder.setModifiers(visitModifierList(child))
                    "parameters" -> builder.setParams(visitParameterList(child))
                    "name" -> builder.setName(child.text)
                    "type" -> builder.setType(child.text)
                }
            }
        }

        element.setStructure(builder.getElementStructure())
    }


    fun visitModifierList(psiElement: PsiElement): List<String> {
        val modifiers = ArrayList<String>()
        for (child in psiElement.children) {
            if (jsonUtil.isSubElement(child))
                modifiers.add(child.text)
        }
        return modifiers
    }

    fun visitParameterList(psiElement: PsiElement): List<String> {
        val parameters = ArrayList<String>()
        for (child in psiElement.children) {
            val par_type = when (jsonUtil.getSubKeywords(child)) {
                "parameter" -> visitParameter(child)
                else -> null
            }

            if (par_type != null)
                parameters.add(par_type)
        }
        return parameters
    }


    fun visitParameter(psiElement: PsiElement): String? {
        for (child in psiElement.children) {
            val parType = when (jsonUtil.getSubKeywords(child)) {
                "type" -> child.text
                else -> null
            }

            if (parType != null)
                return parType
        }
        return null
    }

}