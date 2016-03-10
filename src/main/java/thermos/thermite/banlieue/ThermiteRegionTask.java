package thermos.thermite.banlieue;

import java.io.IOException;
import java.nio.channels.FileChannel.MapMode;

import net.minecraft.world.chunk.storage.RegionFile;

public class ThermiteRegionTask implements Runnable {
	private RegionFile rf;
	
	public ThermiteRegionTask(RegionFile rf)
	{
		this.rf = rf;
	}
	@Override
	public void run() {
		try {
			this.rf.readable = this.rf.dataFile.getChannel().map(MapMode.READ_ONLY, 0L, this.rf.dataFile.length());
			
		} catch (IOException e) {
			this.rf.readable = null;
			System.out.println("[Thermos] Error in loading file to RAM!");
			e.printStackTrace();
		}
	}
	public long getPos(int x, int z)
	{
		return 4 * ((x & 31) + (z & 31) * 32);
	}
}
