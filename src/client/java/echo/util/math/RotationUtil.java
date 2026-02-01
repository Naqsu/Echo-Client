package echo.util.math;

import echo.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {

    public static float[] getRotations(Entity target) {
        Vec3d eyesPos = Wrapper.getPlayer().getEyePos();
        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2.0, 0);

        double diffX = targetPos.x - eyesPos.x;
        double diffY = targetPos.y - eyesPos.y;
        double diffZ = targetPos.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        yaw = Wrapper.getPlayer().getYaw() + MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().getYaw());
        pitch = Wrapper.getPlayer().getPitch() + MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().getPitch());

        pitch = MathHelper.clamp(pitch, -90F, 90F);

        return new float[]{yaw, pitch};
    }
}