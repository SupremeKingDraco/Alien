package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PotDeFleurs extends Projectile {

    public PotDeFleurs(double coordonneX, double coordonneY, Vaisseau vaisseau) {
        super(coordonneX, coordonneY, vaisseau);
        image = new Image("pot-de-fleurs.png");
        longueur = 31;
        hauteur = 61;
        calculerMagnitude();
        calculerVitesseInit();
        accelY = 1000;
    }


    public void update(double deltaTime, Vaisseau vaisseau) {
        updatePhysique(deltaTime);
    }

    public void calculerMagnitude() {
        deltaX = getDeltaX(vaisseau.coordonnerX);
        deltaY = getDeltaY(vaisseau.coordonnerY - 400);
        magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void calculerVitesseInit() {
        vitX = 1000 * (deltaX / magnitude);
        vitY = 1000 * (deltaY / magnitude);
    }


}

