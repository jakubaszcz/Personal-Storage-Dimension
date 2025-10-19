package net.chrupki.persage.world;

import net.chrupki.persage.Persage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static net.minecraft.core.BlockPos.getX;

public class ModDimension extends Item {

        public static final ResourceKey<Level> PERSONAL_DIMENSION = ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath("persage", "personal_dimension")
        );

        public ModDimension(Properties pProperties) {
                super(pProperties);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                if (level.isClientSide()) {
                        return InteractionResultHolder.pass(player.getItemInHand(hand));
                }


                ServerPlayer serverPlayer = (ServerPlayer) player;
                MinecraftServer server = serverPlayer.server;
                ItemStack stack = player.getItemInHand(hand);

                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                CompoundTag tag = customData != null ? customData.copyTag() : new CompoundTag();

                if (serverPlayer.level().dimension().equals(ModDimension.PERSONAL_DIMENSION)) {

                        if (tag.contains("return_dimension")) {
                                String returnDim = tag.getString("return_dimension");
                                long returnPosLong = tag.getLong("return_position");

                                ResourceKey<Level> dim = ResourceKey.create(
                                        Registries.DIMENSION,
                                        ResourceLocation.parse(returnDim)
                                );
                                BlockPos pos = BlockPos.of(returnPosLong);
                                ServerLevel returnLevel = server.getLevel(dim);

                                if (returnLevel != null) {
                                        serverPlayer.teleportTo(
                                                returnLevel,
                                                pos.getX() + 0.5,
                                                pos.getY(),
                                                pos.getZ() + 0.5,
                                                serverPlayer.getYRot(),
                                                serverPlayer.getXRot()
                                        );
                                }
                                player.getCooldowns().addCooldown(this, 400);
                        }
                        return InteractionResultHolder.success(stack);
                }

                ServerLevel personalWorld = server.getLevel(PERSONAL_DIMENSION);

                if (personalWorld == null) {
                        return InteractionResultHolder.fail(stack);
                }

                tag.putString("return_dimension", serverPlayer.level().dimension().location().toString());
                tag.putLong("return_position", serverPlayer.blockPosition().asLong());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));


                int offset = Math.abs(player.getUUID().hashCode() % 10000);
                BlockPos origin = new BlockPos(offset * 32, 57, 0);

                boolean needsGeneration = true;
                for (int x = 0; x < 16 && needsGeneration; x++) {
                        for (int z = 0; z < 16 && needsGeneration; z++) {
                                BlockPos checkPos = origin.offset(x - 8, -1, z - 8);
                                if (!personalWorld.isEmptyBlock(checkPos)) {
                                        needsGeneration = false;
                                }
                        }
                }

                if (needsGeneration) {
                        int size = 16;
                        int half = size / 2;

                        for (int x = 0; x < size; x++) {
                                for (int y = 0; y < size; y++) {
                                        for (int z = 0; z < size; z++) {
                                                boolean border = (x == 0 || x == size - 1 ||
                                                        y == 0 || y == size - 1 ||
                                                        z == 0 || z == size - 1);

                                                BlockPos pos = origin.offset(x - half, y - half, z - half);
                                                if (border && personalWorld.isEmptyBlock(pos)) {
                                                        personalWorld.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3);
                                                }
                                        }
                                }
                        }
                }

                serverPlayer.teleportTo(
                        personalWorld,
                        origin.getX() + 0.5,
                        origin.getY(),
                        origin.getZ() + 0.5,
                        serverPlayer.getYRot(),
                        serverPlayer.getXRot()
                );
                return InteractionResultHolder.success(stack);
        }

}
