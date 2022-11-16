package io.github.solclient.client.platform.mc.world.level;

import org.jetbrains.annotations.NotNull;

import io.github.solclient.client.platform.mc.world.entity.Entity;

public interface ClientLevel extends Level {

	@NotNull Iterable<Entity> getRenderedEntities();

}