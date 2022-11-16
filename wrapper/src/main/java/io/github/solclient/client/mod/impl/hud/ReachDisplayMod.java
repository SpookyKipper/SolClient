package io.github.solclient.client.mod.impl.hud;

import java.text.DecimalFormat;

import io.github.solclient.client.event.EventHandler;
import io.github.solclient.client.event.impl.world.entity.EntityAttackEvent;
import io.github.solclient.client.mod.hud.SimpleHudMod;

public final class ReachDisplayMod extends SimpleHudMod {

	public static final ReachDisplayMod INSTANCE = new ReachDisplayMod();
	private static final DecimalFormat FORMAT = new DecimalFormat("0.##");

	private double distance = 0;
	private long hitTime = -1;

	@Override
	public String getId() {
		return "reach_display";
	}

	@Override
	public String getText(boolean editMode) {
		if((System.currentTimeMillis() - hitTime) > 5000) {
			distance = 0;
		}
		if(editMode) {
			return "0 mts";
		}
		else {
			return FORMAT.format(distance) + " m" + (distance != 1.0 ? "ts" : "");
		}
	}

	@EventHandler
	public void totallyNoReachHax(EntityAttackEvent event) {
		distance = event.getEntity().getEyePosition().distanceSquared(mc.getPlayer().getEyePosition());
		hitTime = System.currentTimeMillis();
	}

}