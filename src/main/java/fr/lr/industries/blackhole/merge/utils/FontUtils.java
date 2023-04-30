package fr.lr.industries.blackhole.merge.utils;

// Import the required classes and packages
import javafx.scene.text.Font;

// The FontUtils class is used to manage everything related to the fonts
public class FontUtils {
    // The getFont method is used to get a Font object from a font file
    public static Font getFont(final String fontName, final double fontSize) {
        // Create a Font object from the font file and return it
        return Font.loadFont(FontUtils.class.getResourceAsStream("/fonts/" + fontName + ".ttf"), fontSize);
    }
}
