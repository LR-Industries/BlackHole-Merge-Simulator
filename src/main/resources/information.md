This application is a Black-Hole Merge Simulator made in Java 17 using JavaFX 20.
It is made with <3 by Lucie Lebrun and Louis Ravignot Dos Santos on the behalf of LR Industries.
The simulation was made for an ES-Physique project with Mr. Yoala (Called Abdelmalek in the "milieu")

The black holes' movement is computed using Newton's laws of gravitation (THE GOAT), Verlet algorithm and the Euler Method.
The gravitational waves are computed using the formula of the Schwarzschild metric.
(and also the famous Pythagorean Theorem, a bit of trigonometry and circle equations)
[If you want to know more about how is everything working, you can check the source code on GitHub by clicking anywhere on this line.](https://github.com/LR-Industries/BlackHole-Merge-Simulator)

To fullscreen or not to fullscreen, press F11. To close the application, press Alt+F4, or put your mouse into the top right of the screen to make a close button appear.
The simulation is paused by default, press the play button to start it.
The stop, reset, mute and info buttons are self-explanatory (I hope cause you clicked on it to get here).
Note that every shown value is in SI units (meters, kilograms, seconds, etc.), and that every label next to the slider that is showing the value can be edited.
The speed of the simulation is adjustable using the Time Step slider, it works as a multiplier to the actual time at which the simulation should be running.
In simple words, it allows to slow down or speed up the simulation (note that a lower value make it more precise but a higher value make it a lot less precise).
For the radius of the black holes, you can use the corresponding slider or type a value in the text field.
The mass of the black holes is also adjustable using the slider or the text field.
The distance to be merged multiplier is a value that will be multiplied by the sum of the black holes' radius to get the distance at which the black holes will merge.
It is pretty useful to change it if you want to see the black holes merge at a specific distance or not merge at all.

# © 2023 LR Industries - Lucie Lebrun - Louis Ravignot Dos Santos
