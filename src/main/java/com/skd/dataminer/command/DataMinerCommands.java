package com.skd.dataminer.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skd.dataminer.DataMiner;
import com.skd.dataminer.dumper.RegistryDumper;
import com.skd.dataminer.perf.PerformanceMonitor;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataMinerCommands {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("dataminer")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("dump")
                    .executes(ctx -> {
                        RegistryDumper.dumpAll();
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("Registry dump completed. Check dataminer_dumps/"),
                            true
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("perf")
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            PerformanceMonitor.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Performance monitor started. Use /dataminer perf stop to finish."),
                                true
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            PerformanceMonitor.stop();
                            try {
                                Path reportPath = PerformanceMonitor.saveReport();
                                ctx.getSource().sendSuccess(
                                    () -> Component.literal("Performance report saved to " + reportPath),
                                    true
                                );
                            } catch (IOException e) {
                                ctx.getSource().sendFailure(
                                    Component.literal("Failed to save performance report: " + e.getMessage())
                                );
                            }
                            return 1;
                        })
                    )
                )
        );
    }
}
