package dedrarion.client.render.entity; // Убедись, что пакет правильный

import dedrarion.client.model.entity.NullGuardianModel;
import dedrarion.common.entity.NullGuardianEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NullGuardianRenderer extends GeoEntityRenderer<NullGuardianEntity> {
    public NullGuardianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NullGuardianModel());
    }
}