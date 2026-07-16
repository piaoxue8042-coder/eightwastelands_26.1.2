package com.px8042.eightwastelands.cultivation;

public class CultivationClientCache {

    private static CultivationSnapshot snapshot = new CultivationSnapshot(
            CultivationStage.MORTAL.getIndex(),
            0,
            0,
            CultivationManager.BASE_TUNAI_DAILY_USES,
            0L,
            0,
            0.0D,
            0.0D,
            0.0D,
            20.0D,
            20.0D,
            1.0D,
            4.0D,
            0.10D,
            CultivationRealm.QI_REFINING.getIndex(),
            0,
            0,
            100
    );

    private CultivationClientCache() {
    }

    public static CultivationSnapshot getSnapshot() {
        return snapshot;
    }

    public static void setSnapshot(CultivationSnapshot newSnapshot) {
        snapshot = newSnapshot;
    }

    public static void tickCooldown() {
        if (snapshot.breakthroughCooldownTicks() <= 0L) {
            return;
        }

        snapshot = new CultivationSnapshot(
                snapshot.stageIndex(),
                snapshot.qi(),
                snapshot.tunaiUsedToday(),
                snapshot.tunaiDailyUses(),
                snapshot.breakthroughCooldownTicks() - 1L,
                snapshot.breakthroughFailureCount(),
                snapshot.zhenyuan(),
                snapshot.maxZhenyuan(),
                snapshot.zhenyuanRegenPerSecond(),
                snapshot.health(),
                snapshot.maxHealth(),
                snapshot.attackDamage(),
                snapshot.attackSpeed(),
                snapshot.movementSpeed(),
                snapshot.alchemyRealmIndex(),
                snapshot.alchemyMasteryIndex(),
                snapshot.alchemyExperience(),
                snapshot.alchemyRequiredExperience()
        );
    }
}
