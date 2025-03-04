package com.terraformersmc.modmenu.config;

import com.google.gson.annotations.SerializedName;
import com.terraformersmc.modmenu.config.option.BooleanConfigOption;
import com.terraformersmc.modmenu.config.option.EnumConfigOption;
import com.terraformersmc.modmenu.config.option.StringSetConfigOption;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.client.option.Option;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModMenuConfig {
	public static final EnumConfigOption<Sorting> SORTING = new EnumConfigOption<>("sorting", Sorting.ASCENDING);
	public static final BooleanConfigOption COUNT_LIBRARIES = new BooleanConfigOption("count_libraries", true);
	public static final BooleanConfigOption COMPACT_LIST = new BooleanConfigOption("compact_list", false);
	public static final BooleanConfigOption COUNT_CHILDREN = new BooleanConfigOption("count_children", true);
	public static final EnumConfigOption<ModsButtonStyle> MODS_BUTTON_STYLE = new EnumConfigOption<>("mods_button_style", ModsButtonStyle.CLASSIC);
	public static final BooleanConfigOption COUNT_HIDDEN_MODS = new BooleanConfigOption("count_hidden_mods", true);
	public static final EnumConfigOption<ModCountLocation> MOD_COUNT_LOCATION = new EnumConfigOption<>("mod_count_location", ModCountLocation.TITLE_SCREEN);
	public static final BooleanConfigOption HIDE_MOD_LINKS = new BooleanConfigOption("hide_mod_links", false);
	public static final BooleanConfigOption SHOW_LIBRARIES = new BooleanConfigOption("show_libraries", false);
	public static final BooleanConfigOption HIDE_MOD_LICENSE = new BooleanConfigOption("hide_mod_license", false);
	public static final BooleanConfigOption HIDE_BADGES = new BooleanConfigOption("hide_badges", false);
	public static final BooleanConfigOption HIDE_MOD_CREDITS = new BooleanConfigOption("hide_mod_credits", false);
	public static final BooleanConfigOption EASTER_EGGS = new BooleanConfigOption("easter_eggs", true);
	public static final BooleanConfigOption MODIFY_TITLE_SCREEN = new BooleanConfigOption("modify_title_screen", true);
	public static final BooleanConfigOption MODIFY_GAME_MENU = new BooleanConfigOption("modify_game_menu", true);
	public static final BooleanConfigOption HIDE_CONFIG_BUTTONS = new BooleanConfigOption("hide_config_buttons", false);
	public static final StringSetConfigOption HIDDEN_MODS = new StringSetConfigOption("hidden_mods", new HashSet<>());
	public static final BooleanConfigOption LOG_DEPRECATION_WARNINGS = new BooleanConfigOption("log_deprecation_warnings", false);

	public static final Set<String> HIDDEN_OPTIONS = Stream.of(
		"MODIFY_TITLE_SCREEN",
		"MODIFY_GAME_MENU",
		"HIDE_CONFIG_BUTTONS"
	).collect(Collectors.toCollection(HashSet::new));

	public static Option[] asOptions() {
		ArrayList<Option> options = new ArrayList<>();
		for (Field field : ModMenuConfig.class.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && Option.class.isAssignableFrom(field.getType()) && !HIDDEN_OPTIONS.contains(field.getName())) {
				try {
					options.add((Option) field.get(null));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return options.stream().toArray(Option[]::new);
	}

	public enum Sorting {
		@SerializedName("ascending")
		ASCENDING(Comparator.comparing(mod -> mod.getName().toLowerCase(Locale.ROOT))),
		@SerializedName("descending")
		DESCENDING(ASCENDING.getComparator().reversed());

		Comparator<Mod> comparator;

		Sorting(Comparator<Mod> comparator) {
			this.comparator = comparator;
		}

		public Comparator<Mod> getComparator() {
			return comparator;
		}
	}

	public enum ModCountLocation {
		@SerializedName("title_screen")
		TITLE_SCREEN(true, false),
		@SerializedName("mods_button")
		MODS_BUTTON(false, true),
		@SerializedName("title_screen_and_mods_button")
		TITLE_SCREEN_AND_MODS_BUTTON(true, true),
		@SerializedName("none")
		NONE(false, false);

		private final boolean titleScreen, modsButton;

		ModCountLocation(boolean titleScreen, boolean modsButton) {
			this.titleScreen = titleScreen;
			this.modsButton = modsButton;
		}

		public boolean isOnTitleScreen() {
			return titleScreen;
		}

		public boolean isOnModsButton() {
			return modsButton;
		}
	}

	public enum ModsButtonStyle {
		@SerializedName("classic")
		CLASSIC(false),
		@SerializedName("replace_realms")
		REPLACE_REALMS(true),
		@SerializedName("shrink")
		SHRINK(false),
		@SerializedName("icon")
		ICON(false);

		private final boolean titleScreenOnly;

		ModsButtonStyle(boolean titleScreenOnly) {
			this.titleScreenOnly = titleScreenOnly;
		}

		public ModsButtonStyle forGameMenu() {
			if (titleScreenOnly) {
				return CLASSIC;
			}
			return this;
		}
	}
}
