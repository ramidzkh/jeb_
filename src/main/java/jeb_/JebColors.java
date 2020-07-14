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

package jeb_;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;

import java.util.Map;
import java.util.WeakHashMap;

public class JebColors {

    public static final Map<BeaconBlockEntity, Boolean> ACTIVE_BEACONS = new WeakHashMap<>();

    private static final int DYES = DyeColor.values().length;

    public static float[] getColorComponents(DyeColor dyeColor, Entity entity, float tickDelta) {
        if (entity.hasCustomName() && "jeb_".equals(entity.getName().asString())) {
            return getColorComponents(entity.getEntityId(), entity.age, tickDelta);
        } else {
            return dyeColor.getColorComponents();
        }
    }

    public static float[] getColorComponents(int seed, int age, float tickDelta) {
        int n = age / 25 + seed;
        float r = ((float) (age % 25) + tickDelta) / 25.0F;
        float[] color = SheepEntity.getRgbColor(DyeColor.byId(n % DYES));
        float[] nextColor = SheepEntity.getRgbColor(DyeColor.byId((n + 1) % DYES));
        float v = color[0] * (1.0F - r) + nextColor[0] * r;
        float w = color[1] * (1.0F - r) + nextColor[1] * r;
        float x = color[2] * (1.0F - r) + nextColor[2] * r;

        return new float[]{v, w, x};
    }
}
