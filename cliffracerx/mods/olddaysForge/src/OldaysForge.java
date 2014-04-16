package cliffracerx.mods.olddaysForge.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "OldaysForge", name = "OldDays: Forge Edition",
        version = "Alpha 0.01a", customProperties = {@CustomProperty(k = "author", v = "CliffracerX")})
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class OldaysForge
{
    public static int THEME_NORMAL = 0;
    public static int THEME_HELL = 1;
    public static int THEME_WOODS = 2;
    public static int THEME_PARADISE = 3;

    public static int TYPE_INLAND = 0;
    public static int TYPE_ISLAND = 1;
    public static int TYPE_FLOATING = 2;
    public static int TYPE_FLAT = 3;
    public static int IndevWidthX = 512;
    public static int IndevWidthZ = 512;
    private static final int MapTheme = THEME_WOODS;
    private static final int IndevMapType = TYPE_FLOATING;

    //public final static CreativeTabs tab = new TaintedTab("taintedTab");
    //public final static StepSound soundSplutFootstep = new StepSound("OldaysForge:splut", 1, 1);
    //public static EnumArmorMaterial gasMask = EnumHelper.addArmorMaterial("cliffiesGasMask", 500, new int[]{2, 6, 5, 2}, 9);
    public static boolean genNewOresInOldDims = false;
    //public static int rMaskI;
    /*public final static Block rTaint = new NormalTaint(2000,
            Material.ground, "rTaint").setHardness(0.25F)
            .setStepSound(soundSplutFootstep)
            .setUnlocalizedName("rTaint").setCreativeTab(tab);*/
    //public final static Item rGasMask = new CustomArmor(8192, 0, "rMask", gasMask, rMaskI);
    
    @Instance("OldaysForge")
    public static OldaysForge instance;
    
    @SidedProxy(clientSide = "cliffracerx.mods.olddaysForge.src.ClientProxy",
            serverSide = "cliffracerx.mods.olddaysForge.src.CommonProxy")
    public static CommonProxy proxy;

    public static int IndevHeight = 64;
    public static byte[] IndevWorld = null;
    private static int SurrGroundType;
    private static int SurrGroundHeight;
    private static int SurrWaterType;
    private static int SurrWaterHeight;
    private static int IndevSpawnX;
    private static int IndevSpawnY;
    private static int IndevSpawnZ;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //Openblocks donation station support.  Support the modders, without them needing to resort to adfly!  =D
        FMLInterModComms.sendMessage("OldaysForge", "donateUrl", "http://cliffracerx.github.io/OldaysForge/donate.html");
        //Config.
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        genNewOresInOldDims = config.get(Configuration.CATEGORY_GENERAL, "Generate new ores in old-worldgeneration?", false).getBoolean(false);
        IndevHeight = config.get(Configuration.CATEGORY_GENERAL, "Height of Indev worlds", 64).getInt(64);
        IndevWidthX = config.get(Configuration.CATEGORY_GENERAL, "Width (X) of Indev worlds", 512).getInt(512);
        IndevWidthZ = config.get(Configuration.CATEGORY_GENERAL, "Width (Z) Indev worlds", 512).getInt(512);
        setIndevBounds(TYPE_FLOATING, THEME_WOODS);
        // saving the configuration to its file
        config.save();
    }
    
    public static void setIndevBounds(int type, int theme){
        SurrGroundType = Block.grass.blockID;
        SurrWaterType = theme==THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
        if (type==5){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-2;
        }else if (type==TYPE_FLOATING){
            SurrGroundHeight = -128;
            SurrWaterHeight = SurrGroundHeight+1;
        }else if (type==TYPE_ISLAND){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-9;
        }else{
            SurrGroundHeight = IndevHeight-31;
            SurrWaterHeight = SurrGroundHeight-16;
        }
    }
    
    @EventHandler
    @SideOnly(Side.CLIENT)
    public void load(FMLInitializationEvent event)
    {
        ClientProxy.registerRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //Block naming and registering
        /*LanguageRegistry.addName(rTaint, "Red Tainted goo (lv 0)");
        GameRegistry.registerBlock(rTaint, "rTaint");*/
        //Name items
        //LanguageRegistry.addName(rGasMask, "Red gas mask");
        //Crafting
        /*GameRegistry.addRecipe(new ItemStack(rGasMask, 1), "###",
                "#$#", "###", '$', rFFieldAL, '#', rATaint);*/
        int worldTypeToTry=0;
        boolean success=false;
        while(!success)
        {
        if(WorldType.worldTypes[worldTypeToTry]==null)
        {
            WorldType WTInd = new WorldTypeIndev(worldTypeToTry);
            WorldType.worldTypes[worldTypeToTry]=WTInd;
            success=true;
        }
        else
        {
            worldTypeToTry++;
        }
        }
    }

    public static void generateIndevLevel(long seed){
        IndevGenerator gen2 = new IndevGenerator(seed);
        if (OldaysForge.IndevMapType==OldaysForge.TYPE_ISLAND){
            gen2.island=true;
        }
        if (OldaysForge.IndevMapType==OldaysForge.TYPE_FLOATING){
            gen2.floating=true;
        }
        if (OldaysForge.IndevMapType==OldaysForge.TYPE_FLAT){
            gen2.flat=true;
        }
        gen2.theme=OldaysForge.MapTheme;
       OldaysForge.IndevWorld = gen2.generateLevel("Created with NBXlite!", OldaysForge.IndevWidthX, OldaysForge.IndevWidthZ, OldaysForge.IndevHeight);
        OldaysForge.IndevSpawnX = gen2.spawnX;
        OldaysForge.IndevSpawnY = gen2.spawnY;
        OldaysForge.IndevSpawnZ = gen2.spawnZ;
    }

    public static int getSkyLightInBounds(int par2)
    {
        int sky = 15;
        if (par2<SurrWaterHeight){
            if (Block.blocksList[SurrWaterType].blockMaterial!=Material.lava){
                sky-=3*(SurrWaterHeight-par2);
            }else{
                sky = 0;
            }
        }
        if (par2<SurrGroundHeight){
            sky = 0;
        }
        if (sky<0){
            sky = 0;
        }
        return sky;
    }
    
    public static void setIndevBounds(int groundtype, int groundheight, int watertype, int waterheight){
        SurrGroundType = groundtype;
        SurrGroundHeight = groundheight;
        SurrWaterType = watertype;
        SurrWaterHeight = waterheight;
    }

    public static int getBlockLightInBounds(int par2)
    {
        int block = 0;
        if (par2>=SurrGroundHeight && SurrWaterHeight>SurrGroundHeight){
            if (par2<SurrWaterHeight){
                block = Block.lightValue[SurrWaterType];
            }else{
                block = Block.lightValue[SurrWaterType]-(par2-SurrWaterHeight)-1;
            }
        }
        if (block<0){
            block = 0;
        }
        return block;
    }

    public static int getBlockIdInBounds(int par2)
    {
        if (par2<SurrGroundHeight-1){
            return Block.bedrock.blockID;
        }
        if (par2<SurrGroundHeight){
            if ((par2<SurrWaterHeight || SurrWaterType==Block.lavaStill.blockID) && SurrGroundType==Block.grass.blockID){
                return Block.dirt.blockID;
            }
            return SurrGroundType;
        }
        if (par2<SurrWaterHeight){
            return SurrWaterType;
        }
        return 0;
    }
}
