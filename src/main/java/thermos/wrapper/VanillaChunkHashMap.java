package thermos.wrapper;

import gnu.trove.map.TLongObjectMap;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.concurrent.*;

import org.bukkit.craftbukkit.util.LongHash;

public class VanillaChunkHashMap extends LongHashMap {
	private final ChunkBlockHashMap chunkt_TH;
	private final HashMap<Integer,Chunk> vanilla = new HashMap<Integer,Chunk>();
    public VanillaChunkHashMap(ChunkBlockHashMap chunkt_TH) {
        this.chunkt_TH = chunkt_TH;
    }

    /*private static long V2B(long key) {
        return LongHash.toLong((int) (key & 0xFFFFFFFFL), (int) (key >>> 32));
    }*/
    
    public HashMap<Integer,Chunk> rawVanilla()
    {
    	return vanilla;
    }
    
    public ChunkBlockHashMap rawThermos()
    {
    	return chunkt_TH;
    }
    
    public int size()
    {
    	return this.chunkt_TH.size();
    }
    
    @Override
    public void add(long key, Object value) {
    	if(value instanceof Chunk)
    	{
    		Chunk c = (Chunk) value;
    		chunkt_TH.put(c);
    		vanilla.put(super.getHashedKey(key), c);
    	}
    }

    
    @Override
    public boolean containsItem(long key) {
        return vanilla.containsKey(super.getHashedKey(key));
    }
    
    @Override
    public Object getValueByKey(long key) {
        return vanilla.get(super.getHashedKey(key));
    }
    
    @Override
    public Object remove(long key) {
        Object o = vanilla.remove(super.getHashedKey(key));
        if(o instanceof Chunk) // Thermos - Use our special map
        {
        	Chunk c = (Chunk)o;
        	chunkt_TH.remove(c);
        }
        return o;
    }
    
    @Override
    public int getNumHashElements()
    {
    	return this.vanilla.size();
    }
}
