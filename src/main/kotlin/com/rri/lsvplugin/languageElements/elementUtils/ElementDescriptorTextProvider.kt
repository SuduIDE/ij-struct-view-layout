package com.rri.lsvplugin.languageElements.elementUtils

import com.rri.lsvplugin.languageElements.elements.*

class ElementDescriptorTextProvider {
    companion object {
        fun getText(element: BaseElement) : String? {
            return when (element) {
                is FileElement -> getPresentableText(element)
                is ClassElement -> getPresentableText(element)
                is InterfaceElement -> getPresentableText(element)
                is FieldElement -> getPresentableText(element)
                is MethodElement -> getPresentableText(element)
                is FunctionElement -> getPresentableText(element)
                else -> null
            }
        }

        private fun getPresentableText(fileElement: FileElement): String? {
            return fileElement.getLangElement().containingFile.name
        }

        private fun getPresentableText(classElement: ClassElement): String? {
            return classElement.elementStructure.getName()
        }

        private fun getPresentableText(interfaceElement: InterfaceElement) : String? {
            return interfaceElement.elementStructure.getName()
        }

        private fun getPresentableText(fieldElement: FieldElement): String {
            val printableText = StringBuilder()
            printableText.append(fieldElement.elementStructure.getName()).append(": ")
                .append(fieldElement.elementStructure.getType())
            if (fieldElement.elementStructure.getValue() != null)
                printableText.append(" = ").append(fieldElement.elementStructure.getValue())

            return printableText.toString()
        }

        private fun getPresentableText(methodElement: FunctionBaseElement): String {
            val presentableText = StringBuilder()
            presentableText.append(methodElement.elementStructure.getName()).append("(")
            if (methodElement.elementStructure.getParameters() != null) {
                presentableText.append(methodElement.elementStructure.getParameters()!!.joinToString(separator = ", "))
            }
            presentableText.append("): ").append(methodElement.elementStructure.getType())

            return presentableText.toString()
        }
    }
}