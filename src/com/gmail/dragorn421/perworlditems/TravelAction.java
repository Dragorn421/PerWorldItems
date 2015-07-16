package com.gmail.dragorn421.perworlditems;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class TravelAction
{

	public enum Type
	{
	
		DISALLOW,
		REMOVE_ITEMS;
	
	}

	final private Type type;
	final private int priority;
	final private Collection<ItemType> itemTypes;
	final private boolean whitelist;
	final private boolean ignoreSameWorld;

	final private Collection<ItemType> itemTypesView;
	final private String itemTypesString;

	public TravelAction(final Type type, final int priority, final Collection<ItemType> disallowedTypes, final boolean whitelist, final boolean ignoreSameWorld)
	{
		this.type = type;
		this.priority = priority;
		this.itemTypes = new HashSet<>(disallowedTypes);
		this.whitelist = whitelist;
		this.ignoreSameWorld = ignoreSameWorld;
		this.itemTypesView = Collections.unmodifiableCollection(this.itemTypes);
		final StringBuilder sb = new StringBuilder();
		for(final ItemType it : this.itemTypes)
		{
			if(sb.length() != 0)
				sb.append(", ");
			sb.append(it.toString());
		}
		this.itemTypesString = sb.toString();
	}

	public Type getType()
	{
		return this.type;
	}

	public TravelAction getHighestPriority(final TravelAction travelAction)
	{
		return travelAction==null?this:(travelAction.priority>this.priority?travelAction:this);
	}

	public boolean isWhitelist()
	{
		return this.whitelist;
	}

	public boolean ignoreSameWorld()
	{
		return this.ignoreSameWorld;
	}

	public Collection<ItemType> getItemTypesView()
	{
		return this.itemTypesView;
	}

	public String getItemTypesAsString()
	{
		return this.itemTypesString;
	}

}
