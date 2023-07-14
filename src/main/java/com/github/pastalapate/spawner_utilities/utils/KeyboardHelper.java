package com.github.pastalapate.spawner_utilities.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper {
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    public static boolean isHoldingShift() {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isHoldingControl() {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_CONTROL) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isHoldingSpace() {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_SPACE);
    }
}