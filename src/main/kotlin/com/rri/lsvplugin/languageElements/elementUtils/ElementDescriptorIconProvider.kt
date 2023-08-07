package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.ui.IconManager
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.utils.SvConstants
import javax.swing.Icon

class ElementDescriptorIconProvider {
    companion object {
        private val iconManager = IconManager.getInstance()

        fun getIcon(element: BaseElement): Icon? {
            val baseIcon = getBaseIcon(element) ?: return null
            return getOffsetIcons(element, getMarkIcons(element, baseIcon))
        }

        private fun getBaseIcon(element: BaseElement): Icon? {
            val attributeKey = element.baseIcon?.attributeKey
            if (element.baseIcon?.defaultIcon?.iconType == SvConstants.IconType.Base &&
                (attributeKey == null || !(element.getSetAttributes().containsKey(attributeKey)
                        || element.getUniqueAttributes().containsKey(attributeKey)))
            )
                return element.baseIcon?.defaultIcon?.loadedIcon
            else if (element.baseIcon?.alternativeIcon?.iconType == SvConstants.IconType.Base) {
                if (element.baseIcon?.attributeValue == null) {
                    return element.baseIcon?.defaultIcon?.loadedIcon
                } else {
                    var attributes: List<*>? = null
                    if (element.getSetAttributes().containsKey(attributeKey))
                        attributes = element.getSetAttributes()[attributeKey]
                    else if (element.getUniqueAttributes()[attributeKey] is List<*>)
                        attributes = element.getUniqueAttributes()[attributeKey] as List<*>

                    if (attributes != null) {
                        for (value in element.baseIcon?.attributeValue!!) {
                            if (attributes.find { it.toString() == value } != null)
                                return element.baseIcon?.alternativeIcon?.loadedIcon
                        }
                    } else if (element.baseIcon?.attributeValue!!.contains(element.getUniqueAttributes()[attributeKey].toString()))
                        return element.baseIcon?.alternativeIcon?.loadedIcon

                    return element.baseIcon?.defaultIcon?.loadedIcon
                }
            }

            return null
        }

        private fun getOffsetIcons(element: BaseElement, baseIcon: Icon): Icon {
            val offsetIconsArray = getIconByType(element, SvConstants.IconType.Offset).toTypedArray()
            return if (offsetIconsArray.isNotEmpty())
                iconManager.createRowIcon(baseIcon, *offsetIconsArray)
            else
                baseIcon
        }

        private fun getMarkIcons(element: BaseElement, baseIcon: Icon): Icon {
            val markIconArray = getIconByType(element, SvConstants.IconType.Mark).toTypedArray()
            return if (markIconArray.isNotEmpty())
                iconManager.createLayered(baseIcon, *markIconArray)
            else
                baseIcon
        }

        private fun getIconByType(element: BaseElement, iconType: SvConstants.IconType): List<Icon> {
            val iconList = mutableListOf<Icon>()
            for (setAttr in element.getSetAttributes().values) {
                if (setAttr is List<*>) {
                    getIconByTypeInList(setAttr, iconType, iconList)
                }
            }

            for (uniqueAttr in element.getUniqueAttributes().values) {
                if (uniqueAttr is List<*>)
                    getIconByTypeInList(uniqueAttr, iconType, iconList)
                else
                    addIconToList(uniqueAttr, iconType, iconList)
            }

            return iconList
        }

        private fun getIconByTypeInList(
            attrList: List<*>,
            iconType: SvConstants.IconType,
            iconList: MutableList<Icon>
        ) {
            for (el in attrList)
                addIconToList(el, iconType, iconList)
        }

        private fun addIconToList(attribute: Any?, iconType: SvConstants.IconType, iconList: MutableList<Icon>) {
            if (attribute is BaseElement.KeywordStructure && attribute.icon?.iconType == iconType && attribute.icon.loadedIcon != null)
                iconList.add(attribute.icon.loadedIcon!!)
        }


    }
}