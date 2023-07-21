package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elements.*
import javax.swing.Icon

class ElementDescriptorIconProvider {
    companion object {
        private val iconManager = IconManager.getInstance()
        fun getIcon(element: BaseElement): Icon? {
            return null
        }

        private fun getVisibility(element: BaseElement): Icon {
//            if (element.getModifiersList()?.contains("public") == true)
//                return iconManager.getPlatformIcon(PlatformIcons.Public)
//            if (element.getModifiersList()?.contains("private") == true)
//                return iconManager.getPlatformIcon(PlatformIcons.Private)
//            if (element.getModifiersList()?.contains("protected") == true)
//                return iconManager.getPlatformIcon(PlatformIcons.Protected)

            return iconManager.getPlatformIcon(PlatformIcons.Local)
        }

        private fun getModifiers(baseIcon : Icon, element: BaseElement): Icon {
//            var icon: Icon = baseIcon
//            if (element.getModifiersList()?.contains("final") == true) {
//                icon = iconManager.createRowIcon(icon, iconManager.getPlatformIcon(PlatformIcons.FinalMark))
//            }
//            if (element.getModifiersList()?.contains("static") == true) {
//                val staticIcon =  iconManager.getPlatformIcon(PlatformIcons.StaticMark)
//                icon = iconManager.createRowIcon(icon, staticIcon)
//            }
//
//            if (element is ClassElement && element.isRunnable()) {
//                val runnableIcon = iconManager.getPlatformIcon(PlatformIcons.RunnableMark)
//                icon = iconManager.createRowIcon(icon, runnableIcon)
//            }

            return baseIcon
        }

    }
}