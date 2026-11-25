package com.example.starcrosshair;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class StarCrosshairClient implements ClientModInitializer {
    private static final Identifier STAR =
            Identifier.of("starcrosshair", "textures/gui/star.png");
    private static final int TEX_SIZE = 64;

    private static KeyBinding openMenuKey;
    private static KeyBinding toggleCrosshairKey;
    private static KeyBinding toggleSkyKey;

    @Override
    public void onInitializeClient() {
        StarConfig.load();

        KeyBinding.Category category =
                KeyBinding.Category.create(Identifier.of("starcrosshair", "main"));

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starcrosshair.menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, category));

        // Optionale Hotkeys: standardmäßig unbelegt, der Nutzer entscheidet selbst.
        toggleCrosshairKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starcrosshair.toggle_crosshair", InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(), category));
        toggleSkyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starcrosshair.toggle_sky", InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(), category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                client.setScreen(new StarConfigScreen(client.currentScreen));
            }
            while (toggleCrosshairKey.wasPressed()) {
                StarConfig.INSTANCE.starCrosshair = !StarConfig.INSTANCE.starCrosshair;
                StarConfig.save();
                notify(client, "Stern-Crosshair", StarConfig.INSTANCE.starCrosshair);
            }
            while (toggleSkyKey.wasPressed()) {
                StarConfig.INSTANCE.skyImage = !StarConfig.INSTANCE.skyImage;
                StarConfig.save();
                notify(client, "Himmels-Bild", StarConfig.INSTANCE.skyImage);
            }
        });

        HudElementRegistry.replaceElement(VanillaHudElements.CROSSHAIR, original ->
            (context, tickCounter) -> {
                StarConfig cfg = StarConfig.INSTANCE;
                if (!cfg.starCrosshair) {          // aus -> normales Fadenkreuz
                    original.render(context, tickCounter);
                    return;
                }

                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.world == null || mc.options.hudHidden
                        || !mc.options.getPerspective().isFirstPerson()) return;

                int size = cfg.crosshairSize;
                float cx = context.getScaledWindowWidth() / 2f;
                float cy = context.getScaledWindowHeight() / 2f;
                float angle = (mc.world.getTime() + tickCounter.getTickProgress(false))
                        * 0.05f * cfg.rotationSpeed;

                var m = context.getMatrices();
                m.pushMatrix();
                m.translate(cx, cy);
                m.rotate(angle);
                context.drawTexture(RenderPipelines.GUI_TEXTURED, STAR,
                        -size / 2, -size / 2, 0, 0, size, size, TEX_SIZE, TEX_SIZE);
                m.popMatrix();
            });
    }

    private static void notify(MinecraftClient client, String name, boolean on) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(name + ": " + (on ? "AN" : "AUS")), true);
        }
    }
}
