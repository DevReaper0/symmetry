package xyz.devreaper0.symmetry.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    private static final String HOTKEYS_KEY = "symmetry.config.hotkeys";

    public static final ConfigHotkey SYMMETRIZE_SELECTION = new ConfigHotkey("symmetrizeSelection", "R").apply(HOTKEYS_KEY);

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(SYMMETRIZE_SELECTION);
}
