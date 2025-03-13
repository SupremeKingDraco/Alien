package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Vaisseau extends Entite {
    private int nombreVie;
    private int humainMangerPourVie;
    private boolean enVie;
    private boolean invincible;
    private boolean enMouvement = false;
    private Image teteExtraTerre = new Image("extraterrestre.png");
    private Image vaisseauOn = new Image("base-vaisseau-on.png");
    private Image vaisseauOff = new Image("base-vaisseau-off.png");
    private Rayon rayon = new Rayon();


    public Vaisseau() {
        coordonnerX = 50;
        coordonnerY = 50;
        longueur = 100;
        hauteur = 140;
        nombreVie = 4;
    }

    public void draw(GraphicsContext context, Camera camera) {
        double coordonnerCamera = camera.calculerEcranX(coordonnerX);
        context.drawImage(teteExtraTerre, coordonnerCamera + 50 - getCentreTeteX(), coordonnerY - getCentreTeteY() + 70);
        context.setFill(Color.rgb(255, 255, 0, 0.6));
        context.fillOval(coordonnerCamera, coordonnerY, 100, 140);
        if (enMouvement) {
            context.drawImage(vaisseauOn, coordonnerCamera - (vaisseauOn.getWidth() / 2) + 50, coordonnerY - getCentreTeteY() + 70 + teteExtraTerre.getHeight());
        } else {
            context.drawImage(vaisseauOff, coordonnerCamera - (vaisseauOn.getWidth() / 2) + 50, coordonnerY - getCentreTeteY() + 70 + teteExtraTerre.getHeight());
        }
        rayon.setCoordonnerX(getGauche());
        rayon.setCoordonnerY(getBas());
        rayon.draw(context, camera);
        drawHitbox(context, coordonnerX, coordonnerY, longueur, hauteur, camera);
    }


    public void update(double deltaTime) {
        if (Input.isKeyPressed(KeyCode.W) && nombreVie < 4) {
            nombreVie++;
            Input.setKeyPressed(KeyCode.W, false);
        }
        enMouvement = false;
        updateEnVie();
        setInvincible();
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean haut = Input.isKeyPressed(KeyCode.UP);
        boolean bas = Input.isKeyPressed(KeyCode.DOWN);

        if (enVie) {
//            mouvementVaisseau(deltaTime, droite, gauche, accelX, vitX);
//            mouvementVaisseau(deltaTime, haut, bas, accelY, vitY);
            if (droite) {
                accelX = 2000;
                enMouvement = true;
            } else if (gauche) {
                accelX = -2000;
                enMouvement = true;
            } else {
                double signeVitesse = vitX / Math.abs(vitX);
                accelX = -signeVitesse * 500;
                vitX += deltaTime * accelX;
                double nouveauSigneVitesse = vitX / Math.abs(vitX);
                if (nouveauSigneVitesse != signeVitesse) {
                    accelX = 0;
                    vitX = 0;
                }

            }
            if (haut) {
                accelY = -2000;
                enMouvement = true;
            } else if (bas) {
                accelY = 2000;
                enMouvement = true;
            } else {

                double signeVitesse = vitY / Math.abs(vitY);
                accelY = -signeVitesse * 500;
                vitY += deltaTime * accelY;
                double nouveauSigneVitesse = vitY / Math.abs(vitY);
                if (nouveauSigneVitesse != signeVitesse) {
                    accelY = 0;
                    vitY = 0;
                }
            }
        }
        updatePhysique(deltaTime);


        if (vitX >= 600) {
            vitX = 600;
        } else if (vitX <= -600) {
            vitX = -600;
        }

        if (vitY >= 600) {
            vitY = 600;
        } else if (vitY <= -600) {
            vitY = -600;
        }

        if (enVie) {
            coordonnerX = Math.min(coordonnerX, MainJavaFX.WIDTH * 4 - longueur);
            coordonnerX = Math.max(coordonnerX, 0);
            coordonnerY = Math.min(coordonnerY, MainJavaFX.HEIGHT * 0.6 - hauteur);
            coordonnerY = Math.max(coordonnerY, 0);

            if (coordonnerX >= MainJavaFX.WIDTH * 4 - longueur || coordonnerX <= 0) {
                vitX = 0;
                accelX = 0;
            }
            if (coordonnerY >= MainJavaFX.HEIGHT * 0.6 - hauteur || coordonnerY <= 0) {
                vitY = 0;
                accelY = 0;
            }
        }

        rayon.update(deltaTime);


    }

    private void mouvementVaisseau(double deltaTime, boolean positif, boolean negatif, double accel, double vit) {
        if (positif) {
            accel = 2000;
            enMouvement = true;
        } else if (negatif) {
            accel = -2000;
            enMouvement = true;
        } else {
            double signeVitesse = vit / Math.abs(vit);
            accel = -signeVitesse * 500;
            vit += deltaTime * accel;
            double nouveauSigneVitesse = vit / Math.abs(vit);
            if (nouveauSigneVitesse != signeVitesse) {
                accel = 0;
                vit = 0;
            }

        }
    }

    private void setInvincible() {
        invincible = Input.isKeyPressed(KeyCode.Q);
    }

    public void hit() {
        if (nombreVie > 0) {
            this.nombreVie--;
        }
    }

    public void soigner(int humainManger) {
        humainMangerPourVie += humainManger;
        if (nombreVie < 4) {
            if (humainMangerPourVie > 1) {
                humainMangerPourVie -= 2;
                this.nombreVie++;
            }
        }
    }

    public boolean getInvincible() {
        return invincible;
    }

    public void enlevement(Entite entite) {
        if (enVie) {
            rayon.enlevement(entite);
        }
    }


    private double getCentreTeteX() {
        return teteExtraTerre.getWidth() / 2;
    }

    private double getCentreTeteY() {
        return teteExtraTerre.getHeight() / 2;
    }


    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public int getNombreVie() {
        return nombreVie;
    }

    public void updateEnVie() {
        if (nombreVie < 1) {
            enVie = false;
        } else {
            enVie = true;
        }
    }

    public boolean getEnVie() {
        return enVie;
    }
}
