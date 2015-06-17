package com.octagon.crazygui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureUtils {
    public static void bindTextureToClient(ResourceLocation texture) {
        if (texture != null) {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc != null) {
                mc.renderEngine.bindTexture(texture);
            } else {
                LogHelper.warn("Binding texture to null client.");
            }
        } else {
            LogHelper.warn("Invalid texture location '%s'", texture);
        }
    }
}
