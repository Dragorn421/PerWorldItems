package com.gmail.dragorn421.perworlditems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class ItemType implements Cloneable, Serializable
{

	final static private long serialVersionUID = 5753745129486137504L;

	final public Material mat;
	final public byte data;

	public ItemType(final ItemType bi)
	{
		this(bi.mat, bi.data);
	}

	public ItemType(final ItemStack is)
	{
		this(is.getType(), ItemType.getData(is));
	}

	public ItemType(final Block b)
	{
		this(b.getType(), ItemType.getData(b));
	}

	public ItemType(final Material mat)
	{
		this(mat, -1);
	}

	public ItemType(final Material mat, final int data)
	{
		this.mat = mat;
		this.data = (byte) ((data<0)?(-1):(data));
	}

	@Override
	public boolean equals(final Object o)
	{
		if(o instanceof ItemType)
			return (this.mat == ((ItemType)o).mat) && ((this.data==-1)?(true):((((ItemType)o).data==-1)?(true):(this.data==((ItemType)o).data)));
		return super.equals(o);
	}

	@Override
	public ItemType clone()
	{
		return new ItemType(this);
	}

	@Override
	public String toString()
	{
		return this.mat.toString() + ((this.data==-1)?(""):(":" + Byte.toString(this.data)));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.mat);
	}

	@SuppressWarnings("deprecation")
	static private byte getData(final Block b)
	{
		return b.getData();
	}

	@SuppressWarnings("deprecation")
	private static int getData(ItemStack is)
	{
		return is.getData().getData();
	}

	@SuppressWarnings("deprecation")
	static public ItemType getType(final String s)
	{
		final String args[] = s.split(":");
		Material m;
		byte d;
		try {
			m = Material.getMaterial(Integer.parseInt(args[0]));
		} catch(NumberFormatException e) {
			m = Material.getMaterial(args[0]);
		}
		if(args.length == 1)
			d = -1;
		else
		{
			try {
				d = Byte.parseByte(args[1]);
			} catch(NumberFormatException e) {
				d = -1;
			}
		}
		return new ItemType(m, d);
	}

	static public List<ItemType> getTypes(final List<String> strings)
	{
		final List<ItemType> itl = new ArrayList<>(strings.size());
		for(int i=0,n=strings.size();i<n;i++)
			itl.add(ItemType.getType(strings.get(i)));
		return itl;
	}

}
