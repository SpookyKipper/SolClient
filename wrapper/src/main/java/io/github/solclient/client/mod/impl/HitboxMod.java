package io.github.solclient.client.mod.impl;

import com.google.gson.annotations.Expose;

import io.github.solclient.client.*;
import io.github.solclient.client.event.EventHandler;
import io.github.solclient.client.event.impl.game.PreTickEvent;
import io.github.solclient.client.event.impl.input.HitboxToggleEvent;
import io.github.solclient.client.event.impl.world.entity.render.HitboxRenderEvent;
import io.github.solclient.client.mod.*;
import io.github.solclient.client.mod.annotation.*;
import io.github.solclient.client.platform.mc.maths.*;
import io.github.solclient.client.platform.mc.option.KeyBinding;
import io.github.solclient.client.platform.mc.render.*;
import io.github.solclient.client.platform.mc.util.Input;
import io.github.solclient.client.platform.mc.world.entity.LivingEntity;
import io.github.solclient.client.platform.mc.world.level.LevelRenderer;
import io.github.solclient.client.util.data.Colour;

public final class HitboxMod extends Mod {

	public static final HitboxMod INSTANCE = new HitboxMod();

	@Option
	private final KeyBinding toggleHitboxes = KeyBinding.create(getTranslationKey() + ".option.toggleHitboxes", Input.UNKNOWN, Constants.KEY_CATEGORY);
	@Expose
	@Option
	private boolean boundingBox = true;
	@Expose
	@Option
	private Colour boundingBoxColour = Colour.WHITE;
	@Expose
	@Option
	private boolean eyeHeight = true;
	@Expose
	@Option
	private Colour eyeHeightColour = Colour.PURE_RED;
	@Expose
	@Option
	private boolean lookVector = true;
	@Expose
	@Option
	private Colour lookVectorColour = Colour.PURE_BLUE;
	@Expose
	@Option
	@Slider(min = 1, max = 10, step = 0.5F)
	private float lineWidth = 2;
	@Expose
	private boolean toggled;

	@Override
	public void onRegister() {
		super.onRegister();
		mc.getOptions().addKey(toggleHitboxes);
	}

	@Override
	public void postStart() {
		super.postStart();
		if(isEnabled()) {
			mc.getEntityRenderDispatcher().setHitboxes(toggled);
		}
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		if(mc.getEntityRenderDispatcher() != null) {
			toggled = mc.getEntityRenderDispatcher().getHitboxes();
		}
		while(toggleHitboxes.consumePress())
			;
	}

	@Override
	public String getId() {
		return "hitbox";
	}

	@Override
	public ModCategory getCategory() {
		return ModCategory.VISUAL;
	}

	@EventHandler
	public void onHitboxRender(HitboxRenderEvent event) {
		event.cancel();
		GlStateManager.depthMask(false);
		GlStateManager.disableTexture2d();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.lineWidth(lineWidth);

		float half = event.getEntity().getEntityWidth() / 2.0F;

		if(boundingBox) {
			Box box = event.getEntity().getBounds();
			Box offsetBox = Box.create(box.minX() - event.getEntity().x() + event.getX(),
					box.minY() - event.getEntity().y() + event.getY(),
					box.minZ() - event.getEntity().z() + event.getZ(),
					box.maxX() - event.getEntity().x() + event.getX(),
					box.maxY() - event.getEntity().y() + event.getY(),
					box.maxZ() - event.getEntity().z() + event.getZ());
			LevelRenderer.strokeBox(offsetBox, boundingBoxColour.getRed(), boundingBoxColour.getGreen(),
					boundingBoxColour.getBlue(), boundingBoxColour.getAlpha());
		}

		if(eyeHeight && event.getEntity() instanceof LivingEntity) {
			LevelRenderer.strokeBox(Box.create(event.getX() - half,
					event.getY() + event.getEntity().getEntityEyeHeight() - 0.009999999776482582D, event.getZ() - half,
					event.getX() + half, event.getY() + event.getEntity().getEntityEyeHeight() + 0.009999999776482582D,
					event.getZ() + half), eyeHeightColour.getRed(), eyeHeightColour.getGreen(),
					eyeHeightColour.getBlue(), eyeHeightColour.getAlpha());
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBufferBuilder();

		if(lookVector) {
			Vec3d look = event.getEntity().getView(event.getTickDelta());
			builder.begin(3, VertexFormat.POSITION_COLOUR);
			builder.vertex(event.getX(), event.getY() + event.getEntity().getEntityEyeHeight(), event.getZ())
					.color(0, 0, 255, 255).endVertex();
			builder.vertex(event.getX() + look.x() * 2, event.getY() + event.getEntity().getEntityEyeHeight() + look.y() * 2,
					event.getZ() + look.z() * 2)
					.color(lookVectorColour.getRed(), lookVectorColour.getGreen(), lookVectorColour.getBlue(),
							lookVectorColour.getAlpha())
					.endVertex();
			tessellator.end();
		}

		GlStateManager.enableTexture2d();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.resetLineWidth();
	}

	@EventHandler
	public void onHitboxToggle(HitboxToggleEvent event) {
		toggled = event.getState();
		Client.INSTANCE.save();
	}

	@EventHandler
	public void onTick(PreTickEvent event) {
		while(toggleHitboxes.consumePress()) {
			// If debug shortcut is used, don't conflict.
			if(toggleHitboxes.getKeyCode() == Input.B && Input.isKeyDown(Input.F3)) {
				continue;
			}

			toggled = !mc.getEntityRenderDispatcher().getHitboxes();
			mc.getEntityRenderDispatcher().setHitboxes(toggled);
			Client.INSTANCE.save();
		}
	}

	@Override
	public boolean isEnabledByDefault() {
		return true;
	}

}