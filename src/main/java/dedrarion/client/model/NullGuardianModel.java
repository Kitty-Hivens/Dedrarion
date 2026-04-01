package dedrarion.client.model;

import dedrarion.Dedrarion;
import dedrarion.content.entity.NullGuardianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NullGuardianModel extends GeoModel<NullGuardianEntity> {

    /**
     * Этот метод указывает путь к файлу 3D-модели (.geo.json),
     * который мы создадим в Blockbench.
     */
    @Override
    public ResourceLocation getModelResource(NullGuardianEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "geo/null_guardian.geo.json");
    }

    /**
     * Этот метод указывает путь к файлу текстуры (.png),
     * которая будет наложена на модель.
     */
    @Override
    public ResourceLocation getTextureResource(NullGuardianEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "textures/entity/null_guardian.png");
    }

    /**
     * Этот метод указывает путь к файлу анимаций (.animation.json),
     * который мы также создадим в Blockbench.
     */
    @Override
    public ResourceLocation getAnimationResource(NullGuardianEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "animations/null_guardian.animation.json");
    }
}