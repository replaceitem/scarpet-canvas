package scarpet_canvas;

import java.awt.*;

import static net.minecraft.block.MapColor.COLORS;

public class MapColor {
    static int[] shades = new int[]{180,220,255,135};

    public static int getShadedId(int baseId, int shade) {
        if(shade < 4 && shade >= 0)
            return baseId*4+shade;
        else
            return baseId*4;
    }

    public static Color applyShade(Color color, int shade) {
        int r = color.getRed()*shades[shade]/255;
        int g = color.getGreen()*shades[shade]/255;
        int b = color.getBlue()*shades[shade]/255;
        return new Color(r, g, b);
    }

    public static int getFromRGB(int red, int green, int blue) {
        //find closest matching color

        double minDistance = Double.MAX_VALUE;

        int bestBaseId = 0;
        int bestShade = 0;


        //start with second color, since 1st is transparent
        for(int i = 1; i <= 61; i++) {
            for(int j = 0; j < 4; j++) {
                Color c = applyShade(new Color(COLORS[i].color),j);

                double d = distance3D(red,green,blue,c.getRed(),c.getGreen(),c.getBlue());
                if(d < minDistance) {
                    minDistance = d;
                    bestBaseId = i;
                    bestShade = j;
                }
            }
        }
        return getShadedId(bestBaseId,bestShade);
    }


    private static double distance3D(int r1, int g1, int b1, int r2, int g2, int b2) {
        double r = (r1-r2);
        double g = (g1-g2);
        double b = (b1-b2);
        return Math.sqrt((r*r)+(g*g)+(b*b));
    }
}
