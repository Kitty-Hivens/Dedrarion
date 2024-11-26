package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Custom.Block.EftoritForgeBlockEntity;
import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.HDU;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HDU.MODID);

    public static final RegistryObject<BlockEntityType<EftoritForgeBlockEntity>> EFTORIT_FORGE_BE =
            BLOCK_ENTITIES.register("eftorit_forge_be", () ->
                    BlockEntityType.Builder.of(EftoritForgeBlockEntity::new,
                            BlockRegistry.EFTORIT_FORGE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
