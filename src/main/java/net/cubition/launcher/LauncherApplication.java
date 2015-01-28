package net.cubition.launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;

import java.lang.reflect.Field;
import java.util.Queue;

/**
 * The LauncherFrame is the main GUI portion of the Cubition Launcher.
 */
public class LauncherApplication extends Application {
    private WebView webView;

    private Queue<Task<String, Runnable>> queue;

    private int totalTasks = 0;
    private int doneTasks = 0;

    @Override
    public void start(Stage stage) {

        // Icon
        stage.getIcons().add(new Image(getClass().getResource("/favicon.png").toString()));
        stage.setTitle("Cubition Launcher");

        Group box = new Group();

        // Create a containign scene
        final Scene scene = new Scene(box, 640, 480);
        scene.setFill(null);
        stage.setScene(scene);

        // Make the stage transparent
        stage.initStyle(StageStyle.TRANSPARENT);

        // Create webview
        webView = new WebView();
        box.getChildren().add(webView);
        webView.setMinSize(scene.getWidth(), scene.getHeight());
        webView.setMaxSize(scene.getWidth(), scene.getHeight());
        webView.setContextMenuEnabled(false);

        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker()
                .stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                stage.show();

                // Put it in the middle
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

                // Add custom hooks
                JSObject jsobj = (JSObject) webEngine.executeScript("window");

                jsobj.setMember("java", new LauncherCallback());
            }
        });

        // Enable dragging
        final Delta dragDelta = new Delta();
        webView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
            }
        });
        webView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });

        webEngine.documentProperty().addListener((observable, oldValue, newValue) -> {
            // Force the page to go transparent
            try {
                Field f = webEngine.getClass().getDeclaredField("page");
                f.setAccessible(true);
                com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
                page.setBackgroundColor((new java.awt.Color(0, 0, 0, 0)).getRGB());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Load the page
        webEngine.load(getClass().getResource("/html/login.html").toString());
    }

    /**
     * Wrapper for JavaFX's launch()
     */
    public static void start() {
        launch();
    }

    /**
     * Callback methods for the launcher
     */
    public class LauncherCallback {
        public void exit() {
            Platform.exit();
        }

        public void login(String username, String password) {
            queue = Launcher.buildQueue();
            totalTasks = queue.size();

            doneTasks = 1;
            Thread thread = new Thread(() -> queue.forEach(stringRunnableTask -> {
                int percent = (int) (((double) doneTasks) / ((double) totalTasks) * 100);
                System.out.println(percent + "%, " + stringRunnableTask.getKey());
                updateProgress(percent);
                updateProgressText(stringRunnableTask.getKey());

                try {
                    stringRunnableTask.getValue().run();
                } catch (Exception e) {
                    Launcher.error("Error while running task", e);
                    return;
                }

                doneTasks++;
            }));
            thread.setName("Launcher queue manager");
            thread.start();
        }

        public void updateProgress(int progress) {
            Platform.runLater(() -> webView.getEngine().executeScript("progressBar(" + progress + ")"));
        }

        public void updateProgressText(String progress) {
            Platform.runLater(() -> webView.getEngine().executeScript("progressContents(\"" + progress + "\")"));
        }

        public void advanced() {
            Launcher.error("Coming soon!");
        }
    }

    public static class Delta {
        public double x, y;
    }
}
