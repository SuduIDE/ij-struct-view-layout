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
                return when(element.elementType) {
                    "class" -> {
                        if (getModifiers(element)?.contains("abstract") == true)
                            DefaultIconContainer.getIcon("abstractClass")
                        else
                            DefaultIconContainer.getIcon("class")
                    }
                    "interface" -> iconManager.getPlatformIcon(PlatformIcons.Interface)
                    "method" -> {
                        if (getModifiers(element)?.contains("abstract") == true)
                            DefaultIconContainer.getIcon("abstractMethod")
                        else
                            DefaultIconContainer.getIcon("method")
                    }
                    null -> null
                    else -> DefaultIconContainer.getIcon(element.elementType!!)
                }
            } else {
                return iconManager.getIcon(element.baseIcon!!, BaseElement::class.java)
            }
        }
        private fun getModifiers(element: BaseElement) : List<String>? {
            if (!element.getUniqueAttributes().containsKey("modifiers"))
                return null

            return (element.getUniqueAttributes()["modifiers"] as List<String>)
        }
        private fun getVisibility(element: BaseElement): Icon? {
            if (getModifiers(element)?.contains("public") == true)
                return DefaultIconContainer.getIcon("public")!!
            if (getModifiers(element)?.contains("private") == true)
                return DefaultIconContainer.getIcon("private")!!
            if (getModifiers(element)?.contains("protected") == true)
                return DefaultIconContainer.getIcon("protected")!!
            if (getModifiers(element) != null)
                return DefaultIconContainer.getIcon("local")

            return null
        }

        private fun getModifiersIcon(baseIcon : Icon, element: BaseElement): Icon {
            var icon: Icon = baseIcon
            if (getModifiers(element)?.contains("final") == true) {
                icon = iconManager.createLayered(icon, DefaultIconContainer.getIcon("final")!!)
            }
            if (getModifiers(element)?.contains("static") == true) {
                val staticIcon =  DefaultIconContainer.getIcon("static")!!
                icon = iconManager.createLayered(icon, staticIcon)
            }

//            if (element is ClassElement && element.isRunnable()) {
//                val runnableIcon = iconManager.getPlatformIcon(PlatformIcons.RunnableMark)
//                icon = iconManager.createRowIcon(icon, runnableIcon)
//            }

            return icon
        }

    }
}