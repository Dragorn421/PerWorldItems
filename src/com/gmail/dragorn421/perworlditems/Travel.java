package com.gmail.dragorn421.perworlditems;

import java.util.Objects;

import org.bukkit.World;

public class Travel
{

	final public World from;
	final public World to;

	public Travel(final World from, final World to)
	{
		if(from == null || to == null)
			throw new IllegalArgumentException((from==null?"from is null ":"") + (to==null?"to is null":""));
		this.from = from;
		this.to = to;
	}

	public boolean isConcernedAbout(final World world)
	{
		return world != null && (world == this.from || world == this.to);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof Travel)
		{
			final Travel t = (Travel) obj;
			return this.from == t.from && this.to == t.to;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.from, this.to);
	}

}
