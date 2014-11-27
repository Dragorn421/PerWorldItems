package com.gmail.dragorn421.perworlditems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.dragorn421.perworlditems.TravelAction.Type;

public class PerWorldItemsManager
{

	final private Map<World,TravelAction> fromWorldTravelActions;
	final private Map<World,TravelAction> toWorldTravelActions;
	final private Map<Travel,TravelAction> fromWorldToWorldTravelActions;

	public PerWorldItemsManager(final PerWorldItemsPlugin plugin)
	{
		this.fromWorldTravelActions = new HashMap<>();
		this.toWorldTravelActions = new HashMap<>();
		this.fromWorldToWorldTravelActions = new HashMap<>();
		final ConfigurationSection c = plugin.getConfig();
		ConfigUtil.modified = false;
		this.loadSingleWorld(ConfigUtil.makeSureSectionExists(c, "exiting"), this.fromWorldTravelActions);
		this.loadSingleWorld(ConfigUtil.makeSureSectionExists(c, "entering"), this.toWorldTravelActions);
		this.loadWorldDuo(ConfigUtil.makeSureSectionExists(c, "exiting-entering"), this.fromWorldToWorldTravelActions);
		if(ConfigUtil.modified)
			PerWorldItemsPlugin.get().saveConfig();
	}

	public void apply(final PlayerTeleportEvent e)
	{
		final World from = e.getFrom().getWorld();
		final World to = e.getTo().getWorld();
		final TravelAction ta = this.getTravelAction(from, to);
		if(ta == null)
			return;
		switch(ta.getType())
		{
		case DISALLOW:
			if(this.hasDisallowedItems(e.getPlayer(), ta))
			{
				e.getPlayer().sendMessage(Messages.ITEMS_DISALLOWED.replace("{ITEMS}", ta.getDisallowedTypesAsString()));
				e.setCancelled(true);
			}
			break;
		case REMOVE_ITEMS:
			if(this.filterInventory(e.getPlayer(), ta))
			{
				e.getPlayer().sendMessage(Messages.ITEMS_REMOVED);
			}
			break;
		}
	}

	public void removeWorld(final World world)
	{
		this.fromWorldTravelActions.remove(world);
		this.toWorldTravelActions.remove(world);
		final Iterator<Entry<Travel,TravelAction>> itetta = this.fromWorldToWorldTravelActions.entrySet().iterator();
		Entry<Travel,TravelAction> etta;
		while(itetta.hasNext())
		{
			etta = itetta.next();
			if(etta.getKey().isConcernedAbout(world))
				itetta.remove();
		}
	}

	private boolean hasDisallowedItems(final Player p, final TravelAction travelAction)
	{
		for(final ItemStack item : p.getInventory().getContents())
		{
			if(item != null && travelAction.getDisallowedTypesView().contains(new ItemType(item)))
				return true;
		}
		return false;
	}

	private boolean filterInventory(final Player p, final TravelAction travelAction)
	{
		final ItemStack contents[] = p.getInventory().getContents();
		boolean modified = false;
		for(int i=0;i<contents.length;i++)
		{
			if(contents[i] != null && travelAction.getDisallowedTypesView().contains(new ItemType(contents[i])))
			{
				contents[i] = null;
				modified = true;
			}
		}
		if(modified)
		{
			p.getInventory().setContents(contents);
		}
		return modified;
	}

	private TravelAction getTravelAction(final World from, final World to)
	{
		final TravelAction fromWorld = from==null?null:this.fromWorldTravelActions.get(from);
		final TravelAction toWorld = to==null?null:this.toWorldTravelActions.get(to);
		final TravelAction fromWorldToWorld = from==null||to==null?null:this.fromWorldToWorldTravelActions.get(new Travel(from, to));
		if(fromWorld == null)
		{
			if(toWorld == null)
			{
				return fromWorldToWorld;
			}
			return toWorld.getHighestPriority(fromWorldToWorld);
		}
		return fromWorld.getHighestPriority(toWorld).getHighestPriority(fromWorldToWorld);
	}

	private void loadSingleWorld(final ConfigurationSection c, final Map<World,TravelAction> map)
	{
		World w;
		TravelAction ta;
		for(final String key : c.getKeys(false))
		{
			if(c.isConfigurationSection(key))
			{
				w = Bukkit.getWorld(key);
				if(w == null)
					PerWorldItemsPlugin.get().getLogger().warning("World \"" + key + "\" in \"" + c.getName() + "\" does not exist.");
				else
				{
					ta = this.loadTravelAction(c.getConfigurationSection(key));
					if(ta != null)
						map.put(w, ta);
				}
			}
		}
	}

	private void loadWorldDuo(final ConfigurationSection c, final Map<Travel,TravelAction> map)
	{
		ConfigurationSection cs;
		World from, to;
		TravelAction ta;
		for(final String key : c.getKeys(false))
		{
			if(c.isConfigurationSection(key))
			{
				cs = c.getConfigurationSection(key);
				from = Bukkit.getWorld(key);
				if(from == null)
				{
					PerWorldItemsPlugin.get().getLogger().warning("World \"" + key + "\" in \"" + c.getName() + "\" does not exist.");
					continue;
				}
				if(!cs.isString("to"))
				{
					PerWorldItemsPlugin.get().getLogger().warning("Destination world from \"" + key + "\" in \"" + c.getName() + "\" is not specified.");
					continue;
				}
				to = Bukkit.getWorld(cs.getString("to"));
				if(to == null)
					PerWorldItemsPlugin.get().getLogger().warning("World \"" + cs.getString("to") + "\" from \"" + key + "\" in \"" + c.getName() + "\" does not exist.");
				else
				{
					ta = this.loadTravelAction(cs);
					if(ta != null)
						map.put(new Travel(from, to), ta);
				}
			}
		}
	}

	private TravelAction loadTravelAction(final ConfigurationSection cs)
	{
		Type type;
		List<String> items;
		try {
			type = Type.valueOf(cs.getString("type", Type.DISALLOW.name()).toUpperCase());
		} catch(final IllegalArgumentException e) {
			type = Type.DISALLOW;
		}
		items = cs.getStringList("items");
		if(items == null || items.size() == 0)
		{
			PerWorldItemsPlugin.get().getLogger().warning("World \"" + cs.getName() + "\" in \"" + cs.getParent().getName() + "\" has no items defined");
			return null;
		}
		return new TravelAction(type, cs.getInt("priority", 0), ItemType.getTypes(items));
	}

}
