package com.example.tp2alienvshumain;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;


public class Humain extends Entite {
    Random rnd = new Random();
    private Image fermier = new Image("fermier.png");
    private Image fermiere = new Image("fermiere.png");
    private Image humain;
    private double temps = rnd.nextDouble(0, 2);
    private final double tempsRecharge = 2;
    private Vaisseau vaisseau;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Projectile> projectileAEnlever = new ArrayList<>();

    public Humain(Vaisseau vaisseau) {
        this.vaisseau = vaisseau;
        setGenre();
        coordonnerX = rnd.nextDouble(0, 4 * WIDTH - longueur);
        coordonnerY = HEIGHT - humain.getHeight();
        longueur = humain.getWidth();
        hauteur = humain.getHeight();

    }

    public void draw(GraphicsContext context, Camera camera) {
        context.drawImage(humain, camera.calculerEcranX(coordonnerX), coordonnerY);

        for (var x : projectiles) {
            x.draw(context, camera);
        }
        drawHitbox(context, coordonnerX, coordonnerY, longueur, hauteur, camera);
    }

    public void setGenre() {
        Random rnd = new Random();
        if (rnd.nextBoolean()) {
            humain = fermier;
        } else {
            humain = fermiere;

        }
    }

    public Projectile creerProjectile() {
        var rnd = new Random();
        int choix = rnd.nextInt(0, 3);
        Projectile projectile;
        if (choix == 0) {
            projectile = new Girouette(coordonnerX + humain.getWidth() / 2, coordonnerY + humain.getHeight() / 2, vaisseau);

        } else if (choix == 1) {
            projectile = new PotDeFleurs(coordonnerX + humain.getWidth() / 2, coordonnerY + humain.getHeight() / 2, vaisseau);

        } else {
            projectile = new Aimant(coordonnerX + humain.getWidth() / 2, coordonnerY + humain.getHeight() / 2, vaisseau);

        }
        return projectile;
    }

    public void update(double deltaTime, Camera camera) {
        updatePhysique(deltaTime);
        retomber();


        if (checkEstEnVue(camera)) {
            temps += deltaTime;

            if (temps >= tempsRecharge) {
                temps -= 2;

                var projectile = creerProjectile();
                projectiles.add(projectile);

            }
        }

        for (var x : projectiles) {
            x.update(deltaTime, vaisseau);
            if (vaisseau.enContact(x)) {
                if (!vaisseau.getInvincible()) {
                    vaisseau.hit();
                }
                projectileAEnlever.add(x);
            } else if (!x.checkEstEnVue(camera)) {
                projectileAEnlever.add(x);
            }
        }
        for (var x : projectileAEnlever) {
            projectiles.remove(x);

        }


    }

}

