package net.minecraftforge.cauldron.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public class StringArraySetting extends ArraySetting<String> {
    private String value;
    private ConfigBase config;

    public StringArraySetting(ConfigBase config, String path, String def,
            String description) {
        super(path, def, description);
        this.value = def;
        this.config = config;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
    
        config.set(path, this.value = value);
	}
    
    @Override
    public void initArr(String values)
    {    	
    	String[] vals = values.split(",");
    	
    	value_array = new ArrayList<String>(vals.length);
    	value_set = new HashSet<String>(vals.length);
    	for(String val : vals)
    	{
    		value_array.add(val);
    	}
    	value_set.addAll(value_array);
    }
}
