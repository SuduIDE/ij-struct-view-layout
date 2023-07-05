package com.intership.lsvplugin

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout

import javax.swing.*

class CustomTestExtensionsImpl : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val content = ContentFactory.getInstance().createContent(createPanel(), "", false)
        toolWindow.contentManager.addContent(content)
        val customService = project.service<CustomTestServiceImpl>()
        customService.printHelloMessage("Hi, bitch")
    }

    private fun createPanel(): JPanel {
        val contentPanel = JPanel()

        contentPanel.layout = BorderLayout(0, 10)
        contentPanel.border = BorderFactory.createEmptyBorder(10, 0, 0, 0)

        val helloWorld = JLabel("Hello, World!")
        contentPanel.add(helloWorld, BorderLayout.CENTER)

        return contentPanel
    }

}