package com.rri.lsvplugin.services

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager.PostStartupActivity

class PostStartupService : PostStartupActivity() {
    override fun runActivity(project: Project) {
        project.service<JsonSvContainerServiceImpl>().loadCurrentVersion()
        super.runActivity(project)
    }
}