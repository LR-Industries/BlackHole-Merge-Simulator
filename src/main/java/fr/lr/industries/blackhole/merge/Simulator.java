package fr.lr.industries.blackhole.merge;

// Import the required classes and packages
import fr.lr.industries.blackhole.merge.simulator.SimulatorStackPane;
import fr.lr.industries.blackhole.merge.gravitational.waves.GravitationalWavesStackPane;
import fr.lr.industries.blackhole.merge.settings.SettingsStackPane;
import fr.lr.industries.blackhole.merge.simulator.BlackHole;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.Objects;

// The Simulator class is used to manage everything related to the simulation
public class Simulator extends Application {
    // The FPS and FRAME_TIME constants are used to set the frame rate of the simulation, and not waste too much computer resources
    public static final int FPS = 144; // Set to 144 FPS because it's the maximum refresh rate of my monitor
    // Convert the FPS to nanoseconds to obtain the maximum time a frame can take to be calculated
    public static final long FRAME_TIME = 1_000_000_000L / FPS;

    // The LAST_FRAME_TIME_SIMULATOR variable is used to store the time at which the last frame was rendered
    public static long LAST_FRAME_TIME_SIMULATOR = 0L;

    // The following constants can be changed to manage the default settings of the simulation
    // The WIDTH and HEIGHT constants are used to set the size of the canvas
    public static final double WIDTH = Screen.getPrimary().getBounds().getWidth();
    public static final double HEIGHT = Screen.getPrimary().getBounds().getHeight();

    // The SIMULATION_WIDTH and SIMULATION_HEIGHT constants are used to set the size of the simulation
    public static final double SIMULATION_WIDTH = WIDTH - (WIDTH * 0.25); // The - (WIDTH * 0.25) will allow the window to occupy 75% of the screen's width
    public static final double SIMULATION_HEIGHT = HEIGHT - (HEIGHT * 0.1); // The - (HEIGHT * 0.1) will allow the window to occupy 90% of the screen's height

    // The G constant is used to set the gravitational constant which I know by heart thanks to my so-called "physics" teacher named Mr. Eneman
    public static final double G = 6.674010551359e-11;

    // The C constant is used to set the speed of light in a vacuum which I also know by heart thanks to my so-called "physics" teacher named Mr. Eneman
    public static final double C = 299792458.0;

    // The BLACK_HOLE_EVENT_HORIZON_COLOR constant is used to set the color of the black hole event horizon
    public static final Color BLACK_HOLE_EVENT_HORIZON_COLOR = Color.RED;

    // The BACKGROUND_COLOR constant is used to set the color of the background
    public static final Color BACKGROUND_COLOR = Color.BLACK;

    // The BACKGROUND_DIFFERENT_COLOR constant is used to set the color of buttons or other elements that are different from the background
    public static final Color BACKGROUND_DIFFERENT_COLOR = Color.valueOf("#101010");
    public static final String BACKGROUND_DIFFERENT_COLOR_HEX = "#101010";

    // The BORDER_COLOR constant is used to set the color of the border
    public static final Color BORDER_COLOR = Color.WHITE;

    // The GRAVITATIONAL_WAVE_COLOR constant is used to set the color of the gravitational wave in the graph
    public static final Color GRAVITATIONAL_WAVE_COLOR = Color.WHITE;

    // The LABEL_COLOR constant is used to set the color of the label used everywhere in the simulation
    public static final Color LABEL_COLOR = Color.WHITE;
    public static final String LABEL_COLOR_HEX = "#FFFFFF";

    // The LABEL_FREQUENCY_COLOR constant is used to set the color of the label frequency used everywhere in the simulation
    public static final Color LABEL_FREQUENCY_COLOR = Color.GOLD;

    // The TIME_STEP constant is used to set the time step of the simulation
    public static double TIME_STEP = 0.1;

    // Those constants are used to set the default settings of the black holes and their names are self-explanatory
    public static final double DEFAULT_BLACK_HOLE_MASS = 5.0e15;
    public static final double DEFAULT_BLACK_HOLE_RADIUS = 44.0; // 44 = LEWIS HAMILTON
    public static final double DEFAULT_BLACK_HOLE_VELOCITY = 7.5;
    public static final double DEFAULT_POSITION_DIFFERENCE = 500.0;
    public static final double INCREASED_SURFACE_FACTOR = 1.0;

    // The BLACK_HOLE_1 constant is used to set the first black hole
    public static BlackHole BLACK_HOLE_1 = new BlackHole(
            SIMULATION_WIDTH * INCREASED_SURFACE_FACTOR / 2 - DEFAULT_POSITION_DIFFERENCE,
            SIMULATION_HEIGHT * INCREASED_SURFACE_FACTOR / 2,
            DEFAULT_BLACK_HOLE_VELOCITY,
            DEFAULT_BLACK_HOLE_VELOCITY,
            DEFAULT_BLACK_HOLE_MASS,
            DEFAULT_BLACK_HOLE_RADIUS
    );

    // The BLACK_HOLE_2 constant is used to set the second black hole
    public static BlackHole BLACK_HOLE_2 = new BlackHole(
            SIMULATION_WIDTH * INCREASED_SURFACE_FACTOR / 2 + DEFAULT_POSITION_DIFFERENCE,
            SIMULATION_HEIGHT * INCREASED_SURFACE_FACTOR / 2,
            -DEFAULT_BLACK_HOLE_VELOCITY,
            -DEFAULT_BLACK_HOLE_VELOCITY,
            DEFAULT_BLACK_HOLE_MASS,
            DEFAULT_BLACK_HOLE_RADIUS
    );

    // The GRAVITATIONAL_WAVE_FREQUENCY constant is used to set the gravitational wave frequency
    public static double GRAVITATIONAL_WAVE_FREQUENCY = 0;

    // The DISTANCE_TO_BE_MERGED_MULTIPLIER constant is used to set the distance to be merged multiplier
    public static double DISTANCE_TO_BE_MERGED_MULTIPLIER = 0.9;

    // The following constants are used to set the settings of the grid
    public static final double GRID_CELL_SIZE = 12.5;
    public static final double GRID_QUALITY = 5.0;
    public static final double GRID_FORCE_MULTIPLIER = 1.5;
    public static final double GRID_MAX_FORCE = 75.0;

    // This variable store the TIME_ELAPSED constant
    public static double TIME_ELAPSED = 0.0;

    // The following variables are used to store the different StackPanes of the application
    public static SimulatorStackPane simulatorStackPane = null;
    public static SettingsStackPane settingsStackPane = null;
    public static GravitationalWavesStackPane gravitationalWavesStackPane = null;

    // The start method is used to launch the application
    @Override
    public void start(final Stage primaryStage) {
        // Create a GridPane to hold the different StackPanes
        final GridPane gridPane = new GridPane();
        // Create a Scene to hold the GridPane
        final Scene scene = new Scene(gridPane, WIDTH, HEIGHT, Color.BLACK);

        // Create the different StackPanes of the application
        simulatorStackPane = new SimulatorStackPane(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        settingsStackPane = new SettingsStackPane(primaryStage, WIDTH - (WIDTH * 0.75), HEIGHT - (HEIGHT * 0.1)); // The - (WIDTH * 0.75) will allow the window to occupy 25% of the screen's width and the - (HEIGHT * 0.1) will allow the window to occupy 90% of the screen's height
        gravitationalWavesStackPane = new GravitationalWavesStackPane(WIDTH, HEIGHT - (HEIGHT * 0.9)); // The - (HEIGHT * 0.9) will allow the window to occupy 10% of the screen's height while occupying the full width of the screen

        // Add the StackPanes to the GridPane
        gridPane.add(simulatorStackPane.getStackPane(), 0, 0, 1, 1); // Put the stack pane in the top left corner of the screen
        gridPane.add(settingsStackPane.getStackPane(), 1, 0, 1, 1); // Put the stack pane in the top right corner of the screen
        gridPane.add(gravitationalWavesStackPane.getStackPane(), 0, 1, 2, 1); // Put the stack pane at the bottom of the screen

        // Add a listener to the Scene to detect when the F11 key is pressed
        scene.setOnKeyPressed(event -> {
            // If the F11 key is pressed, toggle the full screen mode
            if (event.getCode().equals(KeyCode.F11)) {
                // Toggle the full screen mode if it is not already in full screen mode, otherwise disable the full screen mode
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        // Set the window to be full screen
        //primaryStage.setFullScreen(true);
        // Set the full screen exit hint to be empty to hide the hint
        primaryStage.setFullScreenExitHint("");
        // Disable the default full screen exit key combination
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // Set the window to be not resizable
        primaryStage.setResizable(false);
        // Set the window to be always on top
        primaryStage.setAlwaysOnTop(true);
        // Set the title
        primaryStage.setTitle("BlackHole-Merge-Simulator");
        // Set the application icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/black-hole.png"))));
        // Set the scene of the stage with a newly created Scene containing the GridPane
        primaryStage.setScene(scene);
        // Show the stage
        primaryStage.show();
    }

    // The main method is used to launch the application
    public static void main(final String[] args) {
        // Clear the console
        System.out.print("\033[H\033[2J");
        // Print into the console nice things
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("BlackHole-Merge-Simulator");
        System.out.println("Version: 1.0.0");
        System.out.println("Author: Lucie Lebrun and Louis Ravignot Dos Santos on the behalf of LR Industries");
        System.out.println("Made with: Java 17, JavaFX 20, IntelliJ IDEA 2022.2.3");
        System.out.println("<3 And a lot of love <3");
        System.out.println("--------------------------------------------------------------------------------");
        // Launch the application
        launch(args);
    }

    // The otherBlackHole method is used to get the other black hole
    public static BlackHole otherBlackHole(final BlackHole blackHole) {
        // Return the other black hole
        return blackHole == BLACK_HOLE_1 ? BLACK_HOLE_2 : BLACK_HOLE_1;
    }
}
