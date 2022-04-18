package me.mcblueparrot.client.ui.screen;

import java.io.IOException;

import me.mcblueparrot.client.Client;
import me.mcblueparrot.client.ui.component.Component;
import me.mcblueparrot.client.ui.component.Screen;
import me.mcblueparrot.client.util.access.AccessGuiMainMenu;
import me.mcblueparrot.client.util.data.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class PanoramaBackgroundScreen extends Screen {

	private GuiScreen mainMenu = Client.INSTANCE.getMainMenu();

	public PanoramaBackgroundScreen(Component root) {
		super(root);
		background = false;
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
		mainMenu.setWorldAndResolution(mc, width, height);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		mainMenu.updateScreen();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}

	protected void drawPanorama(int mouseX, int mouseY, float partialTicks) {
		AccessGuiMainMenu access = (AccessGuiMainMenu) mainMenu;

		mc.getFramebuffer().unbindFramebuffer();

        GlStateManager.viewport(0, 0, 256, 256);
        access.renderPanorama(mouseX, mouseY, partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        access.rotateAndBlurPanorama(partialTicks);
        mc.getFramebuffer().bindFramebuffer(true);

        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);

        float uvBase = width > height ? 120.0F / width : 120.0F / height;
        float uBase = height * uvBase / 256.0F;
        float vBase = width * uvBase / 256.0F;

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.pos(0.0D, height, zLevel).tex((0.5F - uBase), (0.5F + vBase)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		renderer.pos(width, height, zLevel).tex(0.5F - uBase, 0.5F - vBase).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		renderer.pos(width, 0.0D, zLevel).tex(0.5F + uBase, 0.5F - vBase).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		renderer.pos(0.0D, 0.0D, zLevel).tex(0.5F + uBase, 0.5F + vBase).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();

		drawRect(0, 0, width, height, new Colour(0, 0, 0, 100).getValue());

		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1);
	}

}
