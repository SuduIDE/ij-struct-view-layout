package com.rri.lsvplugin.services

import com.intellij.lang.Language
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.ProjectManager
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

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

    fun getFullPathToCustomSV(): File? =
        ProjectManager.getInstance().openProjects[0].basePath?.let { File(it).resolve(jsonSV.filenameJsonSV) }

    fun isCustomSvExist(): Boolean {
        val fullPath = getFullPathToCustomSV()
        return if (fullPath != null) fullPath.exists() && !fullPath.isDirectory else false
    }

    fun loadCurrentVersion() {
        removeStructureViewForLang()
        if (!isCustomSvExist()) {
            setJsonSV(null)
            return
        }

        val fullPathToJsonSV = getFullPathToCustomSV()

        val updatedJsonSV = fullPathToJsonSV?.readText()?.let {
            @Suppress("UNCHECKED_CAST")
            val tmpUpdatedJsonSV = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(Any::class.java)
                .fromJson(it) as MapTypeSV
            for (langugage in tmpUpdatedJsonSV.values) {
                val newElements = mutableMapOf<String, JsonStructureSV.ElementInfo>()
                if (langugage["element"]?.entries != null)
                    for ((key, value) in langugage["element"]?.entries!!) {
                        newElements[key] = JsonStructureSV.ElementInfo.fromJsonToElementInfo(value as Map<String, Any>)
                    }
                langugage["element"] = newElements
            }
            tmpUpdatedJsonSV
        }
        setJsonSV(updatedJsonSV)

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