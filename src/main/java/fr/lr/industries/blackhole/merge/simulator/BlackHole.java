package fr.lr.industries.blackhole.merge.simulator;

// Import the required classes and packages
import fr.lr.industries.blackhole.merge.Simulator;
import javafx.scene.shape.Circle;
import static fr.lr.industries.blackhole.merge.Simulator.*;

// The BlackHole class is used to represent a black hole as an object
public class BlackHole {
    // The x and y variables are used to store the position of the black hole
    // The vx and vy variables are used to store the velocity of the black hole
    // The ax and ay variables are used to store the acceleration of the black hole
    // The mass variable is used to store the mass of the black hole
    // The radius variable is used to store the radius of the black hole
    private double x, y, vx, vy, ax, ay, mass, radius;

    // The enabled variable is used to store whether the black hole is enabled or not
    private boolean enabled;

    // The blackHoleCircle and eventHorizonCircle variables are used to store the circles that represent the black hole and its event horizon
    private final Circle blackHoleCircle;
    private final Circle eventHorizonCircle;

    // The BlackHole constructor is used to create a new black hole
    public BlackHole(final double x, final double y, final double vx, final double vy, final double mass, final double radius) {
        // Assign the parameters to the global variables
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = 0;
        this.ay = 0;
        this.mass = mass;
        this.radius = radius;
        this.enabled = true;
        this.blackHoleCircle = new Circle(this.x, this.y, this.radius);
        this.blackHoleCircle.setFill(BACKGROUND_COLOR);
        this.eventHorizonCircle = new Circle(this.x, this.y, this.getEventHorizonRadius());
        this.eventHorizonCircle.setFill(BLACK_HOLE_EVENT_HORIZON_COLOR);
    }

    // The getX method is used to get the x position of the black hole
    public double getX() {
        // Return the x position of the black hole
        return this.x;
    }

    // The getY method is used to get the y position of the black hole
    public double getY() {
        // Return the y position of the black hole
        return this.y;
    }

    // The getVx method is used to get the x velocity of the black hole
    public double getVx() {
        // Return the x velocity of the black hole
        return this.vx;
    }

    // The getVy method is used to get the y velocity of the black hole
    public double getVy() {
        // Return the y velocity of the black hole
        return this.vy;
    }

    // The getAx method is used to get the x acceleration of the black hole
    public double getAx() {
        // Return the x acceleration of the black hole
        return this.ax;
    }

    // The getAy method is used to get the y acceleration of the black hole
    public double getAy() {
        // Return the y acceleration of the black hole
        return this.ay;
    }

    // The getMass method is used to get the mass of the black hole
    public double getMass() {
        // Check if the black hole is enabled, if not return 0
        if (!this.isEnabled()) return 0;
        // Return the mass of the black hole
        return this.mass;
    }

    // The getRadius method is used to get the radius of the black hole
    public double getRadius() {
        // Return the radius of the black hole
        return this.radius;
    }

    // The getEventHorizonRadius method is used to get the event horizon radius of the black hole
    public double getEventHorizonRadius() {
        // Return the event horizon radius of the black hole
        return this.radius * 1.02;
    }

    // The isEnabled method is used to check whether the black hole is enabled or not
    public boolean isEnabled() {
        // Return whether the black hole is enabled or not
        return this.enabled;
    }

    // The getBlackHoleCircle method is used to get the circle for the black hole
    public Circle getBlackHoleCircle() {
        // Return the circle for the black hole
        return this.blackHoleCircle;
    }

    // The getEventHorizonCircle method is used to get the circle for the event horizon
    public Circle getEventHorizonCircle() {
        // Return the circle for the event horizon
        return this.eventHorizonCircle;
    }

    // The getDistance method is used to get the distance between the black hole and the point using the Pythagorean theorem
    public double getDistance(final double x, final double y) {
        // Return the distance between the black hole and the point using the Pythagorean theorem
        // Calculated using the formula: sqrt((x2 - x1)^2 + (y2 - y1)^2)
        // Where x1 and y1 are the coordinates of this black hole and x2 and y2 are the coordinates of the point
        // https://en.wikipedia.org/wiki/Pythagorean_theorem
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    // The getDistance method is used to get the distance between the black hole and the other black hole using the Pythagorean theorem
    public double getDistance(final BlackHole blackHole) {
        // Return the distance between the black hole and the other black hole using the Pythagorean theorem from the getDistance method
        return this.getDistance(blackHole.getX(), blackHole.getY());
    }

    // The isInEventHorizon method is used to check whether a point in space is in the event horizon of the black hole
    public boolean isInEventHorizon(final double x, final double y) {
        // Return whether the point in space is in the event horizon of the black hole
        // Calculated using the formula: (x - x0)^2 + (y - y0)^2 <= r^2
        // Where x0 and y0 are the coordinates of the black hole and r is the event horizon radius of the black hole
        // https://en.wikipedia.org/wiki/Circle#Equations
        return Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) <= Math.pow(this.getEventHorizonRadius(), 2);
    }

    // The getSchwarzschildRadius method is used to get the Schwarzschild radius of the black hole
    public double getSchwarzschildRadius() {
        // Return the Schwarzschild radius of the black hole
        // Calculated using the formula: 2 * G * M / c^2
        // Where G is the gravitational constant, M is the mass of the black hole and c is the speed of light
        // https://en.wikipedia.org/wiki/Schwarzschild_radius
        return 2 * G * this.mass / Math.pow(C, 2);
    }

    // The getForce method is used to get the force between the black hole and a point in space at a certain distance
    public double getForce(final double distance) {
        // Return the force between the black hole and a point in space at a certain distance
        // Calculated using the formula: G * M / r^2
        // Where G is the gravitational constant, M is the mass of the black hole and r is the distance between the black hole and the point in space
        // https://en.wikipedia.org/wiki/Newton%27s_law_of_universal_gravitation
        return G * this.mass / Math.pow(distance, 2);
    }

    // The getScaleFactorToOtherBlackHole method is used to get the scale factor between this black hole and the other black hole
    public double getScaleFactorToOtherBlackHole(final BlackHole blackHole) {
        // Get the distance between this black hole and the other black hole
        final double distanceTo = this.getDistance(blackHole);

        // Check if the distance between the two black holes is 0
        if (distanceTo == 0) {
            // If the distance between the two black holes is 0, return 1
            return 1.0;
        } else if (distanceTo < this.getEventHorizonRadius()) {
            // If the distance between the two black holes is less than the event horizon radius of this black hole, return 0
            return 1.0;
        } else if (distanceTo < blackHole.getEventHorizonRadius()) {
            // If the distance between the two black holes is less than the event horizon radius of the other black hole, return 0
            return 1.0;
        }

        // Calculate the scale factor between this black hole and the other black hole
        // Calculated using the formula: 1 + F / d
        // Where F is the gravitational force and d is the distance between the two black holes
        return 1.0 + this.getForce(distanceTo) / distanceTo;
    }

    // The getGravitationalWavesFrequency method is used to generate gravitational waves frequency from the two colliding black holes using the Schwarzschild metric
    public double getGravitationalWavesFrequency(final BlackHole blackHole) {
        // Calculate the distance between the two black holes
        final double distance = this.getDistance(blackHole);

        // Calculate the total mass of the two black holes
        // Calculated using the formula: m1 + m2
        // Where m1 is the mass of the black hole and m2 is the mass of the other black hole
        final double totalMass = this.mass + blackHole.getMass();

        // Calculate the reduced mass of the two black holes
        // Calculated using the formula: m1 * m2 / (m1 + m2)
        // Where m1 is the mass of the black hole and m2 is the mass of the other black hole
        final double reducedMass = this.mass * blackHole.getMass() / totalMass;

        // Return the frequency of the gravitational waves using the Schwarzschild metric
        // Calculated using the formula: 1 / (2 * pi) * sqrt(G * m / r^3) * (1 - 6 * G * mu / (c^2 * r) + 8 * pi * G * mu * R / (3 * c^2 * r))
        // Where G is the gravitational constant, m is the total mass of the two black holes, r is the distance between the two black holes, mu is the reduced mass of the two black holes, c is the speed of light and R is the Schwarzschild radius of the two black holes
        // https://en.wikipedia.org/wiki/Schwarzschild_metric
        return (1.0 / (2.0 * Math.PI)) * Math.sqrt(G * totalMass / Math.pow(distance, 3)) * (1.0 - 6.0 * G * reducedMass / (Math.pow(C, 2) * distance) + 8.0 * Math.PI * G * reducedMass * this.getSchwarzschildRadius() / (3.0 * Math.pow(C, 2) * distance));
    }

    // The setX method is used to set the x position of the black hole
    public void setX(final double x) {
        // Assign the parameter to the x position of the black hole
        this.x = x;
    }

    // The setY method is used to set the y position of the black hole
    public void setY(final double y) {
        // Assign the parameter to the y position of the black hole
        this.y = y;
    }

    // The setVx method is used to set the x velocity of the black hole
    public void setVx(final double vx) {
        // Assign the parameter to the x velocity of the black hole
        this.vx = vx;
    }

    // The setVy method is used to set the y velocity of the black hole
    public void setVy(final double vy) {
        // Assign the parameter to the y velocity of the black hole
        this.vy = vy;
    }

    // The setAx method is used to set the x acceleration of the black hole
    public void setAx(final double ax) {
        // Assign the parameter to the x acceleration of the black hole
        this.ax = ax;
    }

    // The setAy method is used to set the y acceleration of the black hole
    public void setAy(final double ay) {
        // Assign the parameter to the y acceleration of the black hole
        this.ay = ay;
    }

    // The setMass method is used to set the mass of the black hole
    public void setMass(final double mass) {
        // Assign the parameter to the mass of the black hole
        this.mass = mass;
    }

    // The setRadius method is used to set the radius of the black hole
    public void setRadius(final double radius) {
        // Assign the parameter to the radius of the black hole
        this.radius = radius;
    }

    // The setEnabled method is used to set whether the black hole is enabled or not
    public void setEnabled(final boolean enabled) {
        // Assign the parameter to whether the black hole is enabled or not
        this.enabled = enabled;
        // Set the visible property of the black hole circle to the enabled property of the black hole
        this.blackHoleCircle.setVisible(this.enabled);
        // Set the visible property of the event horizon circle to the enabled property of the black hole
        this.eventHorizonCircle.setVisible(this.enabled);
    }

    // The calculateAcceleration method is used to calculate the acceleration of the black hole
    public void calculateAcceleration(final BlackHole blackHole) {
        // If the black hole or the other one has no mass, then return
        if (this.mass == 0 || blackHole.getMass() == 0) return;

        // Get the distance between the black holes
        final double distance = this.getDistance(blackHole);

        // If the distance between the black holes is less or equal to the sum of the radii of the black holes, then the black holes are colliding
        if (distance * DISTANCE_TO_BE_MERGED_MULTIPLIER <= this.getRadius() * 1/2 + blackHole.getRadius() * 1/2) {
            // Calculate the new mass of the black hole
            this.mass += blackHole.getMass();
            // Calculate the new radius of the black hole
            this.radius = this.radius + blackHole.getRadius() * Math.pow(this.mass / (this.mass - blackHole.getMass()), 1.0 / 3.0);
            // Disable the other black hole
            blackHole.setEnabled(false);
            // Reset the scale factor of the black hole
            this.resetScaleFactor();
            // Reset the scale factor of the other black hole
            blackHole.resetScaleFactor();
            // Update the black hole circle
            this.updateCircles(false);
            // Update the other black hole circle
            blackHole.updateCircles(false);
            // Stop the animation timer of the simulation
            simulatorStackPane.getAnimationTimer().stop();
            // Stop the animation timer of the gravitational waves
            gravitationalWavesStackPane.getAnimationTimer().stop();
            // Return from the method
            return;
        }

        // Calculate the gravitational force between the particle and the black hole using Newton's law of universal gravitation that I also know by heart thanks to my so-called "physics" teacher named Mr. Eneman
        // Calculated using the formula: G * m1 * m2 / r^2
        // Where G is the gravitational constant, m1 is the mass of the black hole, m2 is the mass of the other black hole and r is the distance between the black hole and the other black hole
        // https://en.wikipedia.org/wiki/Newton%27s_laws_of_motion#Second
        final double gravitationalForce = G * this.mass * blackHole.getMass() / Math.pow(distance, 2);

        // Calculate the angle between the black hole and the other black hole using the arc-tangent function
        // Calculated using the formula: atan2(y2 - y1, x2 - x1)
        // Where x1 and y1 are the coordinates of this black hole and x2 and y2 are the coordinates of the other black hole
        // https://en.wikipedia.org/wiki/Atan2
        final double angle = Math.atan2(blackHole.getY() - this.y, blackHole.getX() - this.x);

        // Calculate the acceleration of the black hole using the gravitational force and the distance between the black hole and the particle
        // Calculated using the formula for the x's: G * cos(angle) / m
        // Calculated using the formula for the y's: G * sin(angle) / m
        // Where G is the gravitational force, angle is the angle between the black hole and the other black hole and m is the mass of the black hole
        // https://en.wikipedia.org/wiki/Newton%27s_laws_of_motion#Second
        this.ax = gravitationalForce * Math.cos(angle) / this.mass;
        this.ay = gravitationalForce * Math.sin(angle) / this.mass;

        // Calculate the acceleration of the other black hole using the gravitational force and the distance between the black hole and the particle
        // Calculated again using the formula for the x's: G * cos(angle) / m
        // Calculated again using the formula for the y's: G * sin(angle) / m
        // Where G is the gravitational force, angle is the angle between the black hole and the other black hole and m is the mass of the other black hole
        // https://en.wikipedia.org/wiki/Newton%27s_laws_of_motion#Second (Again, cause Newton is the GOAT)
        blackHole.setAx(-gravitationalForce * Math.cos(angle) / blackHole.getMass());
        blackHole.setAy(-gravitationalForce * Math.sin(angle) / blackHole.getMass());
    }

    // The updateVelocity method is used to update the velocity of the black hole using the Verlet algorithm
    public void updateVelocity() {
        // Update the velocity of the black hole using the acceleration of the black hole using the Verlet algorithm
        // Calculated using the formula: v = v0 + a * t
        // Where v is the velocity of the black hole, v0 is the initial velocity of the black hole, a is the acceleration of the black hole and t is the time step
        // https://en.wikipedia.org/wiki/Verlet_integration
        this.vx += this.ax * TIME_STEP;
        this.vy += this.ay * TIME_STEP;
    }

    // The updatePosition method is used to update the position of the black hole using the Euler method
    public void updatePosition() {
        // Update the position of the black hole using the velocity of the black hole using the Euler method
        // Calculated using the formula: x = x0 + v * t + 0.5 * a * t^2 (work also for the y's)
        // Where x is the position of the black hole, x0 is the initial position of the black hole, v is the velocity of the black hole, a is the acceleration of the black hole and t is the time step
        // https://en.wikipedia.org/wiki/Euler_method
        this.x += this.vx * TIME_STEP + 0.5 * this.ax * Math.pow(TIME_STEP, 2);
        this.y += this.vy * TIME_STEP + 0.5 * this.ay * Math.pow(TIME_STEP, 2);
    }

    // The updateCircles method is used to update the circles representing the black hole and the event horizon
    public void updateCircles(final boolean updateScaleFactor) {
        // Update x center, y center and radius of the black hole circle
        this.blackHoleCircle.setCenterX(this.x);
        this.blackHoleCircle.setCenterY(this.y);
        this.blackHoleCircle.setRadius(this.radius);

        // Update x center, y center and radius of the event horizon circle
        this.eventHorizonCircle.setCenterX(this.x);
        this.eventHorizonCircle.setCenterY(this.y);
        this.eventHorizonCircle.setRadius(this.getEventHorizonRadius());

        // If the updateScaleFactor parameter is true, update the scale factor of the black hole and the event horizon
        if (updateScaleFactor) {
            // Update the scale factor of the black hole and the event horizon
            this.updateScaleFactor();
        }
    }

    // The updateScaleFactor method is used to update the scale factor of the black hole and the event horizon
    public void updateScaleFactor() {
        // Get the scale factor between this black hole and the other black hole
        final double scaleFactor = this.getScaleFactorToOtherBlackHole(Simulator.otherBlackHole(this));
        // Update the scale factor of the black hole and the event horizon
        this.blackHoleCircle.setScaleX(scaleFactor);
        this.blackHoleCircle.setScaleY(scaleFactor);
        this.eventHorizonCircle.setScaleX(scaleFactor);
        this.eventHorizonCircle.setScaleY(scaleFactor);
    }

    // The update method is used to update the black hole and the other black hole using the Verlet algorithm
    public void update(final BlackHole blackHole) {
        // Calculate the acceleration of the current black hole
        this.calculateAcceleration(blackHole);

        // If the current black hole is disabled or the other black hole is disabled, then return
        if (!this.enabled || !blackHole.isEnabled()) {
            // I think this is obvious
            return;
        }

        // Update the velocity of the current black hole
        this.updateVelocity();
        // Update the position of the current black hole
        this.updatePosition();
        // Update the circles of the current black hole
        this.updateCircles(true);

        // Reset the acceleration of the current black hole
        this.ax = 0;
        this.ay = 0;

        // Update the velocity of the other black hole
        blackHole.updateVelocity();
        // Update the position of the other black hole
        blackHole.updatePosition();
        // Update the circles of the other black hole
        blackHole.updateCircles(true);

        // Reset the acceleration of the other black hole
        blackHole.setAx(0);
        blackHole.setAy(0);

        // Calculate the gravitational wave frequency and assign it to the gravitational wave frequency of the simulator
        // (Multiply by 1000 to convert from Hz to kHz and also have a better frequency to listen to)
        GRAVITATIONAL_WAVE_FREQUENCY = this.getGravitationalWavesFrequency(blackHole) * 1.0e4;
    }

    // The reset method is used to reset the black hole to the specified values
    public void reset(final double x, final double y, final double vx, final double vy, final double mass, final double radius) {
        // Reset the black hole to the specified values
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = 0;
        this.ay = 0;
        this.mass = mass;
        this.radius = radius;
        this.setEnabled(true);
        // Update the circles of the black hole to avoid any graphical glitches
        this.updateCircles(true);
    }

    // The resetScaleFactor method is used to reset the scale factor of the black hole and the event horizon
    public void resetScaleFactor() {
        // Reset the scale factor of the black hole and the event horizon
        this.blackHoleCircle.setScaleX(1.0);
        this.blackHoleCircle.setScaleY(1.0);
        this.eventHorizonCircle.setScaleX(1.0);
        this.eventHorizonCircle.setScaleY(1.0);
    }
}
