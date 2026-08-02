package com.skd.data_miner.client;

import net.fabricmc.api.ClientModInitializer;

import com.skd.data_miner.latency.ClientPerfHandlers;

public class DataMinerClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPerfHandlers.register();
	}
}
