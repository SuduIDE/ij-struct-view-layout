package com.intership.lsvplugin

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import javax.swing.Icon

class PopupDialogAction (text: String?, description: String?, icon: Icon?) : AnAction(text, description, icon) {

    public constructor() : this(null, null, null){
    }

    override fun actionPerformed(e: AnActionEvent) {
        val currentProject = e.project
        val dlgMsg = StringBuilder(e.presentation.text + "Selected!")
        val dlgTitle = e.presentation.description
        val nav = e.getData(CommonDataKeys.NAVIGATABLE)

        if (nav != null) {
            dlgMsg.append(String.format("\n Selected Element: %s", nav.toString()))
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon())
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }

}