package com.example.tp2alienvshumain;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;

public abstract class Entite {
    protected double longueur, hauteur;
    static boolean modeDebug;
    protected double coordonnerX;
    protected double coordonnerY;

    protected double vitX = 0, vitY = 0;
    protected double accelX = 0, accelY = 0;

    public void update(double deltaTime) {
        updatePhysique(deltaTime);
    }

    public void updatePhysique(double deltaTime) {
        vitY += deltaTime * accelY;
        coordonnerY += vitY * deltaTime;
        vitX += deltaTime * accelX;
        coordonnerX += vitX * deltaTime;
    }

    public void drawHitbox(GraphicsContext context, double x, double y, double longueur, double hauteur, Camera camera) {
        setModeDebug(Input.isKeyPressed(KeyCode.D));
        if (modeDebug) {
            context.setStroke(Color.YELLOW);
            context.strokeRect(camera.calculerEcranX(x), y, longueur, hauteur);
        }
    }

    public boolean enContact(Entite entite) {

        return !(this.coordonnerX + this.longueur <= entite.coordonnerX ||
                this.coordonnerX >= entite.coordonnerX + entite.longueur ||
                this.coordonnerY + this.hauteur <= entite.coordonnerY ||
                this.coordonnerY >= entite.coordonnerY + entite.hauteur);
    }

    public void retomber() {
        if (getBas() < HEIGHT) {
            accelY = 1000;
        } else {
            vitY = 0;
            accelY = 0;
        }
    }

    public boolean checkEstEnVue(Camera camera) {

        return ((camera.calculerEcranX(getDroite()) > 0 && camera.calculerEcranX(getGauche()) < WIDTH) && camera.calculerEcranY(getBas()) > 0 && camera.calculerEcranY(getHaut()) < HEIGHT);
    }

    public void setModeDebug(boolean modeDebug) {
        this.modeDebug = modeDebug;
    }

    public double getLongueur() {
        return longueur;
    }


    public double getHauteur() {
        return hauteur;
    }


    public void setCoordonnerX(double coordonnerX) {
        this.coordonnerX = coordonnerX;
    }

    public void setCoordonnerY(double coordonnerY) {
        this.coordonnerY = coordonnerY;
    }

    public double getVitX() {
        return vitX;
    }

    public void setVitX(double vitX) {
        this.vitX = vitX;
    }

    public double getVitY() {
        return vitY;
    }

    public void setVitY(double vitY) {
        this.vitY = vitY;
    }

    public double getHaut() {
        return coordonnerY;
    }

    public double getBas() {
        return getHaut() + hauteur;
    }

    public double getGauche() {
        return coordonnerX;
    }

    public double getDroite() {
        return getGauche() + longueur;
    }

    public double getCentreX() {
        return getGauche() + longueur / 2;
    }

    public double getCentreY() {
        return getHaut() + hauteur / 2;
    }
}
