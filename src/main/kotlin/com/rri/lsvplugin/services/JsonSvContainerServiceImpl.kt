package com.rri.lsvplugin.services

import com.intellij.ide.impl.StructureViewWrapperImpl
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.structureView.impl.StructureViewFactoryImpl
import com.intellij.ide.structureView.newStructureView.StructureViewComponent
import com.intellij.ide.util.StructureViewCompositeModel
import com.intellij.lang.Language
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import com.intellij.testFramework.utils.vfs.getPsiFile
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewFactory
import com.rri.lsvplugin.utils.JsonInfo
import com.rri.lsvplugin.utils.LanguageUtil
import com.rri.lsvplugin.utils.MapTypeSV
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists

@Service(Service.Level.PROJECT)
class JsonSvContainerServiceImpl(private val project: Project) : JsonSvContainerService {
    private val jsonSV = JsonInfo(project)
    private val structureViewFactoryMap = mutableMapOf<String, CustomizedStructureViewFactory>()
    override fun setJsonSV(mapSV: MapTypeSV?) {
        if (mapSV != null)
            jsonSV.setMapSV(mapSV)
    }

    override fun reset() {
        jsonSV.reset()
        addStructureViewForLang()
    }

    override fun getJsonSV(): String = jsonSV.getJsonSV()
    override fun getMapSV(): MapTypeSV? = jsonSV.getMapSV()

    override fun getFilename(): File = jsonSV.filenameJsonSV

    fun getFactoryMap() = structureViewFactoryMap

    fun getDefaultJsonSV(): String = jsonSV.getDefaultVersionJson() ?: ""

    fun getFullPathToCustomSV(): File? {
        return project.basePath?.let { File(it).resolve(jsonSV.filenameJsonSV) }
    }

    fun isCustomSvExist(): Boolean {
        val fullPath = getFullPathToCustomSV()
        return if (fullPath != null) fullPath.exists() && !fullPath.isDirectory else false
    }

    fun loadCurrentVersion() {
        removeStructureViewForLang()
        ////
        if (!isCustomSvExist()) {
            setJsonSV(null)
            return
        }

        val fullPathToJsonSV = getFullPathToCustomSV()
        jsonSV.loadAndConvertJson(fullPathToJsonSV)
        ////
        addStructureViewForLang()
    }

    @TestOnly
    fun loadCurrentVersion(pathJsonStructureSV: Path) {
        removeStructureViewForLang()
        ////
        if (!pathJsonStructureSV.exists()) {
            setJsonSV(null)
            return
        }
        jsonSV.loadAndConvertJson(pathJsonStructureSV.toFile())
        ////
        addStructureViewForLang()
    }

    private fun addStructureViewForLang() {
        for (lang in jsonSV.getMapSV()?.keys!!) {
            val langId = LanguageUtil.getLanguageIdByLowercaseName(lang)
            if (Language.findLanguageByID(langId) != null) {
                StructureViewComponent.clearStructureViewState(project)
                structureViewFactoryMap[lang] = CustomizedStructureViewFactory()
                LanguageStructureViewBuilder.INSTANCE.addExplicitExtension(
                    Language.findLanguageByID(langId)!!,
                    structureViewFactoryMap[lang]!!
                )
            }
        }
    }

    private fun removeStructureViewForLang() {
        ProjectRootManager.getInstance(project).contentRoots.forEach { it.refresh(true, true) }
        if (jsonSV.getMapSV() == null)
            return
        for (lang in jsonSV.getMapSV()?.keys!!) {
            val langId = LanguageUtil.getLanguageIdByLowercaseName(lang)
            if (Language.findLanguageByID(langId) != null && structureViewFactoryMap.contains(lang)) {
                LanguageStructureViewBuilder.INSTANCE.removeExplicitExtension(
                    Language.findLanguageByID(langId)!!,
                    structureViewFactoryMap[lang]!!
                )
                structureViewFactoryMap.remove(lang)
            }
        }
    }

}