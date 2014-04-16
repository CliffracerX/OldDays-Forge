package cliffracerx.mods.olddaysForge.src;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.base.Optional;

public class WorldTypeIndev extends WorldType
{
    public WorldTypeIndev(int ID) {
        super(ID, "IndevWorldType");
        this.biomesForWorldType = WorldType.DEFAULT.getBiomesForWorldType();
        this.removeAllBiomes();
        {
                this.addNewBiome(BiomeGenBase.plains);
        }
    }

    @Override
    public WorldChunkManager getChunkManager(World var1)
    {
        return new WorldChunkManagerIndev(var1.getSeed(), this);
    }

    @Override
    public IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkProviderIndev(world, world.getSeed());
    }

    public void removeAllBiomes()
    {
        this.removeBiome(BiomeGenBase.plains);
        this.removeBiome(BiomeGenBase.desert);
        this.removeBiome(BiomeGenBase.forest);
        this.removeBiome(BiomeGenBase.extremeHills);
        this.removeBiome(BiomeGenBase.taiga);
        this.removeBiome(BiomeGenBase.swampland);
        this.removeBiome(BiomeGenBase.jungle);
    }

    public void addNewBiome(Optional<? extends BiomeGenBase> biome)
    {
        if (biome.isPresent()) {
            this.addNewBiome(biome.get());
        }
    }
}
