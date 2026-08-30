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
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import org.slf4j.Logger;

@Mod(DataMiner.MODID)
public class DataMiner {

    public static final String MODID = "data_miner";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DataMiner(IEventBus modEventBus, ModContainer modContainer) {
        ErrorCollector.register();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onRegisterPayloads);

        modContainer.registerConfig(ModConfig.Type.COMMON, DataMinerConfig.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        if (net.neoforged.fml.loading.FMLEnvironment.getDist().isClient()) {
            DataMinerClientNetworking.registerReloadListener();
        }
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

    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
            DataMinerNetworkPayloads.ClientReloadPayload.TYPE,
            DataMinerNetworkPayloads.ClientReloadPayload.STREAM_CODEC,
            (payload, context) -> {
                var player = (net.minecraft.server.level.ServerPlayer) context.player();
                if (player != null) DataMinerServerNetworking.handleClientReload(player, payload);
            }
        );
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        DataMinerCommands.register(event.getDispatcher());
    }
}
