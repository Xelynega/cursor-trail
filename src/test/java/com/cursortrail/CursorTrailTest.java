package com.cursortrail;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CursorTrailTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CursorTrailPlugin.class);
		RuneLite.main(args);
	}
}
