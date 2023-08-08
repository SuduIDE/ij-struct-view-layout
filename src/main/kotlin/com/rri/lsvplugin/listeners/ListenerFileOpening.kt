package com.rri.lsvplugin.listeners

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile

class ListenerFileOpening : FileEditorManagerListener {
    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {

//        if (e.file?.fileType?.name == Language.findLanguageByID("JAVA")?.associatedFileType?.name) {
//            println("java sv ")
//            LanguageStructureViewBuilder.INSTANCE.addExplicitExtension(Language.findLanguageByID("JAVA")!!, CustomizedStructureViewFactory())
//        }
        super.fileOpened(source, file)
    }
}