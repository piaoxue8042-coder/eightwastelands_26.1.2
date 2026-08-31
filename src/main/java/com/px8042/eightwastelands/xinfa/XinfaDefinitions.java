package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationAttribute;
import com.px8042.eightwastelands.cultivation.CultivationRealm;

import java.util.List;
import java.util.Optional;

public final class XinfaDefinitions {

    public static final String QI_REFINING_MANUAL_ID = "qi_refining_manual";
    public static final String BODY_TEMPERING_MANUAL_ID = "body_tempering_manual";

    public static final XinfaDefinition QI_REFINING_MANUAL = XinfaBuilder.create(QI_REFINING_MANUAL_ID)
            .requiredRealm(CultivationRealm.QI_REFINING)
            .completionMultiplier(1.10D)
            .linearPage("breathing_start")
            .node("settle_breath", XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 50.0D))
            .node("warm_meridians", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 8.0D))
            .majorNode("open_lower_dantian", XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 0.5D))
            .linearPage("body_foundation")
            .node("steady_bones", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 10.0D))
            .node("gather_inner_qi", XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 75.0D))
            .majorNode("clear_senses", XinfaBonus.attribute(CultivationAttribute.MOVEMENT_SPEED, 0.005D))
            .linearPage("daily_breathing")
            .node("long_breath", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 12.0D))
            .node("thin_qi_flow", XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 0.5D))
            .majorNode(
                    "first_daily_cycle",
                    XinfaBonus.special(XinfaSpecialAttribute.TUNAI_DAILY_USES, 1.0D),
                    XinfaBonus.special(XinfaSpecialAttribute.TUNAI_QI_GAIN, 5.0D)
            )
            .linearPage("body_and_spirit")
            .node("tempered_body", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 15.0D))
            .node("sharp_spirit", XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D))
            .majorNode("flowing_steps", XinfaBonus.attribute(CultivationAttribute.MOVEMENT_SPEED, 0.005D))
            .linearPage("inner_cycle")
            .node("stored_breath", XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 100.0D))
            .node("steady_heart", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 15.0D))
            .majorNode("inner_cycle", XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 1.0D))
            .linearPage("completion")
            .node("clear_meridians", XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 125.0D))
            .node("condensed_breath", XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D))
            .majorNode(
                    "qi_refining_completion",
                    XinfaBonus.special(XinfaSpecialAttribute.TUNAI_DAILY_USES, 1.0D),
                    XinfaBonus.special(XinfaSpecialAttribute.TUNAI_QI_GAIN, 10.0D)
            )
            .build();

    public static final XinfaDefinition BODY_TEMPERING_MANUAL = XinfaBuilder.create(BODY_TEMPERING_MANUAL_ID)
            .requiredRealm(CultivationRealm.QI_REFINING)
            .completionMultiplier(1.10D)
            .linearPage("body_root")
            .node("stable_root", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 10.0D))
            .node("gather_strength", XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D))
            .majorNode("train_tendons", XinfaBonus.attribute(CultivationAttribute.ATTACK_SPEED, 0.1D))
            .linearPage("sinew_force")
            .node("thick_blood", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 15.0D))
            .node("store_vital_qi", XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 50.0D))
            .majorNode("breaking_force", XinfaBonus.attribute(CultivationAttribute.MELEE_DAMAGE_ADDITIVE, 0.05D))
            .linearPage("body_completion")
            .node("strong_bones", XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 20.0D))
            .node("sharp_intent", XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D))
            .majorNode(
                    "body_tempering_completion",
                    XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 25.0D),
                    XinfaBonus.attribute(CultivationAttribute.ATTACK_SPEED, 0.1D)
            )
            .build();

    private static final List<XinfaDefinition> ALL = List.of(QI_REFINING_MANUAL, BODY_TEMPERING_MANUAL);

    private XinfaDefinitions() {
    }

    public static List<XinfaDefinition> all() {
        return ALL;
    }

    public static Optional<XinfaDefinition> byId(String id) {
        for (XinfaDefinition definition : ALL) {
            if (definition.id().equals(id)) {
                return Optional.of(definition);
            }
        }

        return Optional.empty();
    }

}
