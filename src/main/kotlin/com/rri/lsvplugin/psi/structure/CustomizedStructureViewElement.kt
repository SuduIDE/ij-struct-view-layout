package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.ui.Queryable
import com.intellij.platform.backend.navigation.NavigationRequest
import com.intellij.pom.Navigatable
import com.intellij.util.ArrayUtil
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.ViewCreator


class CustomizedStructureViewElement(
    private var element: BaseElement,
    private val creator: ViewCreator,
) : StructureViewTreeElement, Queryable {
    override fun getPresentation(): ItemPresentation {
       return element.getPresentableView()
    }

    override fun getChildren(): Array<CustomizedStructureViewElement> {
        val childrenElements = ArrayList<CustomizedStructureViewElement>()
        if (element.elementType == "file") {
            val prevVersionElement = element.clone()
            prevVersionElement.clear()
            creator.visitElement(prevVersionElement)
            if (prevVersionElement != element) {
                element = prevVersionElement
            }
            creator.adjustDisplayLevel(element)
        }

        element.children.forEach {childrenElements.add(CustomizedStructureViewElement(it, creator)) }
        return ArrayUtil.toObjectArray(childrenElements, CustomizedStructureViewElement::class.java)
    }

    override fun navigate(requestFocus: Boolean) = (element.langElement as Navigatable).navigate(requestFocus)

    override fun canNavigate(): Boolean = (element.langElement as Navigatable).canNavigate()

    override fun canNavigateToSource(): Boolean = (element.langElement as Navigatable).canNavigateToSource()

    override fun getValue(): BaseElement = element
    override fun putInfo(info: MutableMap<in String, in String>) {
        info["element"] = element.elementType!!
        info["text"] = element.presentableText.getText() +
                if (element.presentableText.getDescription() != "")  (" " + element.presentableText.getDescription()) else ""

    }

    override fun navigationRequest(): NavigationRequest? = (element.langElement as Navigatable).navigationRequest()

}