package cliffracerx.mods.olddaysForge.src;

import net.minecraft.src.*;
import net.minecraft.world.World;

public class ChunkProviderIndev extends ChunkProviderBaseFinite{
    public ChunkProviderIndev(World world, long l){
        super(world, l);
    }

    public void generateFiniteLevel(){
        OldaysForge.generateIndevLevel(seed);
    }
}
