package com.skd.data_miner;

import com.skd.data_miner.command.DataMinerCommands;
import com.skd.data_miner.dumper.RegistryDumper;
import com.skd.data_miner.error.ErrorCollector;
import com.skd.data_miner.event.EventHandlers;
import com.skd.data_miner.init.Initializer;
import com.skd.data_miner.latency.LatencyEventHandlers;
import com.skd.data_miner.perf.PerfEventHandlers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMiner implements ModInitializer {

	public static final String MOD_ID = "data_miner";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("DataMiner initializing...");

		ErrorCollector.register();
		DataMinerConfig.load();

		EventHandlers.register();
		LatencyEventHandlers.register();
		PerfEventHandlers.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				DataMinerCommands.register(dispatcher));

		DataMinerExecutor.runAsync(() -> {
			Initializer.init();

			if (DataMinerConfig.DUMP_ON_STARTUP) {
				LOGGER.info("Starting registry dump...");
				RegistryDumper.dumpAll();
				LOGGER.info("Registry dump completed.");
			}
		});
	}
}
