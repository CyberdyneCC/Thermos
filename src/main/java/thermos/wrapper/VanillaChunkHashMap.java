package thermos.wrapper;

import gnu.trove.map.TLongObjectMap;
import net.minecraft.world.chunk.Chunk;

import org.bukkit.craftbukkit.util.LongHash;

public class VanillaChunkHashMap extends LongHashMapTrove<Chunk> {
    public VanillaChunkHashMap(TLongObjectMap<Chunk> map) {
        super(map);
    }

    private static long V2B(long key) {
        return LongHash.toLong((int) (key & 0xFFFFFFFFL), (int) (key >>> 32));
    }
    
    @Override
    public void add(long key, Object value) {
        super.add(V2B(key), value);
    }
    
    @Override
    public boolean containsItem(long key) {
        return super.containsItem(V2B(key));
    }
    
    @Override
    public Object getValueByKey(long key) {
        return super.getValueByKey(V2B(key));
    }
    
    @Override
    public Object remove(long key) {
        return super.remove(V2B(key));
    }
}
