package com.cursortrail;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;

import java.awt.event.MouseEvent;
import java.awt.Color;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.Point;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;

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
 public void queuePoint(MouseEvent event, Color color) {
    this.queue.add(new QueuedPoint(Instant.now(), client.getMouseCanvasPosition(), color));
 }

 private final MouseAdapter mouseAdapter = new MouseAdapter() {
  @Override
  public MouseEvent mouseMoved(MouseEvent event) {
    queuePoint(event, Color.GREEN);
    return event;
  }
  @Override
  public MouseEvent mouseDragged(MouseEvent event) {
    queuePoint(event, Color.RED);
    return event;
  }
 };

	@Override
	protected void startUp() throws Exception {
  this.queue = new ArrayDeque<QueuedPoint>();

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
   while(Duration.between(this.queue.peek().when, Instant.now()).toMillis() > this.config.lifetime()) {
    this.queue.poll();
   }
 }

	@Provides
	CursorTrailConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(CursorTrailConfig.class);
	}
}
