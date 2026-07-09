package com.cursortrail;

import java.awt.Color;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cursor-trail")
public interface CursorTrailConfig extends Config {
	@ConfigItem(
			keyName = "width",
			name = "Width",
			description = "Width of the trail in px"
	)
	default double width() {
		return 1.0f;
	}

	@ConfigItem(
			keyName = "lifetime",
			name = "Lifetime",
			description = "How long to keep points in ms"
	)
	default int lifetime() {
		return 500;
	}

	@ConfigItem(
			keyName = "minimum",
			name = "Minimum",
			description = "Minumum distance between points"
	)
	default double minimum() {
		return 5.0f;
	}

	@ConfigItem(
			keyName = "base_color",
			name = "Base Color",
			description = "Color when not hovering or dragging"
	)
	default Color base_color() {
		return Color.GREEN;
	}

	@ConfigItem(
			keyName = "hover_color",
			name = "Hover Color",
			description = "Color while hovering an entity"
	)
	default Color hover_color() {
		return Color.YELLOW;
	}

	@ConfigItem(
			keyName = "drag_color",
			name = "Drag Color",
			description = "Color when dragging the mouse"
	)
	default Color drag_color() {
		return Color.RED;
	}
}
