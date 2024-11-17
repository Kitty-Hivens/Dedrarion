package Hivens.hdu.Common.entities.TheDestroyedMechanism;

import Hivens.hdu.HDU;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BossEntity extends Monster { // Предполагаем, что это класс сущности, наследующий от Monster
    private int health; // Текущее здоровье
    private int maxHealth; // Максимальное здоровье
    private int phase; // Текущая фаза боя

    public BossEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.maxHealth = 1200; // Установим максимальное здоровье по умолчанию (обычный)
        this.health = maxHealth;
        this.phase = 1; // Начинаем с первой фазы
    }

    @Override
    public void tick() {
        super.tick();
        updatePhase(); // Проверяем и обновляем фазу
        performBehavior(); // Выполняем поведение в зависимости от фазы
    }

    private void updatePhase() {
        if (health <= 0) {
            die(); // Убиваем босса
        } else if (health <= maxHealth * 0.2) {
            phase = 6; // Последняя фаза
        } else if (health <= maxHealth * 0.3) {
            phase = 5; // Фаза 5
        } else if (health <= maxHealth * 0.5) {
            phase = 4; // Фаза 4
        } else if (health <= maxHealth * 0.6) {
            phase = 3; // Фаза 3
        } else if (health <= maxHealth * 0.8) {
            phase = 2; // Фаза 2
        }
    }

    private void performBehavior() {
        switch (phase) {
            case 1:
                phaseOneBehavior();
                break;
            case 2:
                phaseTwoBehavior();
                break;
            case 3:
                phaseThreeBehavior();
                break;
            case 4:
                phaseFourBehavior();
                break;
            case 5:
                phaseFiveBehavior();
                break;
            case 6:
                phaseSixBehavior();
                break;
        }
    }

    private void phaseOneBehavior() {
        // Логика фазы 1
        // Атакуем ближним боем и используем газовый впрыск
        if (isPlayerInRange()) {
            meleeAttack();
        }
        if (shouldUseGasInjection()) {
            useGasInjection();
        }
    }

    private void phaseTwoBehavior() {
        // Логика фазы 2
        // Увеличиваем скорость и добавляем лазерную атаку
        increaseSpeed(1.2);
        if (isPlayerInRange()) {
            meleeAttack();
            useLaserAttack();
        }
    }

    private void phaseThreeBehavior() {
        // Логика фазы 3
        // Отходим от игрока и используем направляемый лазер
        retreatFromPlayer();
        useGuidedLaser();
    }

    private void phaseFourBehavior() {
        // Логика фазы 4
        // Чередуем дальние и ближние атаки, разбрасываем мины
        if (shouldPlaceMines()) {
            placeMines();
        }
        useLaserAttack();
    }

    private void phaseFiveBehavior() {
        // Логика фазы 5
        // Сосредотачиваемся на ближних атаках и отдаляемся после атаки
        meleeAttack();
        retreatAfterAttack();
    }

    private void phaseSixBehavior() {
        // Логика фазы 6
        // Используем быстрый направляемый лазер
        useFastGuidedLaser();
    }

    private void die() {
        // Логика смерти босса
        createFakeExplosions();
        dropLoot();
    }

    // Примеры методов для атак и поведения
    private void meleeAttack() {
        // Логика ближней атаки
    }

    private void useGasInjection() {
        // Логика газового впрыска
    }

    private void useLaserAttack() {
        // Логика лазерной атаки
    }

    private void retreatFromPlayer() {
        // Логика отступления от игрока
    }

    private void placeMines() {
        // Логика размещения мин
    }

    private void useGuidedLaser() {
        // Логика направляемого лазера
    }

    private void useFastGuidedLaser() {
        // Логика для использования быстрого направляемого лазера
        if (isPlayerInRange()) {
            // Получаем ближайшего игрока
            Player nearestPlayer = this.level().getNearestPlayer(this, 20.0); // Радиус 20 блоков

            if (nearestPlayer != null) {
                LaserProjectile laser = new LaserProjectile(this.level(), this, nearestPlayer, 5.0f);
                this.level().addFreshEntity(laser); // Добавляем снаряд в мир
            }
        }
    }

    private void dropLoot() {
        // Логика дропа предметов при смерти босса
    }

    private void createFakeExplosions() {
        // Создание фальшивых взрывов, которые не наносят урон
        for (int i = 0; i < 5; i++) { // Например, 5 фальшивых взрывов
            this.level().explode(
                    null,                   // источник взрыва
                    this.getX(),            // X координата
                    this.getY(),            // Y координата
                    this.getZ(),            // Z координата
                    0.0F,                   // сила взрыва
                    Level.ExplosionInteraction.NONE  // тип взаимодействия взрыва
            );
        }
    }

    private boolean isPlayerInRange() {
        // Проверка, находится ли игрок в пределах досягаемости
        Player nearestPlayer = this.level().getNearestPlayer(this, 10.0); // Получаем ближайшего игрока в радиусе 10 блоков
        return nearestPlayer != null && this.distanceTo(nearestPlayer) < 10; // Проверяем расстояние
    }

    private boolean shouldUseGasInjection() {
        // Логика для определения, когда использовать газовый впрыск
        return random.nextInt(10) < 3; // 30% шанс
    }

    private boolean shouldPlaceMines() {
        // Логика для определения, когда размещать мины
        return random.nextInt(5) < 2; // 40% шанс
    }

    private void increaseSpeed(double multiplier) {
        // Получаем текущий атрибут скорости
        AttributeInstance speedAttribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null) {
            // Текущее значение
            double currentSpeed = speedAttribute.getValue();
            // Максимальное ограничение, например, 2.0
            double maxSpeed = 2.0;

            // Увеличиваем скорость с ограничением
            double newSpeed = Math.min(currentSpeed * multiplier, maxSpeed);
            speedAttribute.setBaseValue(newSpeed);
        }
    }

    private void retreatAfterAttack() {
        // Логика для отдаления от игрока после атаки
        moveAwayFromPlayer(10); // Скачок на 10 блоков
    }

    private void moveAwayFromPlayer(int distance) {
        // Найти ближайшего игрока
        Player nearestPlayer = this.level().getNearestPlayer(this, 10.0);

        if (nearestPlayer != null) {
            // Получить текущую позицию босса
            Vec3 currentPos = this.position();

            // Получить позицию игрока
            Vec3 playerPos = nearestPlayer.position();

            // Вычислить направление движения от игрока
            Vec3 direction = currentPos.subtract(playerPos).normalize();

            // Переместить босса
            Vec3 newPos = currentPos.add(direction.scale(distance));

            // Установить новую позицию
            this.setDeltaMovement(direction.scale(distance));
            this.teleportTo(newPos.x, newPos.y, newPos.z);
        }
    }

    private Vec3 getPlayerPosition() {
        // Найти ближайшего игрока
        Player nearestPlayer = this.level().getNearestPlayer(this, 10.0);

        if (nearestPlayer != null) {
            // Возвращаем позицию ближайшего игрока
            return nearestPlayer.position();
        }

        // Возвращаем текущую позицию босса, если игрок не найден
        return this.position();
    }

    // Другие методы, такие как обработка урона, взаимодействие с игроком и т.д.


    // Методы взаимодействия с игроком
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Специальная логика получения урона
        if (source.getEntity() instanceof Player) {
            Player attacker = (Player) source.getEntity();

            // Динамическое изменение получаемого урона в зависимости от фазы
            switch (phase) {
                case 1:
                    amount *= 0.7f; // Уменьшаем урон в первой фазе
                    break;
                case 2:
                    amount *= 0.5f; // Еще больше уменьшаем во второй фазе
                    break;
                case 5, 6:
                    amount *= 1.2f; // Увеличиваем урон в последних фазах
                    break;
            }

            // Логирование урона
            logDamageReceived(attacker, amount);
        }

        return super.hurt(source, amount);
    }

    // Расширенный метод взаимодействия с игроком
    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);

        // Специальные эффекты при касании игрока в поздних фазах
        if (phase >= 4) {
            // Наносим урон при касании
            player.hurt(damageSources().mobAttack(this), 3.0f);

            // Добавляем негативный эффект
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, // Замедление
                    100,   // Длительность эффекта (в тиках)
                    1      // Уровень эффекта
            ));
        }
    }

    // Метод логирования получаемого урона
    private void logDamageReceived(Player attacker, float amount) {
        // Опциональный метод логирования
        HDU.LOGGER.info("Boss took damage: {} from player {} in phase {}",
                amount,
                attacker.getName().getString(),
                phase
        );
    }

    // Метод для создания особых эффектов в определенных фазах
    private void createPhaseTransitionEffects() {
        switch (phase) {
            case 3:
                // Создаем визуальный эффект при переходе в среднюю фазу
                createAreaEffect();
                break;
            case 6:
                // Финальная фаза - особый эффект
                createFinalPhaseEffect();
                break;
        }
    }

    private void createAreaEffect() {
        // Создаем область с особым эффектом
        this.level().explode(
                this,                      // Источник
                this.getX(),
                this.getY(),
                this.getZ(),
                2.0f,                      // Сила взрыва
                Level.ExplosionInteraction.NONE  // Тип взаимодействия
        );
    }

    private void createFinalPhaseEffect() {
        // Финальный эффект - серия небольших взрывов
        for (int i = 0; i < 3; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 4;
            double offsetZ = (random.nextDouble() - 0.5) * 4;

            this.level().explode(
                    this,
                    this.getX() + offsetX,
                    this.getY(),
                    this.getZ() + offsetZ,
                    1.0f,
                    Level.ExplosionInteraction.NONE
            );
        }
    }

    // Метод для broadcasts важных событий
    private void broadcastBossEvent(String message) {
        this.level().getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(message).withStyle(ChatFormatting.RED),
                false
        );
    }
}