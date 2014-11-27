package com.gmail.dragorn421.perworlditems;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PerWorldItemsPlugin extends JavaPlugin implements Listener
{

	static private PerWorldItemsPlugin instance;

	private YAMLConfigHandler yamlConfigHandler;

	private PerWorldItemsManager pwiManager;

	@Override
	public void onEnable()
	{
		PerWorldItemsPlugin.instance = this;
		try {
			this.yamlConfigHandler = new YAMLConfigHandler(this);
		} catch (final IOException e) {
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run()
				{
					Bukkit.getPluginManager().disablePlugin(PerWorldItemsPlugin.instance);
				}
			}, 0L);
			throw new IllegalStateException("Unable to load configuration.", e);
		}
		if(Messages.load(ConfigUtil.makeSureSectionExists(this.getConfig(), "messages")))
			this.saveConfig();
		this.pwiManager = new PerWorldItemsManager(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		super.getLogger().info(super.getName() + " enabled!");
	}

	@Override
	public void onDisable()
	{
		super.getLogger().info(super.getName() + " disabled!");
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerTeleport(final PlayerTeleportEvent e)
	{
		this.pwiManager.apply(e);
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerTeleport(final PlayerPortalEvent e)
	{
		this.pwiManager.apply(e);
	}

	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onWorldUnload(final WorldUnloadEvent e)
	{
		this.pwiManager.removeWorld(e.getWorld());
	}

	@Override
	public FileConfiguration getConfig()
	{
		return this.yamlConfigHandler.getConfig();
	}

	@Override
	public void reloadConfig()
	{
		this.yamlConfigHandler.reloadConfigSilent();
	}

	@Override
	public void saveConfig()
	{
		this.yamlConfigHandler.save();
	}

	static public PerWorldItemsPlugin get()
	{
		return PerWorldItemsPlugin.instance;
	}

}
