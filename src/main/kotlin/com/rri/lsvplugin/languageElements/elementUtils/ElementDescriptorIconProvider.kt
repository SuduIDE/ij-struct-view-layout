package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elements.*
import javax.swing.Icon

class ElementDescriptorIconProvider {
    companion object {
        private val iconManager = IconManager.getInstance()

        fun getIcon(element: BaseElement) : Icon? {
            val baseIcon = getBaseIcon(element) ?: return null
            return iconManager.createRowIcon(getModifiersIcon(baseIcon, element), getVisibility(element))
        }
        private fun getBaseIcon(element : BaseElement) : Icon? {
            if (element.baseIcon == "default") {
                return when(element.typeElement) {
                    "class" -> {
                        if (getModifiers(element)?.contains("abstract") == true)
                            iconManager.getPlatformIcon(PlatformIcons.AbstractClass)
                        else
                            iconManager.getPlatformIcon(PlatformIcons.Class)
                    }
                    "interface" -> iconManager.getPlatformIcon(PlatformIcons.Interface)
                    "method" -> {
                        if (getModifiers(element)?.contains("abstract") == true)
                            iconManager.getPlatformIcon(PlatformIcons.AbstractMethod)
                        else
                            iconManager.getPlatformIcon(PlatformIcons.Method)
                    }
                    "field" -> iconManager.getPlatformIcon(PlatformIcons.Field)
                    "lambda" -> iconManager.getPlatformIcon(PlatformIcons.Lambda)
                    "property" -> iconManager.getPlatformIcon(PlatformIcons.Property)
                    "aclass" -> iconManager.getPlatformIcon(PlatformIcons.AnonymousClass)
                    else -> null
                }
            } else {
                return iconManager.getIcon(element.baseIcon!!, BaseElement::class.java)
            }
        }
        private fun getModifiers(element: BaseElement) : List<String>? {
            if (!element.structure.containsKey("modifiers"))
                return null

            return (element.structure["modifiers"] as List<String>)
        }
        private fun getVisibility(element: BaseElement): Icon {
            if (getModifiers(element)?.contains("public") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Public)
            if (getModifiers(element)?.contains("private") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Private)
            if (getModifiers(element)?.contains("protected") == true)
                return iconManager.getPlatformIcon(PlatformIcons.Protected)

            return iconManager.getPlatformIcon(PlatformIcons.Local)
        }

        private fun getModifiersIcon(baseIcon : Icon, element: BaseElement): Icon {
            var icon: Icon = baseIcon
            if (getModifiers(element)?.contains("final") == true) {
                icon = iconManager.createRowIcon(icon, iconManager.getPlatformIcon(PlatformIcons.FinalMark))
            }
            if (getModifiers(element)?.contains("static") == true) {
                val staticIcon =  iconManager.getPlatformIcon(PlatformIcons.StaticMark)
                icon = iconManager.createRowIcon(icon, staticIcon)
            }

//            if (element is ClassElement && element.isRunnable()) {
//                val runnableIcon = iconManager.getPlatformIcon(PlatformIcons.RunnableMark)
//                icon = iconManager.createRowIcon(icon, runnableIcon)
//            }

            return baseIcon
        }

    }
}