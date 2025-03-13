package com.example.tp2alienvshumain;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

import static com.example.tp2alienvshumain.MainJavaFX.HEIGHT;
import static com.example.tp2alienvshumain.MainJavaFX.WIDTH;

public class Jeu {
    private int numeroNiveau;
    private ArrayList<Vache> vaches = new ArrayList<>();
    private ArrayList<Humain> humains = new ArrayList<>();
    private Vaisseau vaisseau;
    private int vacheTuer = 0;

    private boolean quitter;
    private boolean gagnee;


    private Fond fond;
    private MiniCarte miniCarte;
    private final Image imageVache = new Image("mini-vache.png");
    private final Image imageVaisseau = new Image("icone.png");
    private boolean tombeGauche;
    private ArrayList<Entite> entiteTuee = new ArrayList<>();
    private Camera camera;
    private int compteurHumainMangee;

    public Jeu(int numeroNiveau, Vaisseau vaisseau, Camera camera) {
        this.numeroNiveau = numeroNiveau;
        this.vaisseau = vaisseau;
        this.camera = camera;
        initNiveau();
    }

    public void creerEntite() {
        int nbDeVaches = 5 + 2 * numeroNiveau;
        for (int i = 0; i < nbDeVaches; i++) {
            vaches.add(new Vache());
        }
        int nbHumains = 3 + 3 * (numeroNiveau - 1);
        for (int i = 0; i < nbHumains; i++) {
            humains.add(new Humain(vaisseau));
        }

    }

    public void creerArrierePlan() {
        miniCarte = new MiniCarte(vaches, vaisseau);
        fond = new Fond();

    }

    public void draw(GraphicsContext context, Camera camera) {
        context.clearRect(0, 0, WIDTH, HEIGHT);
        fond.draw(context, camera);
        vaisseau.draw(context, camera);
        camera.drawHitbox(context);

        for (var vache : vaches) {
            vache.draw(context, camera);
        }
        for (var humain : humains) {
            humain.draw(context, camera);
        }
        miniCarte.draw(context);
        drawCompteurVacheManger(context);
        drawCompteurVieVaisseau(context);
    }

    public void update(double deltaTime) {
        vaisseau.update(deltaTime);
        if (!vaisseau.getEnVie()) {
            partiePerdu();
        }
        if (vaches.isEmpty() || Input.isKeyPressed(KeyCode.I)) {
            gagnee = true;
            Input.setKeyPressed(KeyCode.I, false);
        }


        for (var vache : vaches) {

            vache.update(deltaTime);
            vaisseau.enlevement(vache);

            if (vaisseau.enContact(vache)) {
                entiteTuee.add(vache);
            }
        }
        if (vaisseau.getEnVie() && vaisseau.getEnVie()) {
            vacheTuer += entiteTuee.size();
            vaches.removeAll(entiteTuee);
            entiteTuee.clear();
        }

        for (var humain : humains) {
            humain.update(deltaTime, camera);
            vaisseau.enlevement(humain);

            if (vaisseau.enContact(humain) && vaisseau.getEnVie()) {
                compteurHumainMangee++;
                vaisseau.soigner(compteurHumainMangee);
                entiteTuee.add(humain);
            }

        }


        if (vaisseau.getEnVie()) {
            humains.removeAll(entiteTuee);
            entiteTuee.clear();
        }


    }


    public void drawCompteurVacheManger(GraphicsContext context) {
        context.drawImage(imageVache, 164, 10);
        context.setFont(Font.font(imageVache.getHeight()));
        context.fillText(Integer.toString(vacheTuer), 164 + imageVache.getWidth() + 10, 5 + imageVache.getHeight());
    }

    public void drawCompteurVieVaisseau(GraphicsContext context) {
        double rapport = imageVaisseau.getHeight() / imageVache.getHeight();
        context.drawImage(imageVaisseau, 164 + imageVache.getWidth() + 10 + 50, 10, imageVaisseau.getWidth() / rapport, imageVaisseau.getHeight() / rapport);
        if (vaisseau.getInvincible()) {
            context.fillText(":)", 164 + imageVache.getWidth() + 10 + 50 + imageVaisseau.getWidth() / rapport, 5 + imageVaisseau.getHeight() / rapport);
        } else {
            context.fillText(Integer.toString(vaisseau.getNombreVie()), 164 + imageVache.getWidth() + 10 + 50 + imageVaisseau.getWidth() / rapport, 5 + imageVaisseau.getHeight() / rapport);
        }

    }


    public void afficherMessageGagnee(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.setFont(Font.font(50));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        Text messageGagnee = new Text("Niveau " + numeroNiveau + " terminé!!");
        context.fillText(messageGagnee.getText(), WIDTH / 2, HEIGHT / 2);
    }

    public void effectuerTransition(Pane root, AnimationTimer timer) {
        Rectangle rectangleDeTransition = new Rectangle(WIDTH, HEIGHT, Color.BLACK);
        rectangleDeTransition.setOpacity(0);
        root.getChildren().add(rectangleDeTransition);
        FadeTransition transition = new FadeTransition(Duration.seconds(2.5), rectangleDeTransition);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(event -> {
            rectangleDeTransition.setOpacity(0);
            root.getChildren().remove(rectangleDeTransition);
            timer.start();
        });
        transition.play();
    }

    public void clearNiveau() {
        vaches.clear();
        humains.clear();
    }

    public void initNiveau() {
        gagnee = false;
        clearNiveau();
        creerEntite();
        creerArrierePlan();
        Random rnd = new Random();
        tombeGauche = rnd.nextBoolean();
        vaisseau.setCoordonnerX(WIDTH * 4 / 2 - vaisseau.getLongueur() / 2);
        vaisseau.setCoordonnerY(HEIGHT / 2);
        camera.setX(1.5 * WIDTH);
    }


    public boolean getGagnee() {
        return gagnee;
    }

    public boolean getQuitter() {
        return quitter;

    }

    public void partiePerdu() {
        double vit = 100;
        if (tombeGauche) {
            vaisseau.setVitX(vit * -1);
        } else {
            vaisseau.setVitX(vit);
        }
        vaisseau.setVitY(vit);
        if (vaisseau.getHaut() > HEIGHT) {
            quitter = true;
        }
    }

    public void partieGagnee() {
        numeroNiveau++;
        initNiveau();
    }

    public void mangerEntite() {
        if (vaisseau.getEnVie()) {
            if (entiteTuee.get(0).getClass() == Vache.class) {
                vacheTuer += entiteTuee.size();
                vaches.removeAll(entiteTuee);
            } else if (entiteTuee.get(0).getClass() == Humain.class) {
                humains.removeAll(entiteTuee);
            }
            entiteTuee.clear();
        }
    }

}
