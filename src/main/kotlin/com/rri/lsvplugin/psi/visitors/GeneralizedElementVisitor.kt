package com.rri.lsvplugin.psi.visitors

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.builders.BaseElementStructureBuilder
import com.rri.lsvplugin.languageElements.elements.*
import com.rri.lsvplugin.psi.JsonContainerUtil

class GeneralizedElementVisitor : IElementVisitor {
    override fun visitElement(element: BaseElement, builder: BaseElementStructureBuilder, jsonUtil: JsonContainerUtil) {
        generalizedVisit(element.getLangElement(), element, builder, jsonUtil)
    }

    fun generalizedVisit(
        psiElement: PsiElement,
        element: BaseElement,
        builder: BaseElementStructureBuilder,
        jsonUtil: JsonContainerUtil
    ) {
        builder.reset(element.elementStructure)
        for (child in psiElement.children) {
            val newElement = when (jsonUtil.getMainKeywords(child)) {
                "class" -> CallableClassElement(child)
                "method" -> MethodElement(child)
                "field" -> FieldElement(child)
                "parameter" -> ParameterElement(child)
                else -> null
            }
            if (newElement != null) {
                element.addChild(newElement)
                generalizedVisit(child, newElement, builder, jsonUtil)
            } else {
                when (jsonUtil.getSubKeywords(child)) {
                    "name" -> builder.setName(child.text)
                    "type" -> builder.setType(child.text)
                    "private" -> builder.addModifier(child.text)
                    "public" -> builder.addModifier(child.text)
                    "final" -> builder.addModifier(child.text)
                    "static" -> builder.addModifier(child.text)
                }
                element.setStructure(builder.getElementStructure())
                generalizedVisit(child, element, builder, jsonUtil)
            }
        }
    }
}