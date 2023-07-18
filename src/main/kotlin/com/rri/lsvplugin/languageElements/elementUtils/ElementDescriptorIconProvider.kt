package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elements.*
import javax.swing.Icon

class ElementDescriptorIconProvider {
    companion object {
        private val iconManager = IconManager.getInstance()
        fun getIcon(element: BaseElement): Icon? {
            val baseIcon = when (element) {
                is FileElement -> getBaseIcon(element)
                is ClassElement -> getBaseIcon(element)
                is InterfaceElement -> getBaseIcon(element)
                is FieldElement -> getBaseIcon(element)
                is MethodElement -> getBaseIcon(element)
                is FunctionElement -> getBaseIcon(element)
                else -> null
            } ?: return null

            return iconManager.createRowIcon(getModifiers(baseIcon, element), getVisibility(element))
        }

        private fun getBaseIcon(fileElement: FileElement): Icon {
            return iconManager.getPlatformIcon(PlatformIcons.CustomFileType)
        }

        private fun getBaseIcon(classElement: ClassElement): Icon {
            if (classElement.getModifiersList()?.contains("abstract") == true)
                return iconManager.getPlatformIcon(PlatformIcons.AbstractClass)

            return iconManager.getPlatformIcon(PlatformIcons.Class)
        }

        private fun getBaseIcon(interfaceElement: InterfaceElement): Icon {
            return iconManager.getPlatformIcon(PlatformIcons.Interface)
        }

        private fun getBaseIcon(methodElement: MethodElement): Icon {
            if (methodElement.getModifiersList()?.contains("abstract") == true)
                return iconManager.getPlatformIcon(PlatformIcons.AbstractMethod)

            return iconManager.getPlatformIcon(PlatformIcons.Method)
        }

        private fun getBaseIcon(fieldElement: FieldElement): Icon {
            return iconManager.getPlatformIcon(PlatformIcons.Field)
        }

        private fun getBaseIcon(functionElement: FunctionElement): Icon {
            return iconManager.getPlatformIcon(PlatformIcons.Function)
        }

        private fun getVisibility(element: BaseElement): Icon {
            if (element.getModifiersList()?.contains("public") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Public)
            if (element.getModifiersList()?.contains("private") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Private)
            if (element.getModifiersList()?.contains("protected") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Protected)

            return iconManager.getPlatformIcon(PlatformIcons.Local)
        }

        private fun getModifiers(baseIcon : Icon, element: BaseElement): Icon {
            var icon: Icon = baseIcon
            if (element.getModifiersList()?.contains("final") == true) {
                icon = iconManager.createLayered(icon, iconManager.getPlatformIcon(PlatformIcons.FinalMark))
            }
            if (element.getModifiersList()?.contains("static") == true) {
                val staticIcon =  iconManager.getPlatformIcon(PlatformIcons.StaticMark)
                icon = iconManager.createLayered(icon, staticIcon)
            }

            if (element is ClassElement && element.isRunnable()) {
                val runnableIcon = iconManager.getPlatformIcon(PlatformIcons.RunnableMark)
                icon = iconManager.createLayered(icon, runnableIcon)
            }

            return icon
        }

    }
}