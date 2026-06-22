package com.skd.dataminer;

import com.mojang.logging.LogUtils;
import com.skd.dataminer.dumper.RegistryDumper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import org.slf4j.Logger;

@Mod(DataMiner.MODID)
public class DataMiner {

    public static final String MODID = "dataminer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DataMiner(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, DataMinerConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("DataMiner initializing...");

        if (DataMinerConfig.DUMP_ON_STARTUP.get()) {
            LOGGER.info("Starting registry dump...");
            RegistryDumper.dumpAll();
            LOGGER.info("Registry dump completed.");
        }
    }
}
