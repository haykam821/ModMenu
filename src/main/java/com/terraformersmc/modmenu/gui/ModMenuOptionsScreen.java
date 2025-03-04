package com.terraformersmc.modmenu.gui;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.config.ModMenuConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class ModMenuOptionsScreen extends GameOptionsScreen {

	private Screen previous;
	private ButtonListWidget buttonList;

	@SuppressWarnings("resource")
	public ModMenuOptionsScreen(Screen previous) {
		super(previous, MinecraftClient.getInstance().options, new TranslatableText("modmenu.options"));
		this.previous = previous;
	}


	protected void init() {
		this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
		this.buttonList.addAll(ModMenuConfig.asOptions());
		this.children.add(this.buttonList);
		this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
			ModMenuConfigManager.save();
			this.client.openScreen(this.previous);
		}));
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.buttonList.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
		super.render(matrices, mouseX, mouseY, delta);
		List<OrderedText> tooltip = getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
		if (tooltip != null) {
			this.renderOrderedTooltip(matrices, tooltip, mouseX, mouseY);
		}

	}

	public void removed() {
		ModMenuConfigManager.save();
	}
}
