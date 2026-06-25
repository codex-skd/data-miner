package com.skd.dataminer.command;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerExecutor;
import com.skd.dataminer.dumper.RegistryDumper;
import com.skd.dataminer.event.EventTracer;
import com.skd.dataminer.perf.PerformanceMonitor;
import com.skd.dataminer.vision.VisionAnalyzer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Path;

public class DataMinerCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("dataminer")
                .then(Commands.literal("dump")
                    .executes(ctx -> {
                        DataMinerExecutor.runAsync(() -> {
                            RegistryDumper.dumpAll();
                            DataMiner.LOGGER.info("Registry dump completed");
                        });
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("Dump started in background. Check logs for progress."),
                            false
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
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            PerformanceMonitor.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try {
                                    Path reportPath = PerformanceMonitor.saveReport();
                                    DataMiner.LOGGER.info("Performance report saved to {}", reportPath);
                                } catch (IOException e) {
                                    DataMiner.LOGGER.error("Failed to save performance report", e);
                                }
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report saving in background. Check logs."),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("events")
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            EventTracer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Event tracer started. Use /dataminer events stop to finish."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            EventTracer.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try {
                                    Path reportPath = EventTracer.saveReport();
                                    DataMiner.LOGGER.info("Event report saved to {}", reportPath);
                                } catch (IOException e) {
                                    DataMiner.LOGGER.error("Failed to save event report", e);
                                }
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report saving in background. Check logs."),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("vision")
                    .then(Commands.literal("analyze")
                        .executes(ctx -> {
                            VisionAnalyzer.analyze();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Screenshot captured and sent for analysis."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            VisionAnalyzer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Vision analyzer started. Use /dataminer vision stop to finish."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            VisionAnalyzer.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try {
                                    Path reportPath = VisionAnalyzer.saveReport();
                                    DataMiner.LOGGER.info("Vision report saved to {}", reportPath);
                                } catch (IOException e) {
                                    DataMiner.LOGGER.error("Failed to save vision report", e);
                                }
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report saving in background. Check logs."),
                                false
                            );
                            return 1;
                        })
                    )
                )
        );
    }
}
