package com.cursortrail;

import com.cursortrail.CursorTrailPlugin.QueuedPoint;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.api.Point;

class CursorTrailOverlay extends Overlay {
  private final CursorTrailPlugin plugin;
  private final CursorTrailConfig config;

  public CursorTrailOverlay(CursorTrailPlugin plugin, CursorTrailConfig config) {
    this.plugin = plugin;
    this.config = config;

    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ALWAYS_ON_TOP);
    setPriority(1.0f);
  }

  @Override
  public Dimension render(Graphics2D graphics) {
  graphics.setStroke(new BasicStroke((float) this.config.width()));

    Point last = null;
    for(QueuedPoint point : plugin.queue) {
      if(last != null) {
        Line2D.Double line = new Line2D.Double(last.getX(), last.getY(), point.where.getX(), point.where.getY());
        graphics.setColor(point.color);
        graphics.draw(line);
      }
      last = point.where;
    }
    return null;
  }
}
