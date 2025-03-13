package com.example.tp2alienvshumain;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;


public class Vache extends Entite {
    private Image vacheGauche = new Image("vache.png");
    private Image vacheDroite = new Image("vache-droite.png");
    private Image vache;
    Random rnd = new Random();
    boolean dirDroite;

    public Vache() {
        setDir();
        vitesseDepart();
        coordonnerX = rnd.nextDouble(1, 4 * WIDTH - vache.getWidth() - 1);
        coordonnerY = HEIGHT - vache.getHeight();
        longueur = vache.getWidth();
        hauteur = vache.getHeight();
    }

    public void vitesseDepart() {

        vitX = rnd.nextDouble(10, 50);
        if (!dirDroite) {
            vitX *= -1;
        }


    }

    public void draw(GraphicsContext context, Camera camera) {


        context.drawImage(vache, camera.calculerEcranX(coordonnerX), coordonnerY);
        drawHitbox(context, coordonnerX, coordonnerY, longueur, hauteur, camera);

    }

    public void setDir() {
        dirDroite = rnd.nextBoolean();
        if (dirDroite) {
            vache = vacheDroite;
        } else {
            vache = vacheGauche;
        }
    }

    public void changementDirectino() {
        if (getGauche() < 0) {
            coordonnerX = 0;
            vitX = vitX * -1;
            vache = vacheDroite;
        } else if (getDroite() > 4 * WIDTH) {
            coordonnerX = 4 * WIDTH - longueur;
            vitX = vitX * -1;
            vache = vacheGauche;

        }
    }

    public void update(double deltaTime) {
        if (getBas() < HEIGHT) {
            double temp = vitX;
            vitX = 0;
            updatePhysique(deltaTime);
            vitX = temp;
        } else {
            updatePhysique(deltaTime);

        }
        changementDirectino();
        retomber();
        if (getBas() < HEIGHT) {
        }
    }

}
