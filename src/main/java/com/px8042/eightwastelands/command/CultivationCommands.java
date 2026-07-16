package com.px8042.eightwastelands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.px8042.eightwastelands.EightWastelands;
import com.px8042.eightwastelands.alchemy.AlchemyManager;
import com.px8042.eightwastelands.alchemy.AlchemyMastery;
import com.px8042.eightwastelands.cultivation.CultivationCombatManager;
import com.px8042.eightwastelands.cultivation.CultivationManager;
import com.px8042.eightwastelands.cultivation.CultivationRealm;
import com.px8042.eightwastelands.cultivation.CultivationSnapshot;
import com.px8042.eightwastelands.cultivation.CultivationStage;
import com.px8042.eightwastelands.item.pill.PillGrade;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Arrays;

public class CultivationCommands {

    private static final SimpleCommandExceptionType INVALID_STAGE =
            new SimpleCommandExceptionType(Component.literal("Unknown cultivation stage."));
    private static final SimpleCommandExceptionType INVALID_REALM =
            new SimpleCommandExceptionType(Component.literal("Unknown cultivation realm."));
    private static final SimpleCommandExceptionType INVALID_MASTERY =
            new SimpleCommandExceptionType(Component.literal("Unknown alchemy mastery."));
    private static final SimpleCommandExceptionType INVALID_PILL_GRADE =
            new SimpleCommandExceptionType(Component.literal("Unknown pill grade."));
    private static final SimpleCommandExceptionType CANNOT_REFINE =
            new SimpleCommandExceptionType(Component.literal("Alchemy realm is too low for this pill realm."));
    private static final SimpleCommandExceptionType TARGET_NOT_LIVING =
            new SimpleCommandExceptionType(Component.literal("Target must be a living entity."));

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(EightWastelands.MODID)
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(Commands.literal("cultivation")
                        .then(Commands.literal("get")
                                .executes(CultivationCommands::get))
                        .then(Commands.literal("add_qi")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(CultivationCommands::addQi)))
                        .then(Commands.literal("set_stage")
                                .then(Commands.argument("stage", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                Arrays.stream(CultivationStage.values())
                                                        .map(CultivationStage::getSerializedName),
                                                builder
                                        ))
                                        .executes(CultivationCommands::setStage)))
                        .then(Commands.literal("reset_tunai")
                                .executes(CultivationCommands::resetTunai))
                        .then(Commands.literal("clear")
                                .executes(CultivationCommands::clear)))
                .then(Commands.literal("alchemy")
                        .then(Commands.literal("get")
                                .executes(CultivationCommands::getAlchemy))
                        .then(Commands.literal("set_realm")
                                .then(Commands.argument("realm", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                Arrays.stream(CultivationRealm.values())
                                                        .map(CultivationRealm::getSerializedName),
                                                builder
                                        ))
                                        .executes(CultivationCommands::setAlchemyRealm)))
                        .then(Commands.literal("set_mastery")
                                .then(Commands.argument("mastery", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                Arrays.stream(AlchemyMastery.values())
                                                        .map(AlchemyMastery::getSerializedName),
                                                builder
                                        ))
                                        .executes(CultivationCommands::setAlchemyMastery)))
                        .then(Commands.literal("roll_pill_grade")
                                .then(Commands.argument("pill_realm", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                Arrays.stream(CultivationRealm.values())
                                                        .map(CultivationRealm::getSerializedName),
                                                builder
                                        ))
                                        .executes(CultivationCommands::rollPillGrade)))
                        .then(Commands.literal("record_refine")
                                .then(Commands.argument("pill_realm", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                Arrays.stream(CultivationRealm.values())
                                                        .map(CultivationRealm::getSerializedName),
                                                builder
                                        ))
                                        .then(Commands.argument("grade", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                        Arrays.stream(PillGrade.gradedValues())
                                                                .map(PillGrade::getSerializedName),
                                                        builder
                                                ))
                                                .executes(CultivationCommands::recordRefine))))
                        .then(Commands.literal("clear")
                                .executes(CultivationCommands::clearAlchemy)))
                .then(Commands.literal("entity_cultivation")
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(CultivationCommands::getEntityCultivation)))
                        .then(Commands.literal("set_stage")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .then(Commands.argument("stage", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                        Arrays.stream(CultivationStage.values())
                                                                .map(CultivationStage::getSerializedName),
                                                        builder
                                                ))
                                                .executes(CultivationCommands::setEntityStage))))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(CultivationCommands::clearEntityStage)))));
    }

    private static int get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        CultivationSnapshot snapshot = CultivationManager.getSnapshot(player);
        sendStatus(context.getSource(), snapshot);
        return snapshot.qi();
    }

    private static int addQi(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        int amount = IntegerArgumentType.getInteger(context, "amount");

        CultivationManager.addQi(player, amount);
        CultivationSnapshot snapshot = CultivationManager.getSnapshot(player);
        context.getSource().sendSuccess(() -> Component.literal(
                "Added qi: +" + amount + ". Current: " + snapshot.qi() + "."
        ), false);
        return snapshot.qi();
    }

    private static int setStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String stageName = StringArgumentType.getString(context, "stage");
        CultivationStage stage = CultivationStage.bySerializedName(stageName)
                .orElseThrow(INVALID_STAGE::create);

        CultivationManager.setStageForCommand(player, stage);
        context.getSource().sendSuccess(() -> Component.literal("Set cultivation stage to ")
                .append(stage.getDisplayName())
                .append(Component.literal(".")), false);
        return stage.getIndex();
    }

    private static int resetTunai(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        CultivationManager.resetTunaiForCommand(player);
        CultivationSnapshot snapshot = CultivationManager.getSnapshot(player);
        context.getSource().sendSuccess(() -> Component.literal(
                "Reset daily breathing uses. Current: "
                        + snapshot.tunaiUsedToday()
                        + "/"
                        + snapshot.tunaiDailyUses()
                        + "."
        ), false);
        return snapshot.remainingTunaiUses();
    }

    private static int clear(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        CultivationManager.clearForCommand(player);
        CultivationSnapshot snapshot = CultivationManager.getSnapshot(player);
        context.getSource().sendSuccess(() -> Component.literal("Cleared cultivation data."), false);
        return snapshot.stageIndex();
    }

    private static int getAlchemy(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        sendAlchemyStatus(context.getSource(), player);
        return AlchemyManager.getMastery(player).getIndex();
    }

    private static int setAlchemyRealm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String realmName = StringArgumentType.getString(context, "realm");
        CultivationRealm realm = CultivationRealm.bySerializedName(realmName)
                .orElseThrow(INVALID_REALM::create);

        AlchemyManager.setRealmForCommand(player, realm);
        context.getSource().sendSuccess(() -> Component.literal("Set alchemy realm to ")
                .append(realm.getDisplayName())
                .append(Component.literal(".")), false);
        return realm.getIndex();
    }

    private static int setAlchemyMastery(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String masteryName = StringArgumentType.getString(context, "mastery");
        AlchemyMastery mastery = AlchemyMastery.bySerializedName(masteryName)
                .orElseThrow(INVALID_MASTERY::create);

        AlchemyManager.setMasteryForCommand(player, mastery);
        context.getSource().sendSuccess(() -> Component.literal("Set alchemy mastery to ")
                .append(mastery.getDisplayName())
                .append(Component.literal(".")), false);
        return mastery.getIndex();
    }

    private static int rollPillGrade(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String realmName = StringArgumentType.getString(context, "pill_realm");
        CultivationRealm pillRealm = CultivationRealm.bySerializedName(realmName)
                .orElseThrow(INVALID_REALM::create);
        PillGrade grade = AlchemyManager.rollPillGrade(player, pillRealm)
                .orElseThrow(CANNOT_REFINE::create);
        AlchemyManager.AlchemyProgressResult progress =
                AlchemyManager.recordPillRefined(player, pillRealm, grade);

        context.getSource().sendSuccess(() -> Component.literal("Rolled pill grade: ")
                .append(grade.getDisplayName())
                .append(formatAlchemyProgress(progress)), false);
        return grade.ordinal();
    }

    private static int recordRefine(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String realmName = StringArgumentType.getString(context, "pill_realm");
        String gradeName = StringArgumentType.getString(context, "grade");
        CultivationRealm pillRealm = CultivationRealm.bySerializedName(realmName)
                .orElseThrow(INVALID_REALM::create);
        PillGrade grade = PillGrade.bySerializedName(gradeName)
                .orElseThrow(INVALID_PILL_GRADE::create);
        if (!grade.isGraded()) {
            throw INVALID_PILL_GRADE.create();
        }

        if (!AlchemyManager.canRefine(player, pillRealm)) {
            throw CANNOT_REFINE.create();
        }

        AlchemyManager.AlchemyProgressResult progress =
                AlchemyManager.recordPillRefined(player, pillRealm, grade);
        context.getSource().sendSuccess(() -> Component.literal("Recorded pill grade: ")
                .append(grade.getDisplayName())
                .append(formatAlchemyProgress(progress)), false);
        return progress.newExperience();
    }

    private static int clearAlchemy(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        AlchemyManager.clearForCommand(player);
        context.getSource().sendSuccess(() -> Component.literal("Cleared alchemy data."), false);
        return 0;
    }

    private static int getEntityCultivation(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LivingEntity target = getLivingTarget(context);
        CultivationStage stage = CultivationCombatManager.getCombatStage(target);
        context.getSource().sendSuccess(() -> Component.literal("Entity cultivation: ")
                .append(target.getDisplayName())
                .append(Component.literal(" = "))
                .append(stage.getDisplayName())
                .append(Component.literal(".")), false);
        return stage.getIndex();
    }

    private static int setEntityStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LivingEntity target = getLivingTarget(context);
        String stageName = StringArgumentType.getString(context, "stage");
        CultivationStage stage = CultivationStage.bySerializedName(stageName)
                .orElseThrow(INVALID_STAGE::create);

        CultivationCombatManager.setEntityStage(target, stage);
        context.getSource().sendSuccess(() -> Component.literal("Set entity cultivation: ")
                .append(target.getDisplayName())
                .append(Component.literal(" = "))
                .append(stage.getDisplayName())
                .append(Component.literal(".")), false);
        return stage.getIndex();
    }

    private static int clearEntityStage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LivingEntity target = getLivingTarget(context);
        CultivationCombatManager.clearEntityStage(target);
        context.getSource().sendSuccess(() -> Component.literal("Cleared entity cultivation: ")
                .append(target.getDisplayName())
                .append(Component.literal(".")), false);
        return 0;
    }

    private static LivingEntity getLivingTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        if (target instanceof LivingEntity livingEntity) {
            return livingEntity;
        }

        throw TARGET_NOT_LIVING.create();
    }

    private static void sendStatus(CommandSourceStack source, CultivationSnapshot snapshot) {
        source.sendSuccess(() -> Component.literal("Cultivation: stage=")
                .append(snapshot.stage().getDisplayName())
                .append(Component.literal(
                        ", qi="
                                + snapshot.qi()
                                + ", tunai="
                                + snapshot.tunaiUsedToday()
                                + "/"
                                + snapshot.tunaiDailyUses()
                                + "."
                )), false);
    }

    private static void sendAlchemyStatus(CommandSourceStack source, ServerPlayer player) {
        source.sendSuccess(() -> Component.literal("Alchemy: realm=")
                .append(AlchemyManager.getRealm(player).getDisplayName())
                .append(Component.literal(", mastery="))
                .append(AlchemyManager.getMastery(player).getDisplayName())
                .append(Component.literal(
                        ", experience="
                                + AlchemyManager.getExperience(player)
                                + "/"
                                + AlchemyManager.getRequiredExperience(player)
                ))
                .append(Component.literal(".")), false);
    }

    private static Component formatAlchemyProgress(AlchemyManager.AlchemyProgressResult progress) {
        Component message = Component.literal(
                ", alchemy experience +"
                        + progress.gainedExperience()
                        + ", current "
                        + progress.newExperience()
                        + "/"
                        + progress.requiredExperience()
                        + "."
        );

        if (!progress.advanced()) {
            return message;
        }

        return message.copy()
                .append(Component.literal(" Advanced to "))
                .append(progress.newRealm().getDisplayName())
                .append(Component.literal(" "))
                .append(progress.newMastery().getDisplayName())
                .append(Component.literal("."));
    }
}
