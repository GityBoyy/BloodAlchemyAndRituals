package org.chubby.github.bloodalchemyandrituals.client.renderer.blockEntities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.BloodRitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.init.BARRecipes;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;
import org.chubby.github.bloodalchemyandrituals.common.recipe.input.BloodRitualAltarInput;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;

public class RitualAltarRenderer implements BlockEntityRenderer<BloodRitualAltarBlockEntity> {

    public RitualAltarRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(BloodRitualAltarBlockEntity pBlockEntity, float v, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int i, int i1)
    {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getItem(0);
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f,1.15f,0.5f);
        pPoseStack.scale(0.5f,0.5f,0.5f);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pBlockEntity.getRenderingRotation()));

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880,
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();
        spawnParticles(pBlockEntity.getLevel(),pBlockEntity.getBlockPos(), Objects.requireNonNull(pBlockEntity.getLevel()).getRandom(),pBlockEntity);

    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private void spawnParticles(Level level, BlockPos pos, RandomSource random, BloodRitualAltarBlockEntity be) {
        if (level == null || !level.isClientSide || be.isEmpty()) {
            return;
        }
        BloodRitualAltarInput input = new BloodRitualAltarInput(be.getItem(0));
        Optional<RecipeHolder<BloodRitualAltarRecipe>> recipe = level.getRecipeManager().getRecipeFor(BARRecipes.BLOOD_RITUAL_ALTAR_RECIPE.get(),input,level);
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.1;
        double centerZ = pos.getZ() + 0.5;

        DustParticleOptions redParticle = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);

        if(recipe.isPresent() && recipe.get().value().result() != null)
        {
            for (int i = 0; i < 5; i++) {
                double angle = random.nextDouble() * 2 * Math.PI;
                double radius = 0.3 + random.nextDouble() * 0.2;

                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                double offsetY = random.nextDouble() * 0.1;

                level.addParticle(redParticle, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 0, 0, 0);
            }
        }

    }
}
