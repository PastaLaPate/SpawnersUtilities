package com.github.pastalapate.spawner_utilities.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RayTrace {
    public static BlockRayTraceResult rayTrace(World world, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        double range = 15;

        float f = player.xRot;
        float f1 = player.yRot;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vector3d vector3d1 = vector3d.add((double) f6 * range, (double) f5 * range, (double) f7 * range);
        return world.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }
}
