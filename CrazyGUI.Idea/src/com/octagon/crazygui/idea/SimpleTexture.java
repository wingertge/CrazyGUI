package com.octagon.crazygui.idea;

import com.octagon.crazygui.antlr.util.LogHelper;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SimpleTexture extends AbstractTexture {
    protected final ResourceLocation textureLocation;
    private static final String __OBFID = "CL_00001052";

    public SimpleTexture(ResourceLocation p_i1275_1_)
    {
        this.textureLocation = p_i1275_1_;
    }

    public void loadTexture(IResourceManager p_110551_1_) throws IOException
    {
        this.deleteGlTexture();
        InputStream inputstream = null;

        try
        {
            IResource iresource = p_110551_1_.getResource(this.textureLocation);
            inputstream = iresource.getInputStream();
            BufferedImage image = ImageIO.read(inputstream);
            boolean blur = false;
            boolean clamp = false;

            if (iresource.hasMetadata())
            {
                try
                {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

                    if (texturemetadatasection != null)
                    {
                        blur = texturemetadatasection.getTextureBlur();
                        clamp = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException e)
                {
                    LogHelper.warn("Failed reading metadata of: " + this.textureLocation, e);
                }
            }

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), image, blur, clamp);
        }
        finally
        {
            if (inputstream != null)
            {
                inputstream.close();
            }
        }
    }
}
