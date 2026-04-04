package dedrarion.registry;

import dedrarion.Dedrarion;
import dedrarion.content.block.entity.EftoritForgeEntity;
import dedrarion.content.block.entity.PedestalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("DataFlowIssue")
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dedrarion.MOD_ID);

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL_ENTITY = BLOCK_ENTITIES.register("pedestal", () ->
            BlockEntityType.Builder.of(PedestalBlockEntity::new, ModBlocks.PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<EftoritForgeEntity>> EFTORIT_FORGE_ENTITY =
            BLOCK_ENTITIES.register("eftorit_forge_entity",
                    () -> BlockEntityType.Builder.of(EftoritForgeEntity::new, ModBlocks.EFTORIT_FORGE.get()).build(null));


    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
