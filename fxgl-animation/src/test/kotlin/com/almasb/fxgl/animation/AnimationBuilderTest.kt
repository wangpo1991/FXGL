/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */
@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")
package com.almasb.fxgl.animation

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.scene.Scene
import javafx.animation.Interpolator
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class AnimationBuilderTest {

    private lateinit var scene: Scene
    private lateinit var builder: AnimationBuilder
    private lateinit var e: Entity
    private lateinit var node: Node

    @BeforeEach
    fun setUp() {
        scene = object : Scene() {}
        builder = AnimationBuilder(scene)

        e = Entity()
        node = Rectangle()
    }

    @Test
    fun `Duration`() {
        val anim = builder.duration(Duration.seconds(2.0))
                .translate(e)
                .from(Point2D(10.0, 10.0))
                .to(Point2D(50.0, 50.0))
                .build()

        anim.start()

        assertThat(e.x, `is`(10.0))
        assertThat(e.y, `is`(10.0))

        anim.onUpdate(0.5)

        assertThat(e.x, `is`(20.0))
        assertThat(e.y, `is`(20.0))

        anim.onUpdate(0.5)

        assertThat(e.x, `is`(30.0))
        assertThat(e.y, `is`(30.0))

        anim.onUpdate(0.5)

        assertThat(e.x, `is`(40.0))
        assertThat(e.y, `is`(40.0))

        anim.onUpdate(0.5)

        assertThat(e.x, `is`(50.0))
        assertThat(e.y, `is`(50.0))
    }

    @Test
    fun `Delay`() {
        val anim = builder.delay(Duration.seconds(2.0))
                .translate(e)
                .from(Point2D(10.0, 10.0))
                .to(Point2D(50.0, 50.0))
                .build()

        anim.start()

        assertThat(e.x, `is`(10.0))
        assertThat(e.y, `is`(10.0))

        anim.onUpdate(1.0)

        assertThat(e.x, `is`(10.0))
        assertThat(e.y, `is`(10.0))

        anim.onUpdate(1.0)

        assertThat(e.x, `is`(10.0))
        assertThat(e.y, `is`(10.0))

        anim.onUpdate(1.0)

        assertThat(e.x, `is`(50.0))
        assertThat(e.y, `is`(50.0))
    }

    @Test
    fun `Repeat`() {
        val anim = builder.repeat(3)
                .fade(e)
                .from(0.0)
                .to(1.0)
                .build()

        anim.start()

        assertThat(e.opacity, `is`(0.0))
        anim.onUpdate(1.0)
        assertThat(e.opacity, `is`(1.0))

        // Testing if the animation restarts.
        anim.onUpdate(0.1)
        assertThat(e.opacity, `is`(0.1))

        anim.onUpdate(0.9)
        assertThat(e.opacity, `is`(1.0))

        // Finishing another loop.
        anim.onUpdate(1.0)
        assertThat(e.opacity, `is`(1.0))

        // Test if the animation had stopped.
        anim.onUpdate(0.1)
        assertThat(e.opacity, `is`(1.0))
    }

    @Test
    fun `Repeat Infinite`() {
        val anim = builder.repeatInfinitely()
                .fade(e)
                .from(0.0)
                .to(1.0)
                .build()

        anim.start()
        for (i in 0..19) {
            anim.onUpdate(0.1)
            assertThat(e.opacity, `is`(0.1))

            anim.onUpdate(0.9)
            assertThat(e.opacity, `is`(1.0))
        }
    }

    @Test
    fun `Reverse`() {
        val anim = builder.autoReverse(true).repeat(2)
                .fade(e)
                .from(0.0)
                .to(1.0)
                .build()

        anim.start()
        assertThat(e.opacity, `is`(0.0))

        anim.onUpdate(1.0)
        assertThat(e.opacity, `is`(1.0))

        anim.onUpdate(0.1)
        assertThat(e.opacity, `is`(0.9))

        anim.onUpdate(0.9)
        assertThat(e.opacity, `is`(0.0))
    }

    @Test
    fun `Translate`() {
        val anim = builder.translate(e)
                .from(Point2D(10.0, 10.0))
                .to(Point2D(50.0, 50.0))
                .build()

        anim.start()

        assertThat(e.x, `is`(10.0))
        assertThat(e.y, `is`(10.0))

        anim.onUpdate(0.5)
        anim.onUpdate(0.5)

        assertThat(e.x, `is`(50.0))
        assertThat(e.y, `is`(50.0))
    }

    @Test
    fun `Fade`() {
        val anim = builder.fade(e)
                .from(0.0)
                .to(1.0)
                .build()

        anim.start()

        assertThat(e.opacity, `is`(0.0))

        anim.onUpdate(0.5)
        anim.onUpdate(0.5)

        assertThat(e.opacity, `is`(1.0))
    }

    @Test
    fun `Rotate`() {
        val anim = builder.rotate(e)
                .from(0.0)
                .to(180.0)
                .build()

        anim.start()

        assertThat(e.rotation, `is`(0.0))

        anim.onUpdate(0.5)
        anim.onUpdate(0.5)

        assertThat(e.rotation, `is`(180.0))
    }

    @Test
    fun `Scale`() {
        val anim = builder.scale(e)
                .from(Point2D(1.0, 1.0))
                .to(Point2D(3.0, 3.0))
                .build()

        anim.start()

        assertThat(e.scaleX, `is`(1.0))
        assertThat(e.scaleY, `is`(1.0))

        anim.onUpdate(0.5)
        anim.onUpdate(0.5)

        assertThat(e.scaleX, `is`(3.0))
        assertThat(e.scaleY, `is`(3.0))
    }

    @Test
    fun `JavaFX property animation`() {
        val rect = Rectangle()

        val anim = builder.animate(rect.widthProperty())
                .from(10.0)
                .to(110.0)
                .build()

        anim.start()
        assertThat(rect.width, `is`(10.0))

        anim.onUpdate(0.5)
        assertThat(rect.width, `is`(60.0))

        anim.onUpdate(0.5)
        assertThat(rect.width, `is`(110.0))

        // animation stopped at this point
        anim.onUpdate(0.5)
        assertThat(rect.width, `is`(110.0))
    }

    @Test
    fun `Generic animated value animation`() {
        val color = AnimatedColor(Color.BLACK, Color.WHITE)

        var value = Color.BLACK

        val anim = builder.animate(color)
                .onProgress(Consumer { value = it })
                .build()

        anim.start()
        assertThat(value, `is`(Color.BLACK))

        anim.onUpdate(0.5)
        assertThat(value.red, `is`(0.5))
        assertThat(value.green, `is`(0.5))
        assertThat(value.blue, `is`(0.5))

        anim.onUpdate(0.5)
        assertThat(value, `is`(Color.WHITE))

        // animation stopped at this point
        anim.onUpdate(0.5)
        assertThat(value, `is`(Color.WHITE))
    }
}