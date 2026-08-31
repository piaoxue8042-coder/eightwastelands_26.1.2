package com.px8042.eightwastelands.xinfa;

import com.px8042.eightwastelands.cultivation.CultivationAttribute;
import com.px8042.eightwastelands.cultivation.CultivationRealm;

import java.util.List;
import java.util.Optional;

public final class XinfaDefinitions {

    public static final String QI_REFINING_MANUAL_ID = "qi_refining_manual";
    public static final String TEST_XINFA_ID = QI_REFINING_MANUAL_ID;

    public static final XinfaDefinition QI_REFINING_MANUAL = new XinfaDefinition(
            QI_REFINING_MANUAL_ID,
            CultivationRealm.QI_REFINING,
            1.10D,
            List.of(
                    new XinfaPage("breathing_start"),
                    new XinfaPage("body_foundation"),
                    new XinfaPage("daily_breathing"),
                    new XinfaPage("body_and_spirit"),
                    new XinfaPage("inner_cycle"),
                    new XinfaPage("completion")
            ),
            List.of(
                    node("settle_breath", 0, 28, 78, false, List.of(),
                            XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 50.0D)),
                    node("warm_meridians", 0, 106, 78, false, List.of("settle_breath"),
                            XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 8.0D)),
                    node("open_lower_dantian", 0, 184, 78, true, List.of("warm_meridians"),
                            XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 0.5D)),

                    node("steady_bones", 1, 28, 78, false, List.of("open_lower_dantian"),
                            XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 10.0D)),
                    node("gather_inner_qi", 1, 106, 78, false, List.of("steady_bones"),
                            XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 75.0D)),
                    node("clear_senses", 1, 184, 78, true, List.of("gather_inner_qi"),
                            XinfaBonus.attribute(CultivationAttribute.MOVEMENT_SPEED, 0.005D)),

                    node("long_breath", 2, 28, 78, false, List.of("clear_senses"),
                            XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 12.0D)),
                    node("thin_qi_flow", 2, 106, 78, false, List.of("long_breath"),
                            XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 0.5D)),
                    node("first_daily_cycle", 2, 184, 78, true, List.of("thin_qi_flow"),
                            XinfaBonus.special(XinfaSpecialAttribute.TUNAI_DAILY_USES, 1.0D),
                            XinfaBonus.special(XinfaSpecialAttribute.TUNAI_QI_GAIN, 5.0D)),

                    node("tempered_body", 3, 28, 78, false, List.of("first_daily_cycle"),
                            XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 15.0D)),
                    node("sharp_spirit", 3, 106, 78, false, List.of("tempered_body"),
                            XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D)),
                    node("flowing_steps", 3, 184, 78, true, List.of("sharp_spirit"),
                            XinfaBonus.attribute(CultivationAttribute.MOVEMENT_SPEED, 0.005D)),

                    node("stored_breath", 4, 28, 78, false, List.of("flowing_steps"),
                            XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 100.0D)),
                    node("steady_heart", 4, 106, 78, false, List.of("stored_breath"),
                            XinfaBonus.attribute(CultivationAttribute.MAX_HEALTH, 15.0D)),
                    node("inner_cycle", 4, 184, 78, true, List.of("steady_heart"),
                            XinfaBonus.special(XinfaSpecialAttribute.ZHENYUAN_REGEN, 1.0D)),

                    node("clear_meridians", 5, 28, 78, false, List.of("inner_cycle"),
                            XinfaBonus.special(XinfaSpecialAttribute.MAX_ZHENYUAN, 125.0D)),
                    node("condensed_breath", 5, 106, 78, false, List.of("clear_meridians"),
                            XinfaBonus.attribute(CultivationAttribute.ATTACK_DAMAGE, 1.0D)),
                    node("qi_refining_completion", 5, 184, 78, true, List.of("condensed_breath"),
                            XinfaBonus.special(XinfaSpecialAttribute.TUNAI_DAILY_USES, 1.0D),
                            XinfaBonus.special(XinfaSpecialAttribute.TUNAI_QI_GAIN, 10.0D))
            )
    );
    public static final XinfaDefinition TEST_XINFA = QI_REFINING_MANUAL;

    private static final List<XinfaDefinition> ALL = List.of(QI_REFINING_MANUAL);

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

    private static XinfaNode node(
            String id,
            int pageIndex,
            int x,
            int y,
            boolean major,
            List<String> prerequisites,
            XinfaBonus... bonuses
    ) {
        return new XinfaNode(
                id,
                pageIndex,
                x,
                y,
                major,
                XinfaCostTable.cost(CultivationRealm.QI_REFINING, pageIndex, major),
                prerequisites,
                List.of(bonuses)
        );
    }
}
