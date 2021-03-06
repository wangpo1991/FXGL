/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxgl.tools.dialogues;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.tools.dialogues.DialogueEditorVars.*;

/**
 * A dialogue editor for FXGL.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class DialogueEditorApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setTitle("FXGL Dialogue Editor - github.com/AlmasB/FXGL");
        settings.setVersion("1.0");
        settings.getCSSList().add("dialogue_editor.css");
        settings.setIntroEnabled(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        settings.setCloseConfirmation(settings.getApplicationMode() == ApplicationMode.RELEASE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(IS_SNAP_TO_GRID, true);
        vars.put(IS_COLOR_BLIND_MODE, true);
    }

    @Override
    protected void initGame() {
        addUINode(new MainUI());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
