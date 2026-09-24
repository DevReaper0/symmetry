package xyz.devreaper0.symmetry.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiDialogBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.input.ScanCodes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import xyz.devreaper0.symmetry.Symmetry;
import xyz.devreaper0.symmetry.SymmetryType;

import java.util.List;

public class GuiSymmetrizeSelection extends GuiDialogBase {
    protected static final int MAX_TEXT_LENGTH = 512;

    protected GuiTextFieldGeneric textField;
    protected SymmetryType symmetryType = SymmetryType.MIRROR_XZ;
    protected final String originalText;
    protected final int textHeight;
    protected final int buttonHeight;
    protected final int totalHeight;
    protected final int totalWidth;

    public GuiSymmetrizeSelection(String defaultText, @Nullable Screen parent) {
        this.setParent(parent);
        this.title = StringUtils.translate("symmetry.gui.symmetrize_selection");
        this.useTitleHierarchy = false;
        this.originalText = defaultText;
        this.textHeight = 20;
        this.buttonHeight = 20;
        this.totalWidth = 200;
        this.totalHeight = this.textHeight + this.buttonHeight + 4;

        this.setWidthAndHeight(this.totalWidth + 20, this.textHeight + this.totalHeight + this.buttonHeight + 30);
        this.centerOnScreen();
    }

    @Override
    public void initGui() {
        int x = this.dialogLeft + 10;
        int y = this.dialogTop + this.textHeight + 10;

        this.textField = new GuiTextFieldGeneric(x, y, this.totalWidth, this.textHeight, this.font);
        this.textField.setMaxLength(MAX_TEXT_LENGTH);
        this.textField.setValue(this.originalText);
        this.textField.setFocused(true);
        this.addTextField(textField, null);

        y += this.textHeight + 4;
        ButtonGeneric symmetryTypeButton = new ButtonGeneric(x, y, this.totalWidth, this.buttonHeight + 2, this.getButtonText());
        this.addButton(symmetryTypeButton, this.createActionListener(ButtonType.SYMMETRY_TYPE));

        y += this.textHeight + 10;
        x += this.createButton(x, y, ButtonType.OK) + 2;
        x += this.createButton(x, y, ButtonType.RESET) + 2;
        this.createButton(x, y, ButtonType.CANCEL);
    }

    protected int createButton(int x, int y, ButtonType type) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, this.buttonHeight, type.getDisplayName());
        button.setWidth(Math.max(40, button.getWidth()));
        return this.addButton(button, this.createActionListener(type)).getWidth();
    }

    protected String getButtonText() {
        return StringUtils.translate("symmetry.gui.button.symmetrize_selection.symmetry_type", this.symmetryType.getDisplayName());
    }

    protected void apply(String name, SymmetryType symmetryType) {
        Symmetry.getInstance().symmetrizeSelection(name, symmetryType);
    }

    @Override
    public boolean isPauseScreen() {
        return this.getParent() != null && this.getParent().isPauseScreen();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float partialTicks) {
        if (this.getParent() != null) {
            this.getParent().extractRenderState(drawContext, mouseX, mouseY, partialTicks);
        }

        super.extractRenderState(drawContext, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawScreenBackground(GuiContext ctx, int mouseX, int mouseY) {
        super.drawScreenBackground(ctx, mouseX, mouseY);

        RenderUtils.drawOutlinedBox(ctx, this.dialogLeft, this.dialogTop, this.dialogWidth, this.dialogHeight, 0xE0000000, COLOR_HORIZONTAL_BAR);
    }

    @Override
    protected void drawTitle(GuiContext ctx, int mouseX, int mouseY, float partialTicks) {
        this.drawStringWithShadow(ctx, this.title, this.dialogLeft + 10, this.dialogTop + 10, COLOR_WHITE);
    }

    @Override
    protected void drawContents(GuiContext ctx, int mouseX, int mouseY, float partialTicks) {
        final List<String> warningText = StringUtils.translateAndLineSplit("symmetry.message.warn.overlapping_placements", StringUtils.translate("litematica.config.generic.name.placementReplaceBehavior"));
        for (int i = 0; i < warningText.size(); i++) {
            String line = warningText.get(i);
            this.drawStringWithShadow(ctx, line, this.dialogLeft + (this.dialogWidth / 2) - (this.font.width(line) / 2), this.dialogTop + this.dialogHeight + 9 + (i * 11), COLOR_WHITE);
        }
    }

    @Override
    public boolean onKeyTyped(KeyEvent input) {
        if (input.key() == ScanCodes.SCAN_RETURN) {
            apply(this.textField.getValue(), this.symmetryType);
            GuiBase.openGui(this.getParent());
            return true;
        }

        return super.onKeyTyped(input);
    }

    protected ButtonListener createActionListener(ButtonType type) {
        return new ButtonListener(type, this);
    }

    protected static class ButtonListener implements IButtonActionListener {
        private final GuiSymmetrizeSelection gui;
        private final ButtonType type;

        public ButtonListener(ButtonType type, GuiSymmetrizeSelection gui) {
            this.type = type;
            this.gui = gui;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == ButtonType.SYMMETRY_TYPE) {
                this.gui.symmetryType = gui.symmetryType.cycle(mouseButton != 1);
                button.setDisplayString(this.gui.getButtonText());
                button.setHoverStrings(this.gui.symmetryType.getHoverText());
            } else if (this.type == ButtonType.OK) {
                this.gui.apply(this.gui.textField.getValue(), this.gui.symmetryType);
                GuiBase.openGui(this.gui.getParent());
            } else if (this.type == ButtonType.CANCEL) {
                GuiBase.openGui(this.gui.getParent());
            } else if (this.type == ButtonType.RESET) {
                this.gui.textField.setValue(this.gui.originalText);
                this.gui.textField.setFocused(true);
            }
        }
    }

    protected enum ButtonType {
        SYMMETRY_TYPE(""),
        OK("malilib.gui.button.ok"),
        CANCEL("malilib.gui.button.cancel"),
        RESET("malilib.gui.button.reset");

        private final String labelKey;

        ButtonType(String labelKey) {
            this.labelKey = labelKey;
        }

        public String getDisplayName() {
            return StringUtils.translate(this.labelKey);
        }
    }
}
