package dedrarion.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Soft translucent particle used by the Magic Detector's firefly system.
 * <p>
 * Features smooth fade-in/out, full-bright lighting, and configurable
 * color/size/lifetime. The sprite set is cached statically so that
 * {@link #create(ClientLevel, double, double, double, double, double, double, float, float, float, float, float, int)}
 * can be used from any client-side code without going through
 * {@code level.addParticle()}.
 */
public class SoftGlowParticle extends TextureSheetParticle {

    @Nullable
    private static SpriteSet spriteSet;

    private final float baseAlpha;
    private final float baseSize;

    protected SoftGlowParticle(ClientLevel level,
                                double x, double y, double z,
                                double xd, double yd, double zd,
                                float r, float g, float b,
                                float alpha, float size, int lifetime) {
        super(level, x, y, z, 0, 0, 0);
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.baseAlpha = alpha;
        this.alpha = 0f; // Start invisible, fade in
        this.baseSize = size;
        this.quadSize = size;
        this.lifetime = lifetime;
        this.hasPhysics = false;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }

    /**
     * Stores the sprite set during particle provider registration.
     * Must be called from {@link Provider#Provider(SpriteSet)}.
     */
    public static void setSpriteSet(@Nullable SpriteSet sprites) {
        spriteSet = sprites;
    }

    /**
     * Creates a colored soft glow particle and assigns the cached sprite.
     * Use this instead of {@code level.addParticle()} when you need to control color.
     * <p>
     * Add the returned particle to the engine via
     * {@code Minecraft.getInstance().particleEngine.add(particle)}.
     *
     * @return configured particle, or {@code null} if sprites aren't loaded yet
     */
    @Nullable
    public static SoftGlowParticle create(ClientLevel level,
                                           double x, double y, double z,
                                           double xd, double yd, double zd,
                                           float r, float g, float b,
                                           float alpha, float size, int lifetime) {
        if (spriteSet == null) return null;
        SoftGlowParticle p = new SoftGlowParticle(level, x, y, z, xd, yd, zd,
                r, g, b, alpha, size, lifetime);
        p.pickSprite(spriteSet);
        return p;
    }

    // --- Tick ---

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float life = (float) this.age / this.lifetime;

        // Smooth fade in (first 15%) and out (last 25%)
        if (life < 0.15f) {
            this.alpha = baseAlpha * (life / 0.15f);
        } else if (life > 0.75f) {
            this.alpha = baseAlpha * (1f - (life - 0.75f) / 0.25f);
        } else {
            this.alpha = baseAlpha;
        }

        // Gentle shrink over lifetime
        this.quadSize = baseSize * (1f - life * 0.25f);

        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.95;
        this.yd *= 0.95;
        this.zd *= 0.95;
    }

    // --- Rendering ---

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 0xF000F0; // Full bright — fireflies glow
    }

    // --- Provider ---

    /**
     * Registered via {@code RegisterParticleProvidersEvent}.
     * The default provider creates white particles; for colored ones use
     * {@link SoftGlowParticle#create} directly.
     */
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
            SoftGlowParticle.setSpriteSet(sprites);
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            SoftGlowParticle particle = new SoftGlowParticle(level, x, y, z, xd, yd, zd,
                    1f, 1f, 1f, 0.5f, 0.12f, 30);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
