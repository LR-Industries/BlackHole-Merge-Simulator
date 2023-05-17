package fr.lr.industries.blackhole.merge.settings;

// Import the required classes and packages
import fr.lr.industries.blackhole.merge.Simulator;
import fr.lr.industries.blackhole.merge.utils.StackPaneMaker;
import fr.lr.industries.blackhole.merge.utils.FontUtils;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static fr.lr.industries.blackhole.merge.Simulator.*;

// Create a class to create the settings stackPane
public class SettingsStackPane implements StackPaneMaker {
    private final Stage primaryStage;
    // Create a stackPane variable to store the stackPane
    private final StackPane stackPane;
    // Create a WIDTH and a HEIGHT variable to store the width and the height of the screen
    private final double WIDTH;
    private final double HEIGHT;

    // This variable is used to store the label for the time elapsed since the simulation started
    private final Label timeElapsedLabel = new Label("Time elapsed: 0.00s \nFPS: 0");

    // This variable is used to store the logo of our dear company, made using GIMP in 5 minutes by our dear co-founder, but it's okay because he's a supposed genius by the founder
    private final ImageView logoLrIndustries = new ImageView(new Image("images/logo-lr-industries.png"));

    // Load the information labels from the information.md file, we do it here to avoid doing it in the loop of the animation timer and having lags
    private final Collection<Label> information = this.getLabelsFromMarkdownFile("information.md");

    // Those variables are used to store the sliders and are self-explanatory
    private final Slider timeSlider = this.getSlider(0.01, 1, TIME_STEP, 0.01);
    private final Slider blackHole1RadiusSlider = this.getSlider(25.0, ((SIMULATION_HEIGHT / 2) / 1.02), BLACK_HOLE_1.getRadius(), 1);
    private final Slider blackHole2RadiusSlider = this.getSlider(25.0, ((SIMULATION_HEIGHT / 2) / 1.02), BLACK_HOLE_2.getRadius(), 1);
    private final Slider blackHole1MassSlider = this.getSlider(1.0, DEFAULT_BLACK_HOLE_MASS * 4, BLACK_HOLE_1.getMass(), 1);
    private final Slider blackHole2MassSlider = this.getSlider(1.0, DEFAULT_BLACK_HOLE_MASS * 4, BLACK_HOLE_2.getMass(), 1);
    private final Slider distanceToBeMergedMultiplierSlider = this.getSlider(0, 10, DISTANCE_TO_BE_MERGED_MULTIPLIER, 0.1);

    // Those variables are used to store the values of the settings
    private double blackHole1Radius = DEFAULT_BLACK_HOLE_RADIUS;
    private double blackHole2Radius = DEFAULT_BLACK_HOLE_RADIUS;
    private double blackHole1Mass = DEFAULT_BLACK_HOLE_MASS;
    private double blackHole2Mass = DEFAULT_BLACK_HOLE_MASS;

    // Create a constructor to initialize the variables
    public SettingsStackPane(final Stage primaryStage, final double width, final double height) {
        // Assign the variables to the class variables
        this.primaryStage = primaryStage;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.stackPane = new StackPane();
    }

    // The getStackPane method is used to get the stackPane of the settings
    @Override
    public StackPane getStackPane() {
        // Create a canvas to draw on our settings
        final Canvas canvas = new Canvas(this.WIDTH, this.HEIGHT);
        // Create a graphics context to draw on the canvas
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        // Preserve the ratio of the logo when resizing the image
        this.logoLrIndustries.setPreserveRatio(true);
        // Set the width of the logo to the width of the screen
        this.logoLrIndustries.setFitWidth(this.WIDTH);

        // Little debug to see the width and the height of the screen for testing purposes
        this.logoLrIndustries.setOnMouseClicked(event -> {
            System.out.println("Screen Width :" + Simulator.WIDTH);
            System.out.println("Screen Height :" + Simulator.HEIGHT);
            System.out.println("Simulator Width :" + simulatorStackPane.getWidth());
            System.out.println("Simulator Height :" + simulatorStackPane.getHeight());
            System.out.println("Settings Width :" + this.WIDTH);
            System.out.println("Settings Height :" + this.HEIGHT);
            System.out.println("GravitationalWaves Width :" + gravitationalWavesStackPane.getWidth());
            System.out.println("GravitationalWaves Height :" + gravitationalWavesStackPane.getHeight());
        });

        // Fill the canvas with the background color
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, this.WIDTH, this.HEIGHT);

        // Draw a border around the canvas with the border color
        gc.setStroke(BORDER_COLOR);
        gc.strokeRect(0, 0, this.WIDTH, this.HEIGHT);

        // Create an animation timer for the labels and the background
        new AnimationTimer() {
            @Override
            public void handle(final long now) {
                // Get the elapsed time since the last frame
                final long elapsedTime = now - LAST_FRAME_TIME_SIMULATOR;

                // If the elapsed time is greater than the frame time, update this canvas
                if (elapsedTime >= FRAME_TIME) {
                    // Fill the canvas with the background color
                    gc.setFill(BACKGROUND_COLOR);
                    gc.fillRect(0, 0, WIDTH, HEIGHT);

                    // Draw a border around the canvas with the border color
                    gc.setStroke(BORDER_COLOR);
                    gc.strokeRect(0, 0, WIDTH, HEIGHT);

                    // Update the time elapsed label and the FPS rounded to the unit
                    timeElapsedLabel.setText("Time elapsed: " + String.format("%.2f", TIME_ELAPSED) + "s\n" + "FPS: " + String.format("%.0f", 1e9 / elapsedTime));
                }
            }
        }.start(); // Start it automatically, we don't need to stop it at one point in the simulation

        // Get the button that closes the simulation
        final Button closeButton = this.getCloseButton();

        // Get the HBox that contains the control buttons
        final HBox controlCenter = this.getControlCenter();

        // Get the BorderPane that contains the time elapsed
        final BorderPane timeElapsedBorderPane = this.getTimeElapsedBorderPane();

        // Get the BorderPane that contains the first black hole size slider
        final BorderPane blackHole1RadiusSliderBorderPane = this.getBlackHole1RadiusSlider();

        // Get the BorderPane that contains the second black hole size slider
        final BorderPane blackHole2RadiusSliderBorderPane = this.getBlackHole2RadiusSlider();

        // Get the BorderPane that contains the first black hole mass slider
        final BorderPane blackHole1MassSliderBorderPane = this.getBlackHole1MassSlider();

        // Get the BorderPane that contains the second black hole mass slider
        final BorderPane blackHole2MassSliderBorderPane = this.getBlackHole2MassSlider();

        // Get the BorderPane that contains the time step slider
        final BorderPane timeStepSliderBorderPane = this.getTimeStepSlider();

        // Get the BorderPane that contains the distance to be merged multiplier slider
        final BorderPane distanceToBeMergedMultiplierSliderBorderPane = this.getDistanceToBeMergedMultiplierSlider();

        // Create a VBox to hold everything
        final VBox settingsVBox = new VBox(10);
        // Set the alignment of the VBox to the center of the settings stackPane
        settingsVBox.setAlignment(Pos.CENTER);

        // Add everything to the VBox
        settingsVBox.getChildren().addAll(this.logoLrIndustries, timeElapsedBorderPane, timeStepSliderBorderPane, blackHole1RadiusSliderBorderPane, blackHole2RadiusSliderBorderPane, blackHole1MassSliderBorderPane, blackHole2MassSliderBorderPane, distanceToBeMergedMultiplierSliderBorderPane, controlCenter);

        // Add the canvas and the VBox to the stackPane
        this.stackPane.getChildren().addAll(canvas, closeButton, settingsVBox);

        // Bring the close button to the front
        closeButton.toFront();

        // Return the stackPane of the settings
        return this.stackPane;
    }

    private BorderPane getTimeElapsedBorderPane() {
        // Create a BorderPane to hold the time elapsed
        final BorderPane timeElapsedBorderPane = new BorderPane();

        // Set the font of the label to Montserrat-Medium and the size to 20
        this.timeElapsedLabel.setFont(FontUtils.getFont("Montserrat-Medium", LABEL_FONT_SIZE_SMALL));
        // Set the text color of the label to the label color
        this.timeElapsedLabel.setTextFill(LABEL_COLOR);

        // Add the label to the BorderPane
        timeElapsedBorderPane.setCenter(this.timeElapsedLabel);

        // Return the BorderPane
        return timeElapsedBorderPane;
    }

    // The getTimeStepSlider method is used to get the BorderPane that contains the time step slider
    private BorderPane getTimeStepSlider() {
        // Return the BorderPane that contains the time step slider
        return getSliderBorderPane(this.timeSlider, "Time Step", "0.00", (observable, oldValue, newValue) -> {
            // Set the time step to the new value
            TIME_STEP = newValue.doubleValue();
        });
    }

    // The getBlackHole1RadiusSlider method is used to get the BorderPane that contains the first black hole size slider
    private BorderPane getBlackHole1RadiusSlider() {
        // Return the BorderPane that contains the black hole size slider
        return getSliderBorderPane(this.blackHole1RadiusSlider, "Black Hole 1 Radius (UA)", "0.00", (observable, oldValue, newValue) -> {
            // Set the black hole radius to the new value
            BLACK_HOLE_1.setRadius(newValue.doubleValue());
            this.blackHole1Radius = newValue.doubleValue();
            // Update the black hole circle
            BLACK_HOLE_1.updateCircles(true);
            // Reset the canvas
            simulatorStackPane.defaultCanvas();
        });
    }

    // The getBlackHole2RadiusSlider method is used to get the BorderPane that contains the second black hole size slider
    private BorderPane getBlackHole2RadiusSlider() {
        // Return the BorderPane that contains the black hole size slider
        return getSliderBorderPane(this.blackHole2RadiusSlider, "Black Hole 2 Radius (UA)", "0.00", (observable, oldValue, newValue) -> {
            // Set the black hole radius to the new value
            BLACK_HOLE_2.setRadius(newValue.doubleValue());
            this.blackHole2Radius = newValue.doubleValue();
            // Update the black hole circle
            BLACK_HOLE_2.updateCircles(true);
            // Reset the canvas
            simulatorStackPane.defaultCanvas();
        });
    }

    // The getBlackHole1MassSlider method is used to get the BorderPane that contains the first black hole size slider
    private BorderPane getBlackHole1MassSlider() {
        // Return the BorderPane that contains the black hole size slider
        return getSliderBorderPane(this.blackHole1MassSlider, "Black Hole 1 Mass (KG)", "0.0E0", (observable, oldValue, newValue) -> {
            // Set the black hole radius to the new value
            BLACK_HOLE_1.setMass(newValue.doubleValue());
            this.blackHole1Mass = newValue.doubleValue();
            // Reset the canvas
            simulatorStackPane.defaultCanvas();
        });
    }

    // The getBlackHole2MassSlider method is used to get the BorderPane that contains the second black hole size slider
    private BorderPane getBlackHole2MassSlider() {
        // Return the BorderPane that contains the black hole size slider
        return getSliderBorderPane(this.blackHole2MassSlider, "Black Hole 2 Mass (KG)", "0.0E0", (observable, oldValue, newValue) -> {
            // Set the black hole radius to the new value
            BLACK_HOLE_2.setMass(newValue.doubleValue());
            this.blackHole2Mass = newValue.doubleValue();
            // Reset the canvas
            simulatorStackPane.defaultCanvas();
        });
    }

    // The getDistanceToBeMergedMultiplierSlider method is used to get the BorderPane that contains the distance to be merged multiplier slider
    private BorderPane getDistanceToBeMergedMultiplierSlider() {
        // Return the BorderPane that contains the distance to be merged multiplier slider
        return getSliderBorderPane(this.distanceToBeMergedMultiplierSlider, "Distance To Be Merged Multiplier", "0.0", (observable, oldValue, newValue) -> {
            // Set the distance to be merged multiplier to the new value
            DISTANCE_TO_BE_MERGED_MULTIPLIER = newValue.doubleValue();
        });
    }

    // The getControlCenter method is used to get a HBox that contains the control center
    private HBox getControlCenter() {
        // Create a HBox to hold the whole control center
        final HBox controlCenter = new HBox();

        // Load the Montserrat font
        final Font font = FontUtils.getFont("Montserrat-Medium", 14);

        // Add an image to the left of the controlCenter HBox (icons/black-hole.png in the resources' folder)
        final ImageView simulationStatusIcon = new ImageView(new Image("images/pause.png"));
        // Set the size of the image to 30x30
        simulationStatusIcon.setFitHeight(font.getSize() * 1.9);
        simulationStatusIcon.setFitWidth(font.getSize() * 1.9);
        // Add the actual image to the controlCenter HBox
        controlCenter.getChildren().add(simulationStatusIcon);

        // Create a start button to start the simulation
        final Button startButton = new Button("Start");
        // Set the action of the button when it is clicked
        startButton.setOnAction(event -> {
            // Check if the simulation is already running
            if (simulatorStackPane.isStarted() || gravitationalWavesStackPane.isStarted()) return;
            // Start the animation timer for the simulation and the gravitational waves
            gravitationalWavesStackPane.getAnimationTimer().start();
            simulatorStackPane.getAnimationTimer().start();
            // Change the text of the start button to resume
            startButton.setText("Resume");
            // Change the image to a play icon
            simulationStatusIcon.setImage(new Image("images/play.png"));
        });
        // Set the font of the button
        startButton.setFont(font);
        // Make the button use the background different color hex color with the label color text
        startButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + ";");

        // Create a pause button to pause the simulation
        final Button pauseButton = new Button("Pause");
        // Set the action of the button when it is clicked
        pauseButton.setOnAction(event -> {
            if (!simulatorStackPane.isStarted() || !gravitationalWavesStackPane.isStarted()) return;
            // Stop the animation timer for the simulation and the gravitational waves
            simulatorStackPane.getAnimationTimer().stop();
            gravitationalWavesStackPane.getAnimationTimer().stop();
            // Change the text of the start button to resume
            startButton.setText("Resume");
            // Change the image to a pause icon
            simulationStatusIcon.setImage(new Image("images/pause.png"));
        });
        // Set the font of the button
        pauseButton.setFont(font);
        // Make the button use the background different color hex color with the label color text
        pauseButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + ";");

        // Create a reset button to reset the simulation
        final Button resetButton = new Button("Reset");
        // Set the action of the button when it is clicked
        resetButton.setOnAction(event -> {
            // Change the text of the start button to start
            startButton.setText("Start");
            // Stop the animation timer for the simulation and the gravitational waves
            simulatorStackPane.getAnimationTimer().stop();
            gravitationalWavesStackPane.getAnimationTimer().stop();
            // Reset the black holes
            BLACK_HOLE_1.reset(
                    SIMULATION_WIDTH / 2 - DEFAULT_POSITION_DIFFERENCE,
                    SIMULATION_HEIGHT / 2,
                    DEFAULT_BLACK_HOLE_VELOCITY,
                    DEFAULT_BLACK_HOLE_VELOCITY,
                    this.blackHole1Mass,
                    this.blackHole1Radius
            );
            BLACK_HOLE_2.reset(
                    SIMULATION_WIDTH / 2 + DEFAULT_POSITION_DIFFERENCE,
                    SIMULATION_HEIGHT / 2,
                    -DEFAULT_BLACK_HOLE_VELOCITY,
                    -DEFAULT_BLACK_HOLE_VELOCITY,
                    this.blackHole2Mass,
                    this.blackHole2Radius
            );
            // Reset the canvas
            simulatorStackPane.defaultCanvas();
            // Reset the gravitational waves
            gravitationalWavesStackPane.reset();
            // Reset the time elapsed
            TIME_ELAPSED = 0;
            // Set the maximumFrequencyLabel to be not visible anymore
            simulatorStackPane.highestRecordedFrequency.setVisible(false);
            // Change the image to a pause icon
            simulationStatusIcon.setImage(new Image("images/pause.png"));
        });
        // Set the font of the button
        resetButton.setFont(font);
        // Make the button use the background different color hex color with the label color text
        resetButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + ";");

        // Create a mute button to mute the sound
        final Button muteButton = new Button();
        // Create a speaker icon (icons/speaker.png in the resources' folder)
        final ImageView speakerIcon = new ImageView(new Image("images/speaker.png"));
        // Create a mute icon (icons/mute.png in the resources' folder)
        final ImageView muteIcon = new ImageView(new Image("images/mute.png"));
        // Set the size of the speaker icon to the size of a police that has a size of 20
        speakerIcon.setFitHeight(font.getSize() * 1.3);
        speakerIcon.setFitWidth(font.getSize() * 1.3);
        // Set the size of the mute icon to the size of a police that has a size of 20
        muteIcon.setFitHeight(font.getSize() * 1.3);
        muteIcon.setFitWidth(font.getSize() * 1.3);
        // Add the mute icon to the mute button
        muteButton.setGraphic(speakerIcon);
        // Set the action of the button when it is clicked
        muteButton.setOnAction(event -> {
            // If the sound is not muted
            if (!gravitationalWavesStackPane.isMuted()) {
                // Change the image to an unmute icon
                muteButton.setGraphic(muteIcon);
                // Mute the sound
                gravitationalWavesStackPane.setMuted(true);
            } else {
                // Change the image to a mute icon
                muteButton.setGraphic(speakerIcon);
                // Unmute the sound
                gravitationalWavesStackPane.setMuted(false);
            }
        });
        // Set the font of the button
        muteButton.setFont(font);
        // Make the button use the background different color hex color with the label color text
        muteButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + ";");

        // Create an info button to show the info with the info icon
        final Button infoButton = new Button();
        // Create an info icon (icons/info.png in the resources' folder)
        final ImageView infoIcon = new ImageView(new Image("images/info.png"));
        // Set the size of the info icon to the size of a police that has a size of 20
        infoIcon.setFitHeight(font.getSize() * 1.3);
        infoIcon.setFitWidth(font.getSize() * 1.3);
        // Add the info icon to the info button
        infoButton.setGraphic(infoIcon);
        // Set the action of the button when it is clicked
        infoButton.setOnAction(event -> {
            // Create a main title label
            final Label titleLabel = new Label("Information");
            // Set the font of the title label
            titleLabel.setFont(FontUtils.getFont("Montserrat-Bold", LABEL_FONT_SIZE));
            // Set the color of the title label to the label color
            titleLabel.setTextFill(LABEL_COLOR);

            // Create a layout for the title and the information
            final VBox titleLayout = new VBox(10, titleLabel);
            // Align the title and the information to the center
            titleLayout.setAlignment(Pos.CENTER);
            // Add the logo using a copy of the already existing one to avoid any issues
            titleLayout.getChildren().add(new ImageView(this.copyImage(this.logoLrIndustries.getImage())));
            // Add the information from the Markdown file to the layout
            titleLayout.getChildren().addAll(this.information);

            // Create a layout for the rectangle and the title layout
            final BorderPane popupLayout = new BorderPane();
            // Set the center of the layout to the title layout
            popupLayout.setCenter(titleLayout);
            // Set the padding of the layout to 20
            popupLayout.setPadding(new Insets(20));

            // Create a layout for the popup content
            final VBox popupContentLayout = new VBox();
            // Align the popup content to the center
            popupContentLayout.setAlignment(Pos.CENTER);
            // Set the background of the popup content to #101010
            popupContentLayout.setBackground(new Background(new BackgroundFill(BACKGROUND_DIFFERENT_COLOR, null, null)));
            // Add the popup layout to the popup content layout
            popupContentLayout.getChildren().addAll(popupLayout);

            // Create a popup a nd set its content to the layout
            final Popup infoPopup = new Popup();
            // Set the auto hide to true, so the popup will hide when the user clicks outside it
            infoPopup.setAutoHide(true);
            // Set the content of the popup to the layout
            infoPopup.getContent().add(popupContentLayout);

            // Create a point that is in the center of the stage
            final Point2D anchorPoint = popupContentLayout.localToScreen(popupLayout.getWidth() / 2, popupLayout.getHeight());
            // Set the anchor location of the popup to the top left of the stage
            infoPopup.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_LEFT);
            // Show the popup when the button is clicked in the center of the stage
            infoPopup.show(this.primaryStage, anchorPoint.getX(), anchorPoint.getY());
        });
        // Set the font of the button
        infoButton.setFont(font);
        // Make the button use the background different color hex color with the label color text
        infoButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + ";");

        // Set the spacing between the elements of the HBox to 5
        controlCenter.setSpacing(5);

        // Add the buttons to the controlCenter HBox
        controlCenter.getChildren().addAll(startButton, pauseButton, resetButton, muteButton, infoButton);

        // Set the controlCenter HBox to the center of the stackPane
        controlCenter.setAlignment(Pos.BOTTOM_CENTER);

        // Set a bottom margin to the controlCenter HBox
        StackPane.setMargin(controlCenter, new Insets(0, 0, 10, 0));

        // Return the controlCenter HBox
        return controlCenter;
    }

    // The getCloseButton method returns a button that closes the application
    private Button getCloseButton() {
        // Create a button with the text "Close"
        final Button closeButton = new Button("Close");
        // Set the font of the button to Montserrat-Bold with a size of 16
        closeButton.setFont(FontUtils.getFont("Montserrat-Bold", LABEL_FONT_SIZE_SMALL));
        // Set the background of the button to the background different color
        closeButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + "; -fx-opacity: 0.0;");
        // Set the action of the button when the mouse enters it
        closeButton.setOnMouseEntered(event -> {
            // Set the opacity of the button to 1.0
            closeButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + "; -fx-opacity: 1.0;");
        });
        // Set the action of the button when the mouse exits it
        closeButton.setOnMouseExited(event -> {
            // Set the opacity of the button to 0.0
            closeButton.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + "; -fx-opacity: 0.0;");
        });
        // Set the action of the button when it is clicked
        closeButton.setOnAction(event -> {
            // Close the application
            this.primaryStage.close();
        });
        // Set a margin to the button
        StackPane.setMargin(closeButton, new Insets(10));
        // Set the alignment of the button to the top right
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        // Return the button
        return closeButton;
    }

    // The getSliderBorderPane method is used to get a BorderPane that contains a slider
    private BorderPane getSliderBorderPane(final Slider slider, final String name, final String format, final ChangeListener<Number> changeListener) {
        // Create a BorderPane to hold the slider and the label
        final BorderPane borderPane = new BorderPane();
        // Set the padding of the BorderPane to 20
        borderPane.setPadding(new Insets(0, 20, 20, 20));

        // Create a VBox to hold the label and the slider
        final VBox vBoxLabelSlider = new VBox(10);
        // Set the size of the VBox to 1.4 times the width and 1/100 times the height
        vBoxLabelSlider.setMinSize(this.WIDTH / 1.4, this.HEIGHT / 100);
        // Set the padding of top, right, bottom and left to 10
        vBoxLabelSlider.setPadding(new Insets(10));

        // Create a label to display the name of the slider
        final Label nameLabel = new Label(name);
        // Set the text color of the label to the label color
        nameLabel.setTextFill(LABEL_COLOR);
        // Set the font of the label to the Montserrat font in size 14
        nameLabel.setFont(FontUtils.getFont("Montserrat-Medium", 14));

        // Create a text field to display the value of the slider
        TextField valueTextField = new TextField(Double.toString(slider.getValue()));
        // Set the font of the text field to the Montserrat font in size 12
        valueTextField.setFont(FontUtils.getFont("Montserrat-Medium", 12));
        // Set the background of the text field to the background different color and the text color to the label color, and make the text be centered
        valueTextField.setStyle("-fx-background-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-text-fill: " + LABEL_COLOR_HEX + "; -fx-alignment: center;");
        // Set the width of the text field to 1/10 times the width
        valueTextField.setPrefWidth(WIDTH / 6);

        // Add the name label and the slider to the VBox
        vBoxLabelSlider.getChildren().addAll(nameLabel, slider);

        // Add the VBox and the value label to the BorderPane
        borderPane.setLeft(vBoxLabelSlider);
        borderPane.setRight(valueTextField);

        // Bind the text property of the text field to the value property of the slider
        valueTextField.textProperty().bindBidirectional(slider.valueProperty(), new java.text.DecimalFormat(format));

        // Add a listener to the slider to update the time step of the simulator
        slider.valueProperty().addListener(changeListener);

        // Return the borderPane of the slider
        return borderPane;
    }

    // The getSlider method is used to get a slider with a custom min, max, and value
    private Slider getSlider(final double min, final double max, final double value, final double increment) {
        // Create a slider with default min, max, and value
        final Slider slider = new Slider(min, max, value);
        // Set the block increment of the slider
        slider.setBlockIncrement(increment);
        // Set the color of the slider to the BACKGROUND_DIFFERENT_COLOR color and remove the border
        slider.setStyle("-fx-control-inner-background: " + BACKGROUND_DIFFERENT_COLOR_HEX + "; -fx-background-radius: 0; -fx-border-radius: 0; -fx-border-insets: 0; -fx-border-width: 0; -fx-border-color: " + BACKGROUND_DIFFERENT_COLOR_HEX + ";");
        // Return the slider
        return slider;
    }

    // The getLabelsFromMarkdownFile method is used to get a list of labels from a Markdown file
    // PS: Real markdown use **content to be bold** for bold, but we decided to use # at the start of the line for bold, cause why not
    private Collection<Label> getLabelsFromMarkdownFile(final String markdownFileName) {
        // Create a list to hold the labels
        final List<Label> labels = new ArrayList<>();

        // Create a new InputStream to read the Markdown file
        final InputStream markdownFile = getClass().getResourceAsStream("/" + markdownFileName);
        // Create a new BufferedReader to read the lines of the Markdown file
        final BufferedReader reader = new BufferedReader(new InputStreamReader(markdownFile, StandardCharsets.UTF_8));

        // Parse the Markdown file and create a list of lines
        final List<String> lines = new ArrayList<>();
        // Try to read the lines of the Markdown file
        try {
            // Create a line variable
            String line;
            // Iterate through the lines of the Markdown file
            while ((line = reader.readLine()) != null) {
                // Add the line to the list of lines
                lines.add(line);
            }
            // Close the reader
            reader.close();
        } catch (IOException e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }

        // Create a Label for each line and add it to the list of labels
        for (final String markdownLine : lines) {
            // Parse the Markdown line to remove any Markdown syntax
            final String formattedLine = parseMarkdownLine(markdownLine);

            // Create a Label to display the formatted text
            final Label label = new Label(formattedLine);
            // Set the label to wrap text
            label.setWrapText(true);
            // Set the text color of the label to the label color
            label.setTextFill(LABEL_COLOR);

            // Set the font size based on the Markdown syntax used in the line
            if (!markdownLine.equals("") && markdownLine.charAt(0) == '#') {
                // Set the font of the label to Montserrat-Bold in size 16
                label.setFont(FontUtils.getFont("Montserrat-Bold", LABEL_FONT_SIZE_SMALL));
            } else {
                // Set the font of the label to Montserrat-Medium in size 16
                label.setFont(FontUtils.getFont("Montserrat-Medium", LABEL_FONT_SIZE_SMALL));
            }

            // Check if the line is a link
            if (formattedLine.startsWith("<a href=")) {
                // Set the action of the label when it is clicked
                label.setOnMouseClicked(event1 -> {
                    // Try to open the link in the default browser
                    try {
                        // Only get the link from the formatted line by removing everything that is not an HTML tag
                        // Don't question this code, it works, and it's all that matters
                        final String link = formattedLine.replace(formattedLine.replaceAll("\\<[^>]*>", ""), "").replace("<a href=", "").replace("</a>", "").replace(">", "").replace("\"", "");
                        // Open the link in the default browser
                        Desktop.getDesktop().browse(new URI(link));
                    } catch (IOException | URISyntaxException e) {
                        // Print the stack trace if an error occurs
                        e.printStackTrace();
                    }
                });
                // Remove the HTML tag from the label's text
                label.setText(label.getText().replaceAll("\\<[^>]*>", ""));
            }

            // Add the Label to the list of labels
            labels.add(label);
        }

        // Return the list of labels
        return labels;
    }

    // The parseMarkdownLine method is used to remove any Markdown syntax from a line of text
    private String parseMarkdownLine(final String markdownLine) {
        // Replace any Markdown syntax with appropriate formatting
        // Again, don't question this code, it works, and it's all that matters
        return markdownLine
                .replaceAll("^#+\\s+(.*)$", "$1") // Headers
                .replaceAll("\\*\\*(.*?)\\*\\*", "$1") // Bold
                .replaceAll("__(.*?)__", "$1") // Underline
                .replaceAll("\\*(.*?)\\*", "$1") // Italic
                .replaceAll("_(.*?)_", "$1") // Crossed out
                .replaceAll("`(.+?)`", "$1") // Code
                .replaceAll("\\[([^\\[]+)\\]\\(([^\\)]+)\\)", "<a href=\"$2\">$1</a>"); // Links
    }

    // The copyImage method is used to copy an already existing image in a variable to a new one
    private Image copyImage(final Image image) {
        // Create a new WritableImage with the same width and height as the image we are copying
        final WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        // Create a new PixelReader to read the pixels of the image
        final PixelReader pixelReader = image.getPixelReader();
        // Create a new PixelWriter to write the pixels of the new image
        final PixelWriter pixelWriter = writableImage.getPixelWriter();
        // Iterate through the pixels of the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Write the pixel from the image we are copying to the new image
                pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
            }
        }
        // Return the new image
        return writableImage;
    }
}
