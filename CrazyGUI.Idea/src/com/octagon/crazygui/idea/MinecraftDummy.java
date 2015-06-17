package com.octagon.crazygui.idea;

import com.octagon.crazygui.antlr.util.LogHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MinecraftDummy {
    private TextureManager texManagerInstance;

    public TextureManager getTextureManager() {
        return texManagerInstance != null ? texManagerInstance : (texManagerInstance = new TextureManager());
    }

    private class TextureManager {
        private IResourceManager resourceManager = new IdeaResourceManager();
        private final Map<ResourceLocation, ITextureObject> mapTextureObjects = new HashMap<>();

        public TextureManager() {

        }

        public void bindTexture(ResourceLocation p_110577_1_)
        {
            Object object = this.mapTextureObjects.get(p_110577_1_);

            if (object == null)
            {
                object = new SimpleTexture(p_110577_1_);
                this.loadTexture(p_110577_1_, (ITextureObject)object);
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((ITextureObject) object).getGlTextureId());
        }

        public boolean loadTexture(ResourceLocation p_110579_1_, final ITextureObject p_110579_2_)
        {
            boolean flag = true;
            ITextureObject p_110579_2_2 = p_110579_2_;

            try
            {
                p_110579_2_.loadTexture(this.resourceManager);
            }
            catch (IOException ioexception)
            {
                LogHelper.warn("Failed to load texture: " + p_110579_1_, ioexception);
                p_110579_2_2 = TextureUtil.missingTexture;
                this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
                flag = false;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
                crashreportcategory.addCrashSection("Resource location", p_110579_1_);
                crashreportcategory.addCrashSectionCallable("Texture object class", new Callable()
                {
                    private static final String __OBFID = "CL_00001065";
                    public String call()
                    {
                        return p_110579_2_.getClass().getName();
                    }
                });
                throw new ReportedException(crashreport);
            }

            this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
            return flag;
        }
    }
}
