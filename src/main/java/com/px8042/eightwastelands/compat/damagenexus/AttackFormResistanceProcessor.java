package com.px8042.eightwastelands.compat.damagenexus;

import io.github.naimjeg.damagenexus.api.DamagePhaseProcessor;
import io.github.naimjeg.damagenexus.api.context.DamageRuleContext;
import io.github.naimjeg.damagenexus.api.enums.DamagePhase;
import io.github.naimjeg.damagenexus.registry.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttackFormResistanceProcessor implements DamagePhaseProcessor {
    private static final float RESISTANCE_K_VALUE = 50.0F;
    private static final float MAX_MITIGATION = 0.95F;

    private static final TagKey<DamageType> IS_MELEE = damageTypeTag("is_melee");
    private static final TagKey<DamageType> IS_PROJECTILE = damageTypeTag("is_projectile");
    private static final TagKey<DamageType> IS_RANGED = damageTypeTag("is_ranged");

    @Override
    public void apply(DamageRuleContext context) {
        DamageSource source = context.source();

        if (source.is(IS_MELEE)) {
            applyMitigation(context, ModAttributes.RESISTANCE_MELEE, "eightwastelands:melee_resistance");
        }

        if (source.is(IS_PROJECTILE) || source.is(IS_RANGED)) {
            applyMitigation(context, ModAttributes.RESISTANCE_PROJECTILE, "eightwastelands:projectile_resistance");
        }
    }

    @Override
    public DamagePhase phase() {
        return DamagePhase.MITIGATION_SETUP;
    }

    @Override
    public int getPriority() {
        return PRIORITY_MOD_STANDARD;
    }

    @Override
    public boolean canHandle(DamageRuleContext context) {
        DamageSource source = context.source();
        return context.isManaged()
                && !source.is(DamageTypeTags.BYPASSES_EFFECTS)
                && !source.is(DamageTypeTags.BYPASSES_RESISTANCE);
    }

    private static void applyMitigation(
            DamageRuleContext context,
            Holder<Attribute> attribute,
            String traceId
    ) {
        float resistance = (float) getAttributeValue(context.victim(), attribute);
        if (resistance == 0.0F) {
            return;
        }

        context.tryAddGlobalMitigation(calculateMitigation(resistance), traceId);
    }

    private static float calculateMitigation(float resistance) {
        float mitigation = resistance >= 0.0F
                ? resistance / (resistance + RESISTANCE_K_VALUE)
                : resistance / RESISTANCE_K_VALUE;

        return Math.max(-1.0F, Math.min(MAX_MITIGATION, mitigation));
    }

    private static double getAttributeValue(LivingEntity entity, Holder<Attribute> attribute) {
        if (entity.getAttribute(attribute) == null) {
            return 0.0D;
        }

        return entity.getAttributeValue(attribute);
    }

    private static TagKey<DamageType> damageTypeTag(String path) {
        return TagKey.create(
                Registries.DAMAGE_TYPE,
                Identifier.fromNamespaceAndPath("damagenexus", path)
        );
    }
}
