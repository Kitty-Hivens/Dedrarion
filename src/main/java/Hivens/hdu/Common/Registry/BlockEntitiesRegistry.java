package Hivens.hdu.Common.Registry;

import Hivens.hdu.Common.Custom.Block.Entity.PedestalBlockEntity;
import Hivens.hdu.HDU;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntitiesRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HDU.MODID);

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL_ENTITY = BLOCK_ENTITIES.register("pedestal", () ->
            BlockEntityType.Builder.of(PedestalBlockEntity::new, BlockRegistry.PEDESTAL.get()).build(null));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}