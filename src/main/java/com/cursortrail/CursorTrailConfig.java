package com.cursortrail;

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
}
