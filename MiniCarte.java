package com.example.tp2alienvshumain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;

public class MiniCarte extends Entite {
    ArrayList<Vache> vaches;
    Vaisseau vaisseau;

    public MiniCarte(ArrayList<Vache> vaches, Vaisseau vaisseau) {
        this.vaches = vaches;
        this.vaisseau = vaisseau;
    }

    public void draw(GraphicsContext context) {
        context.setStroke(Color.WHITE);
        context.setLineWidth(3);

        context.strokeLine(10, 42 + 30, 10 + 144, 42 + 30);
        context.setFill(Color.WHITE);
        for (var vache : vaches) {
            context.fillOval(((vache.getGauche() * 144) / (4 * WIDTH)) + 10, 42 + 30 - 5, 10, 10);
        }
        context.setFill(Color.GREEN);
        context.fillOval(((vaisseau.getCentreX() * 144) / (4 * WIDTH)) + 10, 42 + 30 - 5, 10, 10);
    }

}
