package cliffracerx.mods.olddaysForge.src;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.*;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkProviderBaseFinite implements IChunkProvider{
    protected World worldObj;
    protected long seed;

    public ChunkProviderBaseFinite(World world, long l){
        worldObj = world;
        seed = l;
    }

    public static int IndexFinite(int x, int y, int z){
        return x+(y*OldaysForge.IndevWidthZ+z)*OldaysForge.IndevWidthX;
    }

    protected void fillChunk(Chunk chunk, int x1, int z1){
        for (int x=0; x<16; x++){
            for (int z=0; z<16; z++){
                for (int y=0; y<OldaysForge.IndevHeight; y++){
                    byte block = OldaysForge.IndevWorld[IndexFinite(x+(x1*16), y, z+(z1*16))];
                    if (block<=0 && y != OldaysForge.IndevHeight - 31){
                        continue;
                    }
                    ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
                    if (extendedblockstorage == null)
                    {
                        extendedblockstorage = chunk.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage((y >> 4) << 4, true);
                    }
                    extendedblockstorage.setExtBlockID(x, y & 0xf, z, block);
                }
            }
        }
    }

    @Override
    public Chunk loadChunk(int i, int j){
        return provideChunk(i, j);
    }

    public void generateFiniteLevel(){}

    @Override
    public Chunk provideChunk(int i, int j){
        boolean bounds = i>=0 && i<OldaysForge.IndevWidthX/16 && j>=0 && j<OldaysForge.IndevWidthZ/16;
        Chunk chunk;
        if (bounds){
            if (OldaysForge.IndevWorld==null){
                generateFiniteLevel();
            }
            chunk = new Chunk(worldObj, i, j);
            fillChunk(chunk, i, j);
        }else{
            chunk = new BoundChunk(worldObj, i, j);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public boolean chunkExists(int i, int j){
        return true;
    }

    @Override
    public void populate(IChunkProvider ichunkprovider, int i, int j){
        boolean bounds = i>=0 && i<OldaysForge.IndevWidthX/16 && j>=0 && j<OldaysForge.IndevWidthZ/16;
        if (!bounds){
            return;
        }
        {
            for (int x = i * 16; x < (i + 1) * 16; x++){
                for (int y = 0; y < OldaysForge.IndevHeight; y++){
                    for (int z = j * 16; z < (j + 1) * 16; z++){
                        if (Block.lightValue[worldObj.getBlockId(x, y, z)]>0){
                            worldObj.updateAllLightTypes(x, y, z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate){
        return true;
    }

    @Override
    public boolean unloadQueuedChunks(){
        return false;
    }

    @Override
    public boolean canSave(){
        return true;
    }

    @Override
    public String makeString(){
        return "RandomLevelSource";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k){
        return null;
    }

    @Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        return null;
    }

    @Override
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public void recreateStructures(int par1, int par2)
    {
    }

    @Override
    public void saveExtraData()
    {
    }
}
