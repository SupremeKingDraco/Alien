package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.time.Year;

public class Girouette extends Projectile {


    public Girouette(double coordonneX, double coordonneY, Vaisseau vaisseau) {
        super(coordonneX, coordonneY, vaisseau);
        longueur = 53;
        hauteur = 55;
        image = new Image("girouette.png");
        calculerMagnitude();
        calculerVitesseInit();
    }

    public void update(double deltaTime, Vaisseau vaisseau) {
        updatePhysique(deltaTime);
    }

    public void calculerMagnitude() {
        deltaX = getDeltaX(vaisseau.coordonnerX);
        deltaY = getDeltaY(vaisseau.coordonnerY);
        magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void calculerVitesseInit() {
        vitX = 500 * (deltaX / magnitude);
        vitY = 500 * (deltaY / magnitude);
    }

}

