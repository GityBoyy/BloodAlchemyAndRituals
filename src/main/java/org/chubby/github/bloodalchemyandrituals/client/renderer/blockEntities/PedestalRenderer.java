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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.PedestalBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PedestalRenderer  implements BlockEntityRenderer<PedestalBlockEntity> {
    public PedestalRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.getItem(0);

        if(!stack.isEmpty())
        {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5f, 1.15f, 0.5f);
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(pBlockEntity.getRenderingRotation()));

            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 15728880,
                    OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}