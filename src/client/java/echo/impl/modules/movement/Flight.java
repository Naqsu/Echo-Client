package echo.impl.modules.movement;

import echo.api.event.Listener;
import echo.api.event.impl.MotionEvent;
import echo.api.module.Category;
import echo.api.module.Module;
import echo.api.setting.impl.ModeSetting;
import echo.api.setting.impl.NumberSetting;
import echo.util.player.MovementUtil;

public class Flight extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Motion", "Motion", "Creative");
    private final NumberSetting speed = new NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1);

    public Flight() {
        super("Flight", "Allows you to fly.", Category.MOVEMENT);
        addSettings(mode, speed);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null) return;

        if (mode.is("Creative")) {
            mc.player.getAbilities().allowFlying = true; // <-- Zmiana nazwy
            mc.player.getAbilities().flying = true;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player == null) return;

        mc.player.getAbilities().flying = false;
        if (!mc.player.isCreative()) {
            mc.player.getAbilities().allowFlying = false; // <-- Zmiana nazwy
        }

        // setDeltaMovement -> setVelocity w Yarn
        mc.player.setVelocity(0, 0, 0);
    }

    @Listener
    public void onMotion(MotionEvent event) {
        if (!event.isPre()) return;

        if (mode.is("Motion")) {
            event.setOnGround(true);
            mc.player.getAbilities().flying = false;

            double ySpeed = 0;
            // opcje klawiszy w Yarn
            if (mc.options.jumpKey.isPressed()) { // <-- jumpKey
                ySpeed = speed.getValue();
            } else if (mc.options.sneakKey.isPressed()) { // <-- sneakKey
                ySpeed = -speed.getValue();
            }

            mc.player.setVelocity(mc.player.getVelocity().x, ySpeed, mc.player.getVelocity().z);
            MovementUtil.setSpeed(speed.getValue());
        }
    }
}