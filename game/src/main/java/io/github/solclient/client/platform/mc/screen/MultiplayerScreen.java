package io.github.solclient.client.platform.mc.screen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MultiplayerScreen extends Screen {

	static @NotNull MultiplayerScreen create(@Nullable Screen parent) {
		throw new UnsupportedOperationException();
	}

}