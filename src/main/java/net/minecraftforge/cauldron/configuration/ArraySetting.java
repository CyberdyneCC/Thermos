package net.minecraftforge.cauldron.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class ArraySetting<T> extends Setting<String>{
	public ArraySetting(String path, String def, String description) {
		super(path, def, description);
		initArr(def);
	}

	protected HashSet<T> value_set;
	protected ArrayList<T> value_array;
	
	public boolean contains(T t)
	{
		return value_set.contains(t);
	}
	
	public T get(int i)
	{
		if(i < 0 || i > value_array.size() - 1) return null;
		
		return value_array.get(i);
		
	}
	
	public abstract void initArr(String array);
	
}
