package com.cursortrail;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import javax.inject.Inject;


import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.events.ClientTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
		name = "Cursor Trail"
)
public class CursorTrailPlugin extends Plugin {
	private CursorTrailOverlay overlay;

	@Inject
	private Client client;

	@Inject
	private CursorTrailConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MouseManager mouseManager;

	static class QueuedPoint {
		Instant when;
		Point where;
		Color color;

		public QueuedPoint(Instant when, Point where, Color color) {
			this.when = when;
			this.where = where;
			this.color = color;
		}
	}

	protected ArrayDeque<QueuedPoint> queue;
	public void queuePoint(MouseEvent event, boolean mouse) {
		Point position = client.getMouseCanvasPosition();

		Color color = this.config.base_color();
		MenuEntry[] entries = client.getMenu().getMenuEntries();
		MenuEntry entry = entries.length > 0 ? entries[entries.length - 1] : null;
		if(entry != null) {
			switch(entry.getType()) {
				case WALK:
				case CANCEL:
					break;
				default:
					color = this.config.hover_color();
					break;
			}
		}

		if(mouse) {
			color = this.config.drag_color();
		}

		this.queue.add(new QueuedPoint(Instant.now(), position, color));
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public MouseEvent mouseMoved(MouseEvent event) {
			queuePoint(event, false);
			return event;
		}
		@Override
		public MouseEvent mouseDragged(MouseEvent event) {
			queuePoint(event, true);
			return event;
		}
	};

	@Override
	protected void startUp() throws Exception {
		this.queue = new ArrayDeque<>();

		if(overlay == null) {
			overlay = new CursorTrailOverlay(this, config);
		}
		overlayManager.add(overlay);
		mouseManager.registerMouseListener(mouseAdapter);
	}

	@Override
	protected void shutDown() throws Exception {
		this.queue = null;

		overlayManager.remove(overlay);
		mouseManager.unregisterMouseListener(mouseAdapter);
	}

	@Subscribe
	public void onClientTick(ClientTick event) {
		while(this.queue.peek() != null && Duration.between(this.queue.peek().when, Instant.now()).toMillis() > this.config.lifetime()) {
			this.queue.poll();
		}
	}

	@Provides
	CursorTrailConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(CursorTrailConfig.class);
	}
}
