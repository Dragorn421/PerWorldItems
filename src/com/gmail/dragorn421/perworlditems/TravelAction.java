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
	final private Collection<ItemType> disallowedTypes;
	final private Collection<ItemType> disallowedTypesView;
	final private String disallowedTypesString;

	public TravelAction(final Type type, final int priority, final Collection<ItemType> disallowedTypes)
	{
		this.type = type;
		this.priority = priority;
		this.disallowedTypes = new HashSet<>(disallowedTypes);
		this.disallowedTypesView = Collections.unmodifiableCollection(this.disallowedTypes);
		final StringBuilder sb = new StringBuilder();
		for(final ItemType it : this.disallowedTypes)
		{
			if(sb.length() != 0)
				sb.append(", ");
			sb.append(it.toString());
		}
		this.disallowedTypesString = sb.toString();
	}

	public Type getType()
	{
		return this.type;
	}

	public TravelAction getHighestPriority(final TravelAction travelAction)
	{
		return travelAction==null?this:(travelAction.priority>this.priority?travelAction:this);
	}

	public Collection<ItemType> getDisallowedTypesView()
	{
		return this.disallowedTypesView;
	}

	public String getDisallowedTypesAsString()
	{
		return this.disallowedTypesString;
	}

}
