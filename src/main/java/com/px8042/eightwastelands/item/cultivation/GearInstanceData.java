package com.px8042.eightwastelands.item.cultivation;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Stores generated gear values directly on the ItemStack so they never reroll.
 */
public final class GearInstanceData {

    private static final String ROOT_KEY = "eightwastelands_gear";
    private static final String VERSION_KEY = "version";
    private static final String GRADE_KEY = "grade";
    private static final String FIXED_AFFIXES_KEY = "fixed_affixes";
    private static final String RANDOM_AFFIXES_KEY = "random_affixes";
    private static final String AFFIX_ID_KEY = "id";
    private static final String AFFIX_AMOUNT_KEY = "amount";
    private static final int DATA_VERSION = 1;

    private GearInstanceData() {
    }

    public static boolean isGenerated(ItemStack stack) {
        return getGearTag(stack).getIntOr(VERSION_KEY, 0) == DATA_VERSION;
    }

    public static CultivationGearGrade getGrade(ItemStack stack, CultivationGearGrade fallback) {
        return CultivationGearGrade.bySerializedName(getGearTag(stack).getStringOr(GRADE_KEY, ""))
                .orElse(fallback);
    }

    public static void setGrade(ItemStack stack, CultivationGearGrade grade) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, data -> {
            CompoundTag gearTag = data.getCompoundOrEmpty(ROOT_KEY).copy();
            gearTag.putString(GRADE_KEY, grade.getSerializedName());
            data.put(ROOT_KEY, gearTag);
        });
    }

    public static void ensureGenerated(ItemStack stack, ICultivationGear gear, RandomSource random) {
        if (stack.isEmpty() || isGenerated(stack)) {
            return;
        }

        CultivationGearGrade grade = getGrade(stack, gear.getGearGrade());
        List<GearModifierEntry> fixedAffixes = scaleAffixes(
                GearDefinitions.getFixedAffixes(gear.getGearSlot()),
                grade
        );
        List<GearModifierEntry> randomAffixes = rollRandomAffixes(
                GearDefinitions.getRandomAffixes(gear.getGearSlot()),
                grade,
                random
        );

        CustomData.update(DataComponents.CUSTOM_DATA, stack, data -> {
            CompoundTag gearTag = data.getCompoundOrEmpty(ROOT_KEY).copy();
            gearTag.putInt(VERSION_KEY, DATA_VERSION);
            gearTag.putString(GRADE_KEY, grade.getSerializedName());
            gearTag.put(FIXED_AFFIXES_KEY, writeAffixes(fixedAffixes));
            gearTag.put(RANDOM_AFFIXES_KEY, writeAffixes(randomAffixes));
            data.put(ROOT_KEY, gearTag);
        });
    }

    public static List<GearModifierEntry> getFixedAffixes(ItemStack stack) {
        return readAffixes(getGearTag(stack).getListOrEmpty(FIXED_AFFIXES_KEY));
    }

    public static List<GearModifierEntry> getRandomAffixes(ItemStack stack) {
        return readAffixes(getGearTag(stack).getListOrEmpty(RANDOM_AFFIXES_KEY));
    }

    public static void appendTooltip(ItemStack stack, Consumer<Component> tooltipComponents) {
        if (!isGenerated(stack)) {
            tooltipComponents.accept(Component.translatable(
                    "tooltip.eightwastelands.cultivation_gear.pending_roll"
            ).withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        appendAffixes(
                tooltipComponents,
                "tooltip.eightwastelands.cultivation_gear.fixed_affix",
                getFixedAffixes(stack),
                ChatFormatting.GRAY
        );
        appendAffixes(
                tooltipComponents,
                "tooltip.eightwastelands.cultivation_gear.random_affix",
                getRandomAffixes(stack),
                ChatFormatting.AQUA
        );
    }

    private static void appendAffixes(
            Consumer<Component> tooltipComponents,
            String translationKey,
            List<GearModifierEntry> affixes,
            ChatFormatting color
    ) {
        for (GearModifierEntry entry : affixes) {
            tooltipComponents.accept(Component.translatable(
                    translationKey,
                    entry.affix().getDisplayComponent(entry.amount())
            ).withStyle(color));
        }
    }

    private static CompoundTag getGearTag(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.copyTag().getCompoundOrEmpty(ROOT_KEY);
    }

    private static List<GearModifierEntry> scaleAffixes(
            List<GearAffix> affixes,
            CultivationGearGrade grade
    ) {
        List<GearModifierEntry> entries = new ArrayList<>(affixes.size());
        for (GearAffix affix : affixes) {
            entries.add(new GearModifierEntry(
                    affix,
                    affix.getBaseAmount() * grade.getAttributeMultiplier()
            ));
        }
        return entries;
    }

    private static List<GearModifierEntry> rollRandomAffixes(
            List<GearAffix> candidates,
            CultivationGearGrade grade,
            RandomSource random
    ) {
        List<GearAffix> shuffled = new ArrayList<>(candidates);
        for (int index = shuffled.size() - 1; index > 0; index--) {
            int otherIndex = random.nextInt(index + 1);
            GearAffix current = shuffled.get(index);
            shuffled.set(index, shuffled.get(otherIndex));
            shuffled.set(otherIndex, current);
        }

        int count = Math.min(grade.getMaxRandomAffixes(), shuffled.size());
        int rolledCount = count <= 1 ? count : 1 + random.nextInt(count);
        List<GearModifierEntry> entries = new ArrayList<>(rolledCount);
        for (int index = 0; index < rolledCount; index++) {
            GearAffix affix = shuffled.get(index);
            entries.add(new GearModifierEntry(
                    affix,
                    affix.getBaseAmount() * grade.getAttributeMultiplier()
            ));
        }
        return entries;
    }

    private static ListTag writeAffixes(List<GearModifierEntry> affixes) {
        ListTag list = new ListTag();
        for (GearModifierEntry entry : affixes) {
            CompoundTag tag = new CompoundTag();
            tag.putString(AFFIX_ID_KEY, entry.affix().getSerializedName());
            tag.putDouble(AFFIX_AMOUNT_KEY, entry.amount());
            list.add(tag);
        }
        return list;
    }

    private static List<GearModifierEntry> readAffixes(ListTag list) {
        List<GearModifierEntry> entries = new ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            CompoundTag tag = list.getCompoundOrEmpty(index);
            GearAffix affix = GearAffix.bySerializedName(tag.getStringOr(AFFIX_ID_KEY, ""));
            if (affix != null) {
                entries.add(new GearModifierEntry(
                        affix,
                        tag.getDoubleOr(AFFIX_AMOUNT_KEY, 0.0D)
                ));
            }
        }
        return entries;
    }
}
