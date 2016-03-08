package thermos.wrapper;

import java.util.HashMap;
import java.util.concurrent.*;

import org.bukkit.craftbukkit.util.LongHash;

import net.minecraft.world.chunk.Chunk;

public class ChunkBlockHashMap {
	
	//private final ConcurrentHashMap<Integer, Chunk[][]> map = new ConcurrentHashMap<Integer, Chunk[][]>();
	private final org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]> map = new org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]>();
	private int size = 0;
	
    public static long chunk_hash(int x, int z)
    {
        //return ((x & 0xFFFF) << 16) | (z & 0xFFFF);
        long key = LongHash.toLong(x, z);
        return LongHash.toLong((int) (key & 0xFFFFFFFFL), (int) (key >>> 32));
    }

    private static int chunk_array(int index)
    {
        return Math.abs(index % 16);
    }

    private Chunk[][] chunk_array_get(int x, int z)
    {
        Chunk[][] bunch = this.map.get(chunk_hash(x >> 4, z >> 4));
        return bunch;
    }
    private Chunk[][] chunk_array_remove(int x, int z)
    {
        Chunk[][] bunch = this.map.remove(chunk_hash(x >> 4, z >> 4));
        return bunch;
    }

    public org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]> raw()
    {
    	return this.map;
    }
    
    public int size()
    {
    	return this.size;
    }
    Chunk last = null;
    public Chunk get(int x, int z)
    {
    	if(last != null && last.xPosition == x && last.zPosition == z) return last; // Thermos prune get calls for shitty mods
    	
        Chunk[][] bunch = this.map.get(chunk_hash(x >> 4, z >> 4));
        if(bunch == null) return null;
        Chunk ref = bunch[Math.abs(x % 16)][Math.abs(z % 16)];
        if ( ref != null) last = ref;
        return ref;
    }
    
    public void put(Chunk chunk)
    {
    	   if(chunk == null)
    		   return;
    	   size++;
    	   int x = chunk.xPosition, z = chunk.zPosition;
           Chunk[][] temp_chunk_bunch = chunk_array_get(x, z);
           if(temp_chunk_bunch != null)
           {
               temp_chunk_bunch[chunk_array(x)][chunk_array(z)] = chunk;
           }
           else
           {
               temp_chunk_bunch = new Chunk[16][16];
               temp_chunk_bunch[chunk_array(x)][chunk_array(z)] = chunk;
               this.map.put(chunk_hash(x >> 4, z >> 4), temp_chunk_bunch); //Thermos - IntHash
           }
           last = chunk; // Thermos save it away for future gets
    }

    public void remove(Chunk chunk)
    {
 	   int x = chunk.xPosition, z = chunk.zPosition;
       Chunk[][] temp_chunk_bunch = chunk_array_get(x, z);
       if(temp_chunk_bunch != null)
       {
    	   if(temp_chunk_bunch[chunk_array(x)][chunk_array(z)] != null)
    	   {
    		   size--;
    		   temp_chunk_bunch[chunk_array(x)][chunk_array(z)] = null;
    	   }
       }
       if(x == last.xPosition && z == last.xPosition) // Thermos make sure no chunk is left behind
    	   last = null;
    }
}
