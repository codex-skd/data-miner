package com.skd.dataminer.command;

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
                .then(Commands.literal("events")
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            EventTracer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Event tracer started. Use /dataminer events stop to finish."),
                                true
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            EventTracer.stop();
                            try {
                                Path reportPath = EventTracer.saveReport();
                                ctx.getSource().sendSuccess(
                                    () -> Component.literal("Event report saved to " + reportPath),
                                    true
                                );
                            } catch (IOException e) {
                                ctx.getSource().sendFailure(
                                    Component.literal("Failed to save event report: " + e.getMessage())
                                );
                            }
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
                                true
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("start")
                        .executes(ctx -> {
                            VisionAnalyzer.start();
                            ctx.getSource().sendSuccess(
                                () -> Component.literal("Vision analyzer started. Use /dataminer vision stop to finish."),
                                true
                            );
                            return 1;
                        })
                    )
                    .then(Commands.literal("stop")
                        .executes(ctx -> {
                            VisionAnalyzer.stop();
                            try {
                                Path reportPath = VisionAnalyzer.saveReport();
                                ctx.getSource().sendSuccess(
                                    () -> Component.literal("Vision report saved to " + reportPath),
                                    true
                                );
                            } catch (IOException e) {
                                ctx.getSource().sendFailure(
                                    Component.literal("Failed to save vision report: " + e.getMessage())
                                );
                            }
                            return 1;
                        })
                    )
                )
        );
    }
}
