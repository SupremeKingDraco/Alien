package com.example.tp2alienvshumain;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Projectile extends Entite {
    protected double deltaX, deltaY;
    protected double magnitude;
    protected Image image;
    protected Vaisseau vaisseau;

    public Projectile(double coordonnerX, double coordonnerY, Vaisseau vaisseau) {
        this.coordonnerX = coordonnerX;
        this.coordonnerY = coordonnerY;
        this.vaisseau = vaisseau;
    }

    public void draw(GraphicsContext context, Camera camera) {
        longueur = getLongueur();
        hauteur = getHauteur();
        image = getImage();
        context.drawImage(image, camera.calculerEcranX(coordonnerX), camera.calculerEcranY(coordonnerY), longueur, hauteur);
        drawHitbox(context, coordonnerX, coordonnerY, longueur, hauteur, camera);
    }

    public abstract void update(double deltaTime, Vaisseau vaisseau);

    public double getLongueur() {
        return longueur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public Image getImage() {
        return image;
    }

    public double getDeltaX(double xCible) {
        deltaX = xCible - coordonnerX;
        return deltaX;
    }

    public double getDeltaY(double yCible) {
        deltaY = yCible - coordonnerY;
        return deltaY;
    }
}
