package com.cursortrail;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Iterator;


import com.cursortrail.CursorTrailPlugin.QueuedPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

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

    Iterator<QueuedPoint> points = this.plugin.queue.iterator();

    if(!points.hasNext()) {
      return null;
    }

    double minimum = 0.0F;
    double maximum = this.config.minimum();
    QueuedPoint last = points.next();
    while(points.hasNext()) {
      QueuedPoint point = points.next(); 
      if(point.where.distanceTo(last.where) < minimum) {
        continue;
      }

      Line2D.Double line = new Line2D.Double(last.where.getX(), last.where.getY(), point.where.getX(), point.where.getY());

      Color blend = new Color(
          (point.color.getRed() + last.color.getRed()) / 2,
          (point.color.getGreen() + last.color.getGreen()) / 2,
          (point.color.getBlue() + last.color.getBlue()) / 2,
          255);

      graphics.setColor(blend);
      graphics.draw(line);
      last = point;

      minimum = Math.min(maximum, minimum + 0.5f);
    }
    return null;
  }
}
