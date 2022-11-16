package io.github.solclient.client.util;

import java.util.function.*;

import lombok.RequiredArgsConstructor;

/**
 * A "chain" cache - if I changes, O is recalculated.
 * @param <I> The control value.
 * @param <O> The output function.
 */
@RequiredArgsConstructor
public final class ChainCache<I, O> {

	private final Supplier<I> controlValueSupplier;
	private final Function<I, O> mapper;
	private I lastControlValue;
	private O lastOutput;

	public O get() {
		I controlValue = controlValueSupplier.get();

		if(!controlValue.equals(lastControlValue)) {
			lastControlValue = controlValue;
			return lastOutput = mapper.apply(lastControlValue);
		}

		return lastOutput;
	}
}