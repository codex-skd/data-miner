package com.skd.dataminer.vision;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ScreenCapture {

    public static Path capture(Path outputDir, String filename) throws IOException {
        Minecraft mc = Minecraft.getInstance();
        Files.createDirectories(outputDir);
        Path outputPath = outputDir.resolve(filename);

        Path vanillaPath = mc.gameDirectory.toPath().resolve("screenshots").resolve(filename);

        CompletableFuture<Void> future = new CompletableFuture<>();

        Screenshot.grab(
            mc.gameDirectory,
            filename,
            mc.getMainRenderTarget(),
            1,
            component -> future.complete(null)
        );

        try {
            future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Screenshot interrupted", e);
        } catch (ExecutionException e) {
            throw new IOException("Screenshot failed", e.getCause());
        } catch (java.util.concurrent.TimeoutException e) {
            throw new IOException("Screenshot timed out", e);
        }

        if (Files.exists(vanillaPath)) {
            Files.copy(vanillaPath, outputPath);
            return outputPath;
        }

        throw new IOException("Screenshot file not found: " + vanillaPath);
    }
}
