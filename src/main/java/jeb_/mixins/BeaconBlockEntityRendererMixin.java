/*
 * MIT License
 *
 * Copyright (c) 2020 Ramid Khan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jeb_.mixins;

import jeb_.JebColors;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(BeaconBlockEntityRenderer.class)
public abstract class BeaconBlockEntityRendererMixin {

    private static final Map<BeaconBlockEntity, Boolean> ACTIVE_BEACONS = JebColors.ACTIVE_BEACONS;

    @Redirect(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BeaconBlockEntity$BeamSegment;getColor()[F"))
    private float[] getColor(BeaconBlockEntity.BeamSegment beamSegment, BeaconBlockEntity beaconBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        World world = beaconBlockEntity.getWorld();

        if (world != null) {
            int time = (int) world.getTime();

            if (time % 100 == 0) {
                ACTIVE_BEACONS.remove(beaconBlockEntity);
            }

            check:
            if (!ACTIVE_BEACONS.containsKey(beaconBlockEntity)) {
                ACTIVE_BEACONS.put(beaconBlockEntity, false);

                for (Entity entity : world.getEntities(null, new Box(beaconBlockEntity.getPos()).expand(48))) {
                    if (entity.hasCustomName() && "jeb_".equals(entity.getName().asString())) {
                        ACTIVE_BEACONS.put(beaconBlockEntity, true);
                        break check;
                    }
                }

                BlockPos beaconPos = beaconBlockEntity.getPos();

                for (BlockPos blockPos : BlockPos.iterate(beaconPos.add(-5, -5, -5), beaconPos.add(5, 5, 5))) {
                    BlockEntity blockEntity = world.getBlockEntity(blockPos);

                    if (blockEntity instanceof SignBlockEntity) {
                        for (Text text : ((SignBlockEntityAccessor) blockEntity).getText()) {
                            if (text != null && text.getString().contains("jeb_")) {
                                ACTIVE_BEACONS.put(beaconBlockEntity, true);
                                break check;
                            }
                        }
                    }
                }
            }

            Boolean b = ACTIVE_BEACONS.get(beaconBlockEntity);

            if (b != null && b) {
                return JebColors.getColorComponents(beaconBlockEntity.hashCode(), time, tickDelta);
            }
        }

        return beamSegment.getColor();
    }
}
