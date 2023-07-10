package com.rri.lsvplugin.languageElements.factory

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.*

class ElementFactory : IElementFactory {
    override fun createClass(langElement: PsiElement): ClassBaseElement = CallableClassElement(langElement)

    override fun createAClass(langElement: PsiElement): ClassBaseElement = AnonymousClassElement(langElement)

    override fun createFunction(langElement: PsiElement): FunctionBaseElement = FunctionElement(langElement)

    override fun createMethod(langElement: PsiElement): FunctionBaseElement = MethodElement(langElement)

    override fun createLambda(langElement: PsiElement): LambdaElement = LambdaElement(langElement)

    override fun createField(langElement: PsiElement): FieldElement = FieldElement(langElement)
}