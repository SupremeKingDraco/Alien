package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;


import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;

public class Fond {
    private double coordoneXEtoile[] = new double[100];
    private double coordoneYEtoile[] = new double[100];
    private int tailleEtoile[] = new int[100];
    private ArrayList<Double> coordonnes = new ArrayList<>();
    boolean alterner;

    public Fond() {
        etoile();
        generer();
    }

    public void etoile() {
        for (int i = 0; i < 100; i++) {
            Random rnd = new Random();
            for (int j = 0; j < 100; j++) {
                coordoneXEtoile[i] = rnd.nextDouble(0, 4 * WIDTH);
                coordoneYEtoile[i] = rnd.nextDouble(0, (HEIGHT * 0.5));
                tailleEtoile[i] = rnd.nextInt(8, 16);
            }

        }
    }

    public void draw(GraphicsContext context, Camera camera) {

        context.setFill(Color.web("#1a1a1a"));
        context.fillRect(0, 0, 4 * WIDTH, HEIGHT);
        context.setFill(Color.web("#225500"));
        context.fillRect(0, HEIGHT * 0.9, 4 * WIDTH, HEIGHT * 0.1);
        context.setFill(Color.WHITE);
        context.setFont(Font.font(12));
        for (int i = 0; i < 100; i++) {
            context.setFont(Font.font(tailleEtoile[i]));
            context.fillText("*", camera.calculerEcranX(coordoneXEtoile[i]), coordoneYEtoile[i]);
        }
        Image tracteur = new Image("tracteur.png");
        Image grange = new Image("grange.png");

        for (int i = 0; i < coordonnes.size(); i++) {
            if (i % 2 == 0) {
                context.drawImage(grange, camera.calculerEcranX(coordonnes.get(i)), HEIGHT * 0.9 - grange.getHeight(), 113, 147);
            } else {
                context.drawImage(tracteur, camera.calculerEcranX(coordonnes.get(i)), HEIGHT * 0.9 - tracteur.getHeight(), 89, 55);
            }
        }


    }


    public void generer() {
        Random rnd = new Random();
        double distance = rnd.nextDouble(0, 800);
        coordonnes.add(distance);
        while (distance < WIDTH * 4) {
            distance += rnd.nextDouble(500, 800);
            coordonnes.add(distance);

        }

        if (rnd.nextBoolean()) {
            alterner = true;
        } else if (!rnd.nextBoolean()) {
            alterner = false;
        }


    }


}

