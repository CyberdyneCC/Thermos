package thermos.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.*;

import org.bukkit.craftbukkit.util.LongHash;

import net.minecraft.world.chunk.Chunk;

public class ChunkBlockHashMap {

	private final org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]> map = new org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]>();
	private final Chunk[] lasts = new Chunk[4];
	private int size = 0;

	public org.bukkit.craftbukkit.util.LongObjectHashMap<Chunk[][]> raw()
	{
		return this.map;
	}

	public int size()
	{
		return this.size;
	}

	public boolean bulkCheck(Collection<int[]> coords)
	{
		// FYI: this class repeats a lot of code for a reason. Rather than stupidly jump around methods,
		// this kind of low level, highly-optimized code requires us to avoid raising the stack size and do in-method
		// optimal operations
		Chunk[][] last = null; // FYI: local field does not hide a class field
		int x = -1, z = -1;
		for(int[] set : coords)
		{
			if (last != null)
			{
				if (set[0] >> 4 == x >> 4 && set[1] >> 4 == z >> 4)
				{
					x = set[0]; z = set[1];
					x %= 16;
					z %= 16;					
					if (last[(x + (x >> 31)) ^ (x >> 31)][(z + (z >> 31)) ^ (z >> 31)] == null)
					{
						return false;
					}
					x = set[0]; z = set[1];					
				}
				else
				{
					x = set[0]; z = set[1];
					last = this.map.get((((long)(x>>4))<<32L)^(z>>4));
					if (last == null)
					{
						return false;
					}
					x %= 16;
					z %= 16;
					if (last[(x + (x >> 31)) ^ (x >> 31)][(z + (z >> 31)) ^ (z >> 31)] == null)
					{
						return false;
					}		
					x = set[0]; z = set[1];					
					
				}
			}
			else
			{
				x = set[0]; z = set[1];
				last = this.map.get((((long)(x>>4))<<32L)^(z>>4));

				if (last == null)
				{
					return false;
				}
				x %= 16;
				z %= 16;
				if (last[(x + (x >> 31)) ^ (x >> 31)][(z + (z >> 31)) ^ (z >> 31)] == null)
				{
					return false;
				}
				x = set[0]; z = set[1];				
			}
		}
		
		return true;
	}
	
	public Chunk get(int x, int z)
	{
		for(Chunk last : lasts)
			if(last != null && last.xPosition == x && last.zPosition == z)
				return last;
		
		
		Chunk[][] bunch = this.map.get((((long)(x>>4))<<32L)^(z>>4));
		if(bunch == null) return null;
		
		x %= 16;
		z %= 16;
		Chunk ref = bunch[(x + (x >> 31)) ^ (x >> 31)][(z + (z >> 31)) ^ (z >> 31)];
		
		if ( ref != null)
		{
			for(int i = lasts.length - 1; i > 0; i--)
				lasts[i] = lasts[i - 1];
			
			lasts[0] = ref;
		}
		
		
		return ref;
	}

	public void put(Chunk chunk)
	{
		if(chunk == null)
			return;
		
		size++;
		int x = chunk.xPosition, z = chunk.zPosition;
		
		long chunkhash = (((long)(x>>4))<<32L)^(z>>4);
		
		Chunk[][] temp_chunk_bunch = this.map.get(chunkhash);
		
		x %= 16;
		z %= 16;
		
		x = (x + (x >> 31)) ^ (x >> 31);
		z = (z + (z >> 31)) ^ (z >> 31);
		
		if(temp_chunk_bunch != null)
		{
			temp_chunk_bunch[x][z] = chunk;
		}
		else
		{
			temp_chunk_bunch = new Chunk[16][16];
			temp_chunk_bunch[x][z] = chunk;
			this.map.put(chunkhash, temp_chunk_bunch); //Thermos
		}

		for(int i = lasts.length - 1; i > 0; i--)
			lasts[i] = lasts[i - 1];
			
		lasts[0] = chunk;
		
	}

	public void remove(Chunk chunk)
	{
		int x = chunk.xPosition, z = chunk.zPosition;
		Chunk[][] temp_chunk_bunch = this.map.get((((long)(x>>4))<<32L)^(z>>4));
		
		x %=16;
		z %=16;
		
		x = (x + (x >> 31)) ^ (x >> 31);
		z = (z + (z >> 31)) ^ (z >> 31);		
		
		if(temp_chunk_bunch != null)
		{
			if(temp_chunk_bunch[x][z] != null)
			{
				size--;
				temp_chunk_bunch[x][z] = null;
			}
		}
		
		for(int i = 0; i < lasts.length; i++)
		{
			if(lasts[i] != null && lasts[i].xPosition == chunk.xPosition && lasts[i].zPosition == chunk.zPosition)
			{
				lasts[i] = lasts[0];
				lasts[0] = null;
				break;
			}
		}
	}
}
