package com.rri.lsvplugin.services

import com.intellij.lang.Language
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewFactory
import com.rri.lsvplugin.utils.JsonInfo
import com.rri.lsvplugin.utils.JsonStructureSV
import com.rri.lsvplugin.utils.MapTypeSV
import com.rri.lsvplugin.utils.SvConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists

@Service(Service.Level.PROJECT)
class JsonSvContainerServiceImpl : JsonSvContainerService {
    private val jsonSV = JsonInfo()
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

    fun getFullPathToCustomSV(): File? {
        return ProjectManager.getInstance().openProjects[0].basePath?.let { File(it).resolve(jsonSV.filenameJsonSV) }
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
            if (Language.findLanguageByID(lang) != null)
                structureViewFactoryMap[lang] = CustomizedStructureViewFactory()
                LanguageStructureViewBuilder.INSTANCE.addExplicitExtension(
                    Language.findLanguageByID(lang)!!,
                    structureViewFactoryMap[lang]!!
                )
        }
    }

    private fun removeStructureViewForLang() {
        if (jsonSV.getMapSV() == null)
            return
        for (lang in jsonSV.getMapSV()?.keys!!) {
            if (Language.findLanguageByID(lang) != null && structureViewFactoryMap.contains(lang)) {
                LanguageStructureViewBuilder.INSTANCE.removeExplicitExtension(
                    Language.findLanguageByID(lang)!!,
                    structureViewFactoryMap[lang]!!
                )
                structureViewFactoryMap.remove(lang)
            }
        }
    }

}