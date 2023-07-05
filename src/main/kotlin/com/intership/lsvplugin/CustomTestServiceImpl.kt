package com.intership.lsvplugin

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
final class CustomTestServiceImpl(private val project: Project) : CustomTestService {
    override fun printHelloMessage(param: String) {
        println("Hello, " + project.name + "\n Your args: " + param)
    }

}