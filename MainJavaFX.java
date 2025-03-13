package com.example.tp2alienvshumain;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainJavaFX extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    private static boolean toucheRelacher = true;
    private AnimationTimer timer;

    @Override
    public void start(Stage stage) throws IOException {

        Scene sceneMenu = menu(stage);
        stage.setScene(sceneMenu);
        stage.setTitle("Invasion Agricole");
        stage.show();
    }

    public Scene menu(Stage stage) {

        StackPane root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Image imageIntro = new Image("intro.png");
        ImageView imageView = new ImageView(imageIntro);
        root.getChildren().add(imageView);

        VBox vboxButton = new VBox();
        vboxButton.setSpacing(10);
        vboxButton.setAlignment(Pos.CENTER);
        Button jouer = new Button("Jouer!");
        jouer.setFont(Font.font(30));
        jouer.setPrefSize(120, 30);
        Button infos = new Button("Infos");
        infos.setFont(Font.font(20));
        infos.setPrefSize(100, 30);
        vboxButton.getChildren().addAll(jouer, infos);
        root.getChildren().add(vboxButton);
        jouer.setOnAction(actionEvent -> {
            stage.setScene(sceneJeu(stage));
        });
        infos.setOnAction(actionEvent -> {
            stage.setScene(ecranInfos(stage));
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        return scene;

    }

    public Scene ecranInfos(Stage stage) {

        StackPane root = new StackPane();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        VBox info = new VBox();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.web("#1a1a1a"));
        context.fillRect(0, 0, 4 * WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Image miniVache = new Image("mini-vache.png");
        Image icone = new Image("icone.png");
        Image imageTemporaire;
        int x = 0;
        boolean alterner = true;
        do {
            if (alterner) {
                imageTemporaire = miniVache;
                context.drawImage(imageTemporaire, x + 10, 0);
                alterner = false;
            } else {
                imageTemporaire = icone;
                context.drawImage(imageTemporaire, x + 15, 0, 48, 48);
                alterner = true;

            }

            x += imageTemporaire.getWidth();
        } while (imageTemporaire.getWidth() + x < WIDTH);


        info.setAlignment(Pos.CENTER);
        info.setSpacing(20);
        Text titre = new Text("Invasion Agricole");
        titre.setFont(javafx.scene.text.Font.font(50));
        titre.setFill(Color.WHITE);
        Text nomsEquipe = new Text("Par Eric Yu et Toma Ciprian Busoi");
        nomsEquipe.setFont(javafx.scene.text.Font.font(25));
        nomsEquipe.setFill(Color.WHITE);
        Text description = new Text("Travail remis à Nicolas Hurtubise. Graphismes adaptés de https://game-icons.net/ ET DE https://openclipart.org." +
                "Développé dans le cadre du cours 420-203-RE - Développement de programmes dans un environnement graphique, au Collège de Bois-de-Boulogne");
        description.setFont(javafx.scene.text.Font.font(15));
        description.setFill(Color.WHITE);
        description.setWrappingWidth(500);
        Button bouttonRetour = new Button("Retour");
        info.getChildren().addAll(titre, nomsEquipe, description, bouttonRetour);
        root.getChildren().add(info);

        bouttonRetour.setOnAction(actionEvent -> {
            stage.setScene(menu(stage));
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.setScene(menu(stage));
            }
        });
        return scene;
    }

    public Scene sceneJeu(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.setScene(menu(stage));
            } else {
                if (e.getCode() != KeyCode.D && e.getCode() != KeyCode.Q) {
                    Input.setKeyPressed(e.getCode(), true);
                } else if (e.getCode() == KeyCode.D) {
                    changerInput(KeyCode.D);

                } else if (e.getCode() == KeyCode.Q) {
                    changerInput(KeyCode.Q);
                }

            }
            toucheRelacher = false;
        });

        scene.setOnKeyReleased((e) -> {
            if (e.getCode() != KeyCode.D && e.getCode() != KeyCode.Q) {
                Input.setKeyPressed(e.getCode(), false);
            }
            toucheRelacher = true;
        });

        Vaisseau vaisseau = new Vaisseau();
        Camera camera = new Camera();
        Jeu jeu = new Jeu(1, vaisseau, camera);

        timer = new AnimationTimer() {
            private long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) * 1e-9;
                context.clearRect(0, 0, WIDTH, HEIGHT);
                jeu.draw(context, camera);
                jeu.update(deltaTime);
                camera.suivre(vaisseau);
                if (jeu.getQuitter()) {
                    timer.stop();
                    stage.setScene(menu(stage));
                }
                if (jeu.getGagnee()) {
                    timer.stop();
                    jeu.effectuerTransition(root, timer);
                    jeu.afficherMessageGagnee(context);
                    jeu.partieGagnee();
                }


                lastTime = now;
            }
        };
        timer.start();
        return scene;
    }

    public static void changerInput(KeyCode keyCode) {
        if (Input.isKeyPressed(keyCode) && toucheRelacher) {
            Input.setKeyPressed(keyCode, false);
        } else {
            Input.setKeyPressed(keyCode, true);
        }
    }

    public static void main(String[] args) {
        launch();
    }


}