package com.skd.dataminer.vision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ScreenCapture {

    public static CompletableFuture<Path> capture(Path outputDir, String filename) {
        Minecraft mc = Minecraft.getInstance();
        CompletableFuture<Path> resultFuture = new CompletableFuture<>();

        mc.execute(() -> {
            Path vanillaPath = mc.gameDirectory.toPath().resolve("screenshots").resolve(filename);

            Screenshot.grab(
                mc.gameDirectory,
                filename,
                mc.getMainRenderTarget(),
                1,
                component -> {
                    try {
                        if (Files.exists(vanillaPath)) {
                            Files.createDirectories(outputDir);
                            Path outputPath = outputDir.resolve(filename);
                            Files.copy(vanillaPath, outputPath);
                            resultFuture.complete(outputPath);
                        } else {
                            resultFuture.completeExceptionally(
                                new IOException("Screenshot not found: " + vanillaPath));
                        }
                    } catch (IOException e) {
                        resultFuture.completeExceptionally(e);
                    }
                }
            );
        });

        return resultFuture;
    }
}
