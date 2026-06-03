package com.jifelog.platform

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class SpringModulithTests {

    private val modules = ApplicationModules.of("com.jifelog.platform.core")

    @Test
    fun `domain modules follow Spring Modulith boundaries`() {
        modules.verify()
    }

    @Test
    fun `write documentation snippets`() {
        Documenter(modules)
            .writeModuleCanvases()
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml()
    }

    @Test
    fun `print modules`() {
        modules.forEach { module ->
            println("Base package: ${module.basePackage}")
            println("Named interfaces: ${module.namedInterfaces}")
            println()
        }
    }
}
