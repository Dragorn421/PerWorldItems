package com.gmail.dragorn421.perworlditems;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtil
{

	static public boolean modified = false;

	static public ConfigurationSection makeSureSectionExists(final ConfigurationSection config, final String sectionKey)
	{
		if(!config.isConfigurationSection(sectionKey))
		{
			config.set(sectionKey, null);
			modified = true;
			return config.createSection(sectionKey);
		}
		return config.getConfigurationSection(sectionKey);
	}

}
