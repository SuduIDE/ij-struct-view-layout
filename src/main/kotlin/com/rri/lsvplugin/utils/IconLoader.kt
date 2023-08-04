package com.rri.lsvplugin.utils

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.intellij.util.alsoIfNull
import java.nio.file.Path
import javax.swing.Icon
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.staticProperties
import kotlin.reflect.full.valueParameters

class IconLoader {
    companion object {
        private val iconMap : Map<String, Icon> = fillIconMap()

        fun getIcon(iconStr: String) : Icon? {
            return PlatformIcons.values().firstOrNull { it.name == iconStr }?.let { IconManager.getInstance().getPlatformIcon(it) } ?:
            iconMap[iconStr] ?: IconLoader.findIcon(Path.of(iconStr).toUri().toURL(), true)
        }


        private fun fillIconMap() : Map<String, Icon> {
            val iconMap = mutableMapOf<String, Icon>()
            for (group in AllIcons::class.nestedClasses) {
                for (icon in group.staticProperties) {
                    iconMap[group.simpleName + "." + icon.name] = icon.get() as Icon
                }
            }

            return iconMap
        }

    }
}