package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Aimant extends Projectile {
    final double K = 1500;
    final double QAIMANT = -900;
    final double QVAISSEAU = 100;

    public Aimant(double coordonneX, double coordonneY, Vaisseau vaisseau) {
        super(coordonneX, coordonneY, vaisseau);
        image = new Image("aimant.png");
        hauteur = 50;
        longueur = 50;
    }

    public void update(double deltaTime, Vaisseau vaisseau) {

        deltaX = coordonnerX - vaisseau.coordonnerX;
        deltaY = coordonnerY - vaisseau.coordonnerY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

// On normalise : ça revient à un vecteur unitaire qui va
// dans la direction de la force
        double proportionX = deltaX / distance;
        double proportionY = deltaY / distance;
        double forceElectrique = (K * QAIMANT * QVAISSEAU) / (distance * distance);
// On calcule la proportion de la force en X vs en Y
        double forceEnX = forceElectrique * proportionX;
        double forceEnY = forceElectrique * proportionY;

        vitX += forceEnX * deltaTime;
        vitY += forceEnY * deltaTime;
        double vitesseMagnitude = Math.sqrt(vitX * vitX + vitY * vitY);
        if (vitesseMagnitude > 300) {
            double proportionReduction = 300 / vitesseMagnitude;
            vitX *= proportionReduction;
            vitY *= proportionReduction;

        }

        coordonnerX += vitX * deltaTime;
        coordonnerY += vitY * deltaTime;

    }

}

