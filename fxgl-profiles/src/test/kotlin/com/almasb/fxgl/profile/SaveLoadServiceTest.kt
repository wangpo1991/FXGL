/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxgl.profile

import com.almasb.fxgl.core.serialization.Bundle
import com.almasb.fxgl.io.FileSystemService
import com.almasb.fxgl.test.InjectInTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SaveLoadServiceTest {

    private lateinit var saveLoadService: SaveLoadService

    companion object {
        @BeforeAll
        @JvmStatic fun before() {
            cleanUp()
        }

        @AfterAll
        @JvmStatic fun cleanUp() {

            // ensure previous tests have been cleared
            Paths.get("profiles/").toFile().deleteRecursively()

            assertTrue(!Files.exists(Paths.get("profiles/")), "Profiles dir is present before")
        }
    }

    @BeforeEach
    fun setUp() {
        saveLoadService = SaveLoadService()

        InjectInTest.inject(MethodHandles.lookup(), saveLoadService, "fs", FileSystemService().also { it.onInit() })
    }

    @Test
    fun `Test write - read`() {
        // we do a full clean before so it's important that
        // we keep the order, i.e. we save first so we have something to load from
        `Write game data`()
        `Read game data`()
        `Read file names`()
        `Delete game data`()
        //`Delete profile`()
    }

    fun `Write game data`() {
        val bundle1 = Bundle("Hello")
        bundle1.put("id", 9)

        val data1 = DataFile()
        data1.putBundle(bundle1)

        saveLoadService.writeTask("profiles/TestSave.sav", data1).run()

        assertTrue(saveLoadService.saveFileExists("profiles/TestSave.sav"))
    }

    fun `Read game data`() {
        val saveFile = saveLoadService.readTask("profiles/TestSave.sav").run()

        assertNotNull(saveFile)

        assertThat(saveFile.data.getBundle("Hello").get("id"), `is`(9))
    }

    fun `Read file names`() {
        saveLoadService.writeTask("profiles/TestSave2.sav", DataFile()).run()

        assertTrue(saveLoadService.saveFileExists("profiles/TestSave2.sav"))

        val saveFiles = saveLoadService.readSaveFilesTask("profiles", "sav").run()

        assertThat(saveFiles.size, `is`(2))
        assertThat(saveFiles[0].name, `is`("profiles/TestSave.sav"))
        assertThat(saveFiles[1].name, `is`("profiles/TestSave2.sav"))
    }

    fun `Delete game data`() {
        assertTrue(saveLoadService.saveFileExists("profiles/TestSave.sav"))

        saveLoadService.deleteSaveFileTask("profiles/TestSave.sav").run()

        assertFalse(saveLoadService.saveFileExists("profiles/TestSave.sav"))
    }
//
//    fun `Delete profile`() {
//        assertTrue(Files.exists(Paths.get("profiles/TestProfileName")))
//
//        saveLoadService.deleteProfileTask("TestProfileName").run()
//
//        assertFalse(Files.exists(Paths.get("profiles/TestProfileName")))
//    }
}