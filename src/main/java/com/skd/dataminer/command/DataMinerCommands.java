package com.skd.dataminer.command;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerExecutor;
import com.skd.dataminer.dumper.RegistryDumper;
import com.skd.dataminer.event.EventTracer;
import com.skd.dataminer.latency.LatencyTracer;
import com.skd.dataminer.modimpact.ModAnalyzer;
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
                        DataMinerExecutor.runAsync(RegistryDumper::dumpAll);
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("Dump started in background. Check logs."),
                            false
                        );
                        return 1;
                    })
                )
                .then(Commands.literal("mods")
                    .then(Commands.literal("analyze")
                        .executes(ctx -> {
                            DataMinerExecutor.runAsync(ModAnalyzer::generateModAnalysis);
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Mod analysis started. Check startup/mod_analysis.json."),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("perf")
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            PerformanceMonitor.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Performance monitor started."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            PerformanceMonitor.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try { PerformanceMonitor.saveReport(); } catch (IOException ignored) {}
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report in background."),
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
                                () -> Component.literal("Event tracer started."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            EventTracer.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try { EventTracer.saveReport(); } catch (IOException ignored) {}
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report in background."),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("latency")
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            LatencyTracer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Latency tracer started."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            LatencyTracer.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try { LatencyTracer.saveReport(); } catch (IOException ignored) {}
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report in background."),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("vision")
                    .then(Commands.literal("analyze")
                        .executes(ctx -> {
                            if (!VisionAnalyzer.isClientReady()) {
                                ctx.getSource().sendFailure(
                                    Component.literal("Vision: client only."));
                                return 0;
                            }
                            VisionAnalyzer.analyze();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Screenshot captured."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            if (!VisionAnalyzer.isClientReady()) {
                                ctx.getSource().sendFailure(
                                    Component.literal("Vision: client only."));
                                return 0;
                            }
                            VisionAnalyzer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Vision analyzer started."),
                                false
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            VisionAnalyzer.stop();
                            DataMinerExecutor.runAsync(() -> {
                                try { VisionAnalyzer.saveReport(); } catch (IOException ignored) {}
                            });
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Stopping. Report in background."),
                                false
                            );
                            return 1;
                        })
                    )
                )
        );
    }
}
