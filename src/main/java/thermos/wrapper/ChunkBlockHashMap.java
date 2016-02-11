package thermos.wrapper;

import java.util.HashMap;

import net.minecraft.world.chunk.Chunk;

public class ChunkBlockHashMap {
	
	private final HashMap<Integer, Chunk[][]> map = new HashMap<Integer, Chunk[][]>();
	private int size = 0;
	
    private static int chunk_hash(int x, int z)
    {
        return ((x & 0xFFFF) << 16) | (z & 0xFFFF);
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

    public HashMap<Integer,Chunk[][]> raw()
    {
    	return this.map;
    }
    
    public int size()
    {
    	return this.size;
    }
    
    public Chunk get(int x, int z)
    {
        Chunk[][] bunch = this.map.get(chunk_hash(x >> 4, z >> 4));
        if(bunch == null) return null;
        return bunch[Math.abs(x % 16)][Math.abs(z % 16)];
    }
    
    public void put(Chunk chunk)
    {
    	   if(chunk == null)return;
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
    }
}
