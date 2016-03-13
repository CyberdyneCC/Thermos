package net.minecraftforge.cauldron.configuration;

import java.util.ArrayList;

public class IntArraySetting extends Setting<Integer[]> {
    private Integer[] value;
    private ConfigBase config;
    
    public IntArraySetting(ConfigBase config, String path, Integer[] def, String description)
    {
        super(path, def, description);
        this.value = def;
        this.config = config;
    }

    @Override
    public Integer[] getValue()
    {
        return value;
    }

    @Override
    public void setValue(String value)
    {
    	String[] vals = value.split(",");
    	ArrayList<Integer> minty = new ArrayList<Integer>(vals.length);
        for(int i = 0; i < vals.length; i++)
        {
        	try
        	{
        		minty.add(Integer.parseInt(vals[i]));
        	}
        	catch(Exception e)
        	{
        		
        	}
        	catch(Error eeek)
        	{
        		
        	}
        }
        this.value = new Integer[minty.size()];
        for(int i = 0; i < this.value.length; i++)
        {
        	this.value[i] = minty.get(i);
        }
        config.set(path, this.value);
    }
}
