package com.example.starcrosshair;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StarConfigScreen extends Screen {
    private static final float[] SPEEDS = {0f, 0.5f, 1f, 2f, 4f};
    private static final int[] SIZES = {8, 12, 16, 24, 32};

    private final Screen parent;

    public StarConfigScreen(Screen parent) {
        super(Text.literal("Star Crosshair"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 200, h = 20, x = width / 2 - w / 2, y = height / 2 - 50, gap = 24;

        addDrawableChild(ButtonWidget.builder(crosshairText(), b -> {
            StarConfig.INSTANCE.starCrosshair = !StarConfig.INSTANCE.starCrosshair;
            b.setMessage(crosshairText());
        }).dimensions(x, y, w, h).build());

        addDrawableChild(ButtonWidget.builder(skyText(), b -> {
            StarConfig.INSTANCE.skyImage = !StarConfig.INSTANCE.skyImage;
            b.setMessage(skyText());
        }).dimensions(x, y + gap, w, h).build());

        addDrawableChild(ButtonWidget.builder(speedText(), b -> {
            StarConfig.INSTANCE.rotationSpeed =
                    next(SPEEDS, StarConfig.INSTANCE.rotationSpeed);
            b.setMessage(speedText());
        }).dimensions(x, y + gap * 2, w, h).build());

        addDrawableChild(ButtonWidget.builder(sizeText(), b -> {
            StarConfig.INSTANCE.crosshairSize =
                    (int) next(toFloat(SIZES), StarConfig.INSTANCE.crosshairSize);
            b.setMessage(sizeText());
        }).dimensions(x, y + gap * 3, w, h).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Fertig"), b -> close())
                .dimensions(x, y + gap * 4 + 8, w, h).build());
    }

    private static float next(float[] values, float current) {
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i] - current) < 0.001f) return values[(i + 1) % values.length];
        }
        return values[0];
    }

    private static float[] toFloat(int[] a) {
        float[] f = new float[a.length];
        for (int i = 0; i < a.length; i++) f[i] = a[i];
        return f;
    }

    private static Text onOff(boolean v) { return Text.literal(v ? "AN" : "AUS"); }

    private Text crosshairText() {
        return Text.literal("Stern-Crosshair: ").append(onOff(StarConfig.INSTANCE.starCrosshair));
    }
    private Text skyText() {
        return Text.literal("Himmels-Bild: ").append(onOff(StarConfig.INSTANCE.skyImage));
    }
    private Text speedText() {
        return Text.literal("Drehgeschwindigkeit: " + StarConfig.INSTANCE.rotationSpeed + "x");
    }
    private Text sizeText() {
        return Text.literal("Crosshair-Größe: " + StarConfig.INSTANCE.crosshairSize + " px");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 80, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        StarConfig.save();
        client.setScreen(parent);
    }
}
