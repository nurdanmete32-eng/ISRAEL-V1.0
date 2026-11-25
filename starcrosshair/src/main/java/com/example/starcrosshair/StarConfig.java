package com.example.starcrosshair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StarConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("starcrosshair.json");

    public static StarConfig INSTANCE = new StarConfig();

    public boolean starCrosshair = true;   // false = normales Vanilla-Fadenkreuz
    public boolean skyImage = true;        // wird vom Himmels-Renderer ausgewertet (folgt)
    public float rotationSpeed = 1.0f;     // 0 = keine Drehung
    public int crosshairSize = 16;         // Pixel auf dem Bildschirm

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                StarConfig c = GSON.fromJson(Files.readString(FILE), StarConfig.class);
                if (c != null) INSTANCE = c;
            }
        } catch (Exception e) {
            System.err.println("[starcrosshair] Config konnte nicht geladen werden: " + e);
        }
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            System.err.println("[starcrosshair] Config konnte nicht gespeichert werden: " + e);
        }
    }
}
