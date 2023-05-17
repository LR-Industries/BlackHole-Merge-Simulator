package fr.lr.industries.blackhole.merge.simulator;

// Import the required classes and packages
import fr.lr.industries.blackhole.merge.Simulator;
import fr.lr.industries.blackhole.merge.utils.StackPaneMaker;
import fr.lr.industries.blackhole.merge.utils.FontUtils;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import static fr.lr.industries.blackhole.merge.Simulator.*;

// The SimulatorStackPane class is used to create the stack pane for the simulator
public class SimulatorStackPane implements StackPaneMaker {
    // The WIDTH and HEIGHT variables are used to store the width and height of the screen
    private final double WIDTH;
    private final double HEIGHT;

    // The canvas variable is used to store the canvas for the simulation
    private final Canvas canvas;

    // The gc variable is used to store the graphics context of the canvas
    private final GraphicsContext gc;

    // The highestRecordedFrequency variable is used to store the maximum frequency label
    public final Label highestRecordedFrequency;

    // The animationTimer variable is used to store the animation timer for the simulation
    private AnimationTimer animationTimer;

    // The started variable is used to store if the simulation has started
    private boolean started = false;

    // The SimulatorStackPane constructor is used to create a new instance of the SimulatorStackPane class
    public SimulatorStackPane(final double width, final double height) {
        // Set the width and height of the screen
        this.WIDTH = width;
        this.HEIGHT = height;

        // Create a canvas to the size of the screen
        this.canvas = new Canvas(this.WIDTH, this.HEIGHT);
        // Get the graphics context of the canvas
        this.gc = this.canvas.getGraphicsContext2D();

        // Create the maximum frequency label
        this.highestRecordedFrequency = new Label();
        // Set the text color of the highestRecordedFrequency to the label frequency color
        this.highestRecordedFrequency.setTextFill(LABEL_FREQUENCY_COLOR);
        // Set the font of the highestRecordedFrequency
        this.highestRecordedFrequency.setFont(FontUtils.getFont("Montserrat-Bold", 20));
        // Put the highestRecordedFrequency at the left low corner of the screen
        StackPane.setAlignment(this.highestRecordedFrequency, Pos.TOP_LEFT);
        // Set the highestRecordedFrequency invisible
        this.highestRecordedFrequency.setVisible(false);
    }

    // The getStackPane method is used to get the stack pane
    @Override
    public StackPane getStackPane() {
        // Create the stack pane from the border pane
        final StackPane stackPane = new StackPane(new Pane(this.canvas, this.highestRecordedFrequency, BLACK_HOLE_1.getEventHorizonCircle(), BLACK_HOLE_1.getBlackHoleCircle(), BLACK_HOLE_2.getEventHorizonCircle(), BLACK_HOLE_2.getBlackHoleCircle()));

        // Draw the default canvas
        this.defaultCanvas();

        // Create the animation timer for the simulation
        this.animationTimer = new AnimationTimer() {
            // The handle method is called every frame of the simulation
            @Override
            public void handle(long now) {
                // Get the elapsed time since the last frame
                final long elapsedTime = now - LAST_FRAME_TIME_SIMULATOR;

                // If the elapsed time is greater than the frame time, update this canvas
                if (elapsedTime >= FRAME_TIME) {
                    // Check if both black holes are enabled
                    if (BLACK_HOLE_1.isEnabled() && BLACK_HOLE_2.isEnabled()) {
                        // Calculate the gravitational forces between the black holes
                        BLACK_HOLE_1.update(BLACK_HOLE_2);
                    }

                    // Update the screen with those new computed forces
                    defaultCanvas();

                    // Update the last frame time
                    LAST_FRAME_TIME_SIMULATOR = now;

                    // Update the time elapsed to the FPS * the time step divided by 1.0e9 to convert it to seconds
                    TIME_ELAPSED += (FRAME_TIME * TIME_STEP) / 1.0e9;
                }
            }

            // The start method is called when the animation timer is started
            @Override
            public void start() {
                // Set the started variable to true
                started = true;

                // Call the super method
                super.start();
            }

            // The stop method is called when the animation timer is stopped
            @Override
            public void stop() {
                // Set the started variable to false
                started = false;

                // Check if one of the black holes is disabled to check if the simulation is over
                if (!BLACK_HOLE_1.isEnabled() || !BLACK_HOLE_2.isEnabled()) {
                    // The simulation is over, so we show a little recap of the simulation
                    showSimulationRecap();
                }

                // Call the super method
                super.stop();
            }
        };

        // Return the stack pane
        return stackPane;
    }

    // The defaultCanvas method is used to reset the canvas to its default state
    public void defaultCanvas() {
        // Clear the canvas
        this.gc.clearRect(0, 0, this.WIDTH, this.HEIGHT);

        // Fill the canvas with the background color
        this.gc.setFill(BACKGROUND_COLOR);
        this.gc.fillRect(0, 0, this.WIDTH, this.HEIGHT);

        // Draw a border around the canvas with the border color
        this.gc.setStroke(BORDER_COLOR);
        this.gc.strokeRect(0, 0, this.WIDTH, this.HEIGHT);

        // Draw a grid on the canvas to make it easier to see the black holes
        this.drawGrid();

        // Save the graphics context
        this.gc.save();
    }

    // The drawGrid method is used to draw a grid on the canvas
    private void drawGrid() {
        // Iterate through the x's and y's of the canvas
        for (int x = 0; x < WIDTH; x += GRID_QUALITY) {
            for (int y = 0; y < HEIGHT; y += GRID_QUALITY) {
                // Check if the x or y is a multiple of the grid cell size, if so, the pixel is a side of a grid cell
                if (x % GRID_CELL_SIZE == 0 || y % GRID_CELL_SIZE == 0) {
                    // First, check if the pixel is not in the event horizon of the black holes to not waste computation time
                    if ((BLACK_HOLE_1.isEnabled() && BLACK_HOLE_1.isInEventHorizon(x, y)) || (BLACK_HOLE_2.isEnabled() && BLACK_HOLE_2.isInEventHorizon(x, y))) continue;

                    // See how far the black holes are from the current pixel
                    final double distanceToBlackHole1 = BLACK_HOLE_1.getDistance(x, y);
                    final double distanceToBlackHole2 = BLACK_HOLE_2.getDistance(x, y);

                    // Calculate the difference between the distance of the black holes and the current pixel
                    final double dxBlackHole1 = BLACK_HOLE_1.getX() - x;
                    final double dyBlackHole1 = BLACK_HOLE_1.getY() - y;
                    final double dxBlackHole2 = BLACK_HOLE_2.getX() - x;
                    final double dyBlackHole2 = BLACK_HOLE_2.getY() - y;

                    // Calculate the force of the black holes on the current pixel
                    final double forceBlackHole1 = BLACK_HOLE_1.isEnabled() ? BLACK_HOLE_1.getForce(distanceToBlackHole1) : 0;
                    final double forceBlackHole2 = BLACK_HOLE_2.isEnabled() ? BLACK_HOLE_2.getForce(distanceToBlackHole2) : 0;

                    // Check if the force that the pixel is going to endure is going to make it goes brrrr
                    if (forceBlackHole1 > GRID_MAX_FORCE || forceBlackHole2 > GRID_MAX_FORCE) continue;

                    // Calculate the gravitational forces between the black holes and the current pixel
                    final double gxBlackHole1 = BLACK_HOLE_1.isEnabled() ? dxBlackHole1 / distanceToBlackHole1 * forceBlackHole1 : 0;
                    final double gyBlackHole1 = BLACK_HOLE_1.isEnabled() ? dyBlackHole1 / distanceToBlackHole1 * forceBlackHole1 : 0;
                    final double gxBlackHole2 = BLACK_HOLE_2.isEnabled() ? dxBlackHole2 / distanceToBlackHole2 * forceBlackHole2 : 0;
                    final double gyBlackHole2 = BLACK_HOLE_2.isEnabled() ? dyBlackHole2 / distanceToBlackHole2 * forceBlackHole2 : 0;

                    // Calculate the total gravitational forces between the black holes and the current pixel
                    final double gx = (gxBlackHole1 + gxBlackHole2) * GRID_FORCE_MULTIPLIER;
                    final double gy = (gyBlackHole1 + gyBlackHole2) * GRID_FORCE_MULTIPLIER;

                    // Check if the new position of the pixel is in the event horizon of the black holes, if so, continue to the next pixel to gain some performance
                    if ((BLACK_HOLE_1.isEnabled() && BLACK_HOLE_1.isInEventHorizon(x + gx, y + gy)) || (BLACK_HOLE_2.isEnabled() && BLACK_HOLE_2.isInEventHorizon(x + gx, y + gy))) continue;

                    // Set the brightness of the pixel in function of the force that is endured
                    gc.setFill(Color.hsb(0, 0, Math.min(1, Math.abs(1 - Math.abs((gx + gy) / Simulator.GRID_MAX_FORCE)))));
                    // Actually draw the pixel
                    gc.fillRect(x + gx, y + gy, 1, 1);
                }
            }
        }
    }

    // The showSimulationRecap method is used to show a little recap of the simulation
    private void showSimulationRecap() {
        // Set the text of the highestRecordedFrequency to the latest gravitational wave frequency
        this.highestRecordedFrequency.setText("Highest Recorded Frequency: " + String.format("%.0f", gravitationalWavesStackPane.MAX_GRAVITATIONAL_WAVE_FREQUENCY) + " Hz");
        // Show the highestRecordedFrequency
        this.highestRecordedFrequency.setVisible(true);
    }

    // The getAnimationTimer method is used to get the animation timer for the simulation
    public AnimationTimer getAnimationTimer() {
        // Return the animation timer
        return this.animationTimer;
    }

    // The isStarted method is used to check if the animation timer is started
    public boolean isStarted() {
        // Return the started variable
        return this.started;
    }

    // The getWidth method is used to get the width of this stack pane
    public double getWidth() {
        // Return the width of this stack pane
        return this.WIDTH;
    }

    // The getHeight method is used to get the height of this stack pane
    public double getHeight() {
        // Return the height of this stack pane
        return this.HEIGHT;
    }
}
