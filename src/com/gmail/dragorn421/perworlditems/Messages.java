package com.gmail.dragorn421.perworlditems;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class Messages
{

	/**
	 * {ITEMS}
	 */
	static public String ITEMS_DISALLOWED = "These item types are disallowed: {ITEMS}";

	/**
	 * {ITEMS}
	 */
	static public String ITEMS_ALLOWED = "Only these item types are allowed: {ITEMS}";

	static public String ITEMS_REMOVED = "Some items got removed from your inventory because they were disallowed!";

	static public boolean load(final ConfigurationSection cs)
	{
		boolean modified = false;
		if(!cs.isString("items-disallowed"))
		{
			modified = true;
			cs.set("items-disallowed", Messages.ITEMS_DISALLOWED);
		}
		Messages.ITEMS_DISALLOWED = ChatColor.translateAlternateColorCodes('&', cs.getString("items-disallowed"));
		if(!cs.isString("items-allowed"))
		{
			modified = true;
			cs.set("items-allowed", Messages.ITEMS_ALLOWED);
		}
		Messages.ITEMS_ALLOWED = ChatColor.translateAlternateColorCodes('&', cs.getString("items-allowed"));
		if(!cs.isString("items-removed"))
		{
			modified = true;
			cs.set("items-removed", Messages.ITEMS_REMOVED);
		}
		Messages.ITEMS_REMOVED = ChatColor.translateAlternateColorCodes('&', cs.getString("items-removed"));
		return modified;
	}

}
