package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;

public class Camera {
    private double x, y;
    private final double maxCameraGauche = 0.3 * WIDTH;
    private final double maxCameraDroite = 0.7 * WIDTH;
    private final double maxCameraBas = 0.6 * HEIGHT;


    public double calculerEcranX(double xMonde) {
        return xMonde - x;
    }

    public double calculerEcranY(double yMonde) {
        return yMonde - y;
    }

    public void suivre(Vaisseau vaisseau) {

        if (vaisseau.getGauche() < maxCameraGauche + x) {

            x = Math.max(vaisseau.getGauche() - maxCameraGauche, 0);

        }
        if (vaisseau.getDroite() > maxCameraDroite + x) {

            x = Math.min(vaisseau.getDroite() - maxCameraDroite, 3 * WIDTH);
        }
    }

    public void drawHitbox(GraphicsContext context) {
        if (Input.isKeyPressed(KeyCode.D)) {
            context.setStroke(Color.rgb(255, 255, 255, 0.5));
            context.strokeRect(calculerEcranX(x + maxCameraGauche), y, maxCameraDroite - maxCameraGauche, maxCameraBas);
            context.strokeLine(calculerEcranX(x + maxCameraGauche), 0, calculerEcranX(x + maxCameraGauche), HEIGHT);
            context.strokeLine(calculerEcranX(x + maxCameraDroite), 0, calculerEcranX(x + maxCameraDroite), HEIGHT);
            context.strokeLine(0, maxCameraBas, WIDTH, maxCameraBas);

        }
    }

    public void setX(double x) {
        this.x = x;
    }
}
