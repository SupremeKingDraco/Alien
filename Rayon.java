package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;

public class Rayon extends Entite {
    private double tempsAppuyee = 0, tempsAttente = 0;
    private double charge;


    public Rayon() {
        vitY = 150;
        longueur = 100;
        charge = 1;

    }

    public void draw(GraphicsContext context, Camera camera) {
        drawBarre(context);
        context.setFill(Color.rgb(255, 255, 0, 0.5));
        context.fillRect(camera.calculerEcranX(coordonnerX), coordonnerY, longueur, hauteur);
        drawHitbox(context, coordonnerX, coordonnerY, longueur, hauteur, camera);
    }

    public void update(double deltaTime) {

        tempsAttente = tempsAttente - deltaTime;
        if (Input.isKeyPressed(KeyCode.E)) {
            tempsAttente = 0;
            charge = 1;
        }
        if (attendre()) {
            Input.setKeyPressed(KeyCode.SPACE, false);
        }
        attendre();
        if (Input.isKeyPressed(KeyCode.SPACE)) {
            tempsAppuyee += deltaTime;
            decharger(deltaTime);

        }
        if (!(Input.isKeyPressed(KeyCode.SPACE) || tempsAttente > 5)) {
            tempsAppuyee = 0;
            recharger(deltaTime);
        }
        charge = Math.min(charge, 1);
        charge = Math.max(charge, 0);
        hauteur = calculerhauteur();
    }

    private void recharger(double deltaTime) {
        charge += 0.2 * deltaTime;
    }

    private void decharger(double deltaTime) {
        charge -= 0.25 * deltaTime;
    }

    public void enlevement(Entite entite) {

        if (enContact(entite)) {
            entite.setVitY(-100);
        }
    }

    private double calculerhauteur() {
        hauteur = tempsAppuyee * vitY;
        if (hauteur > HEIGHT)
            hauteur = HEIGHT;

        return hauteur;
    }

    public void drawBarre(GraphicsContext context) {
        Image batterie = new Image("batterie.png");
        if (tempsAttente > 0) {
            context.setFill(Color.RED);
        } else if (charge > 0.2) {
            context.setFill(Color.YELLOW);
        } else if (charge > 0) {
            context.setFill(Color.ORANGE);
        }
        context.fillRect(20 - 10, 20 - 5, batterie.getWidth() * 6 * (charge), batterie.getHeight() + 10);
        context.strokeRect(20 - 10, 20 - 5, batterie.getWidth() * 6, batterie.getHeight() + 10);
        context.drawImage(batterie, 20, 20);
    }

    public boolean attendre() {
        if (charge == 0) {
            tempsAttente = 5;
            tempsAppuyee = 0;
        }
        if (tempsAttente > 0) {
            return true;
        } else {
            return false;
        }
    }


}

