package com.skd.data_miner;

import com.mojang.logging.LogUtils;
import com.skd.data_miner.command.DataMinerCommands;
import com.skd.data_miner.dumper.RegistryDumper;
import com.skd.data_miner.error.ErrorCollector;
import com.skd.data_miner.init.Initializer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import org.slf4j.Logger;

@Mod(DataMiner.MODID)
public class DataMiner {

    public static final String MODID = "data_miner";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DataMiner(IEventBus modEventBus, ModContainer modContainer) {
        ErrorCollector.register();
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, DataMinerConfig.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("DataMiner initializing...");

        DataMinerExecutor.runAsync(() -> {
            Initializer.init();

            if (DataMinerConfig.DUMP_ON_STARTUP.get()) {
                LOGGER.info("Starting registry dump...");
                RegistryDumper.dumpAll();
                LOGGER.info("Registry dump completed.");
            }
        });
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        DataMinerCommands.register(event.getDispatcher());
    }
}
