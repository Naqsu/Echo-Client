package echo.util.player;

import echo.Wrapper;
import net.minecraft.client.network.ClientPlayerEntity;

public class MovementUtil {

    public static boolean isMoving() {
        ClientPlayerEntity player = Wrapper.getPlayer();
        // w Yarn input to 'input', a movementForward/Sideways
        return player != null && (player.input.movementForward != 0 || player.input.movementSideways != 0);
    }

    public static void setSpeed(double speed) {
        ClientPlayerEntity player = Wrapper.getPlayer();
        if (player == null) return;

        if (isMoving()) {
            float yaw = player.getYaw();
            float forward = player.input.movementForward;
            float strafe = player.input.movementSideways;

            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }

            double rad = Math.toRadians(yaw + 90);
            double x = forward * speed * Math.cos(rad) + strafe * speed * Math.sin(rad);
            double z = forward * speed * Math.sin(rad) - strafe * speed * Math.cos(rad);

            player.setVelocity(x, player.getVelocity().y, z);
        } else {
            player.setVelocity(0, player.getVelocity().y, 0);
        }
    }

    public static double getSpeed() {
        ClientPlayerEntity player = Wrapper.getPlayer();
        if (player == null) return 0;
        return Math.hypot(player.getVelocity().x, player.getVelocity().z);
    }
}