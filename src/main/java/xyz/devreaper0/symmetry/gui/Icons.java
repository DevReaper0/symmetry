package xyz.devreaper0.symmetry.gui;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.resources.Identifier;

public enum Icons implements IGuiIcon {
    SCHEMATIC_TYPE_SYMMETRY(0, 0, 12, 12);

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("symmetry", "textures/gui/gui_widgets.png");

    private final int u;
    private final int v;
    private final int w;
    private final int h;

    Icons(int u, int v, int w, int h) {
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
    }

    @Override
    public int getWidth() {
        return this.w;
    }

    @Override
    public int getHeight() {
        return this.h;
    }

    @Override
    public int getU() {
        return this.u;
    }

    @Override
    public int getV() {
        return this.v;
    }

    @Override
    public void renderAt(GuiContext ctx, int x, int y, float zLevel, boolean enabled, boolean selected) {
        RenderUtils.drawTexturedRect(ctx, this.getTexture(), x, y, this.u, this.v, this.w, this.h, zLevel);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }
}
