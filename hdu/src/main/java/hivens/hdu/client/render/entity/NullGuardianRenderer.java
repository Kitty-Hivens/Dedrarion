package hivens.hdu.client.render.entity; // Убедись, что пакет правильный

import hivens.hdu.client.model.entity.NullGuardianModel;
import hivens.hdu.common.entity.NullGuardianEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NullGuardianRenderer extends GeoEntityRenderer<NullGuardianEntity> {
    public NullGuardianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NullGuardianModel());
    }
}