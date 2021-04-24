package scarpet_canvas;

import carpet.CarpetServer;
import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.*;

import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;

import java.util.Collections;

public class ScarpetFunctions {
    public static void apply(Expression expr) {
        expr.addLazyFunction("create_canvas", 0, (c, t, lv) -> {
            return (cc, tt) -> new CanvasValue();
        });

        expr.addLazyFunction("get_pixel", 3, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value xVal = lv.get(1).evalValue(c);
            Value yVal = lv.get(2).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'get_pixel' requires a canvas as the first argument");
            if(! (xVal instanceof NumericValue)) throw new InternalExpressionException("'get_pixel' requires a number as the second argument");
            if(! (yVal instanceof NumericValue)) throw new InternalExpressionException("'get_pixel' requires a number as the third argument");

            byte pix = ((CanvasValue) canvas).getPixel(((NumericValue) xVal).getInt(),((NumericValue) yVal).getInt());

            return (cc, tt) -> new NumericValue(pix);
        });

        expr.addLazyFunction("set_pixel", 4, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value xVal = lv.get(1).evalValue(c);
            Value yVal = lv.get(2).evalValue(c);
            Value cVal = lv.get(3).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'set_pixel' requires a canvas as the first argument");
            if(! (xVal instanceof NumericValue)) throw new InternalExpressionException("'set_pixel' requires a number as the second argument");
            if(! (yVal instanceof NumericValue)) throw new InternalExpressionException("'set_pixel' requires a number as the third argument");
            if(! (cVal instanceof NumericValue)) throw new InternalExpressionException("'set_pixel' requires a number as the fourth argument");

            byte col = (byte) ((NumericValue) cVal).getInt();

            ((CanvasValue) canvas).setPixel(((NumericValue) xVal).getInt(),((NumericValue) yVal).getInt(),col);

            return (cc, tt) -> canvas;
        });

        expr.addLazyFunction("fill_canvas", 2, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value color = lv.get(1).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'fill_canvas' requires a canvas as the first argument");
            if(! (color instanceof NumericValue)) throw new InternalExpressionException("'fill_canvas' requires a number as the second argument");

            CanvasValue cv = (CanvasValue)canvas;
            byte col = (byte) ((NumericValue) color).getInt();

            for(int i = 0; i < 16384; i++) {
                cv.img[i] = col;
            }


            return (cc, tt) -> canvas;
        });


        expr.addLazyFunction("rectangle", 6, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value color = lv.get(1).evalValue(c);
            Value xv = lv.get(2).evalValue(c);
            Value yv = lv.get(3).evalValue(c);
            Value wv = lv.get(4).evalValue(c);
            Value hv = lv.get(5).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'rectangle' requires a canvas as the first argument");
            if(! (color instanceof NumericValue)) throw new InternalExpressionException("'rectangle' requires a number as the second argument");
            if(! (xv instanceof NumericValue)) throw new InternalExpressionException("'rectangle' requires a number as the third argument");
            if(! (yv instanceof NumericValue)) throw new InternalExpressionException("'rectangle' requires a number as the fourth argument");
            if(! (wv instanceof NumericValue)) throw new InternalExpressionException("'rectangle' requires a number as the fifth argument");
            if(! (hv instanceof NumericValue)) throw new InternalExpressionException("'rectangle' requires a number as the sixth argument");

            CanvasValue cv = (CanvasValue)canvas;
            byte col = (byte) ((NumericValue) color).getInt();

            int x = ((NumericValue) xv).getInt();
            int y = ((NumericValue) yv).getInt();
            int w = ((NumericValue) wv).getInt();
            int h = ((NumericValue) hv).getInt();


            for(int i = x; i < x+w; i++) {
                for(int j = y; j < y+h; j++) {
                    cv.setPixel(i,j,col);
                }
            }


            return (cc, tt) -> canvas;
        });

        expr.addLazyFunction("ellipse", 6, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value color = lv.get(1).evalValue(c);
            Value xv = lv.get(2).evalValue(c);
            Value yv = lv.get(3).evalValue(c);
            Value wv = lv.get(4).evalValue(c);
            Value hv = lv.get(5).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'ellipse' requires a canvas as the first argument");
            if(! (color instanceof NumericValue)) throw new InternalExpressionException("'ellipse' requires a number as the second argument");
            if(! (xv instanceof NumericValue)) throw new InternalExpressionException("'ellipse' requires a number as the third argument");
            if(! (yv instanceof NumericValue)) throw new InternalExpressionException("'ellipse' requires a number as the fourth argument");
            if(! (wv instanceof NumericValue)) throw new InternalExpressionException("'ellipse' requires a number as the fifth argument");
            if(! (hv instanceof NumericValue)) throw new InternalExpressionException("'ellipse' requires a number as the sixth argument");

            CanvasValue cv = (CanvasValue)canvas;
            byte col = (byte) ((NumericValue) color).getInt();

            int x = ((NumericValue) xv).getInt();
            int y = ((NumericValue) yv).getInt();
            int w = ((NumericValue) wv).getInt()/2;
            int h = ((NumericValue) hv).getInt()/2;

            int w2 = w*w;
            int h2 = h*h;

            int w2h2 = w2*h2;

            for(int i = -w; i < w; i++) {
                for(int j = -h; j < h; j++) {
                    int d = (i*i*h2)+(j*j*w2);
                    if(d < w2h2) {
                        cv.setPixel(x+i,y+j,col);
                    }
                }
            }


            return (cc, tt) -> canvas;
        });

        expr.addLazyFunction("line", 6, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value color = lv.get(1).evalValue(c);
            Value x1v = lv.get(2).evalValue(c);
            Value y1v = lv.get(3).evalValue(c);
            Value x2v = lv.get(4).evalValue(c);
            Value y2v = lv.get(5).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'line' requires a canvas as the first argument");
            if(! (color instanceof NumericValue)) throw new InternalExpressionException("'line' requires a number as the second argument");
            if(! (x1v instanceof NumericValue)) throw new InternalExpressionException("'line' requires a number as the third argument");
            if(! (y1v instanceof NumericValue)) throw new InternalExpressionException("'line' requires a number as the fourth argument");
            if(! (x2v instanceof NumericValue)) throw new InternalExpressionException("'line' requires a number as the fifth argument");
            if(! (y2v instanceof NumericValue)) throw new InternalExpressionException("'line' requires a number as the sixth argument");

            CanvasValue cv = (CanvasValue)canvas;
            byte col = (byte) ((NumericValue) color).getInt();


            int x0 = ((NumericValue) x1v).getInt();
            int y0 = ((NumericValue) y1v).getInt();
            int x1 = ((NumericValue) x2v).getInt();
            int y1 = ((NumericValue) y2v).getInt();

            int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
            int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
            int err = (dx > dy ? dx : -dy) / 2;

            cv.setPixel(x0,y0,col);

            while (x0 != x1 || y0 != y1) {
                int e2 = err;
                if (e2 > -dx) { err -= dy; x0 += sx; }
                if (e2 <  dy) { err += dx; y0 += sy; }
                cv.setPixel(x0,y0,col);
            }





            return (cc, tt) -> canvas;
        });

        expr.addLazyFunction("draw_map", 2, (c, t, lv) -> {
            Value canvas = lv.get(0).evalValue(c);
            Value id = lv.get(1).evalValue(c);

            if(! (canvas instanceof CanvasValue)) throw new InternalExpressionException("'draw_map' requires a canvas as the first argument");
            if(! (id instanceof NumericValue)) throw new InternalExpressionException("'draw_map' requires a number as the second argument");

            CarpetServer.minecraft_server.getPlayerManager().sendToAll(new MapUpdateS2CPacket(((NumericValue) id).getInt(), (byte) 0,false,true,Collections.emptyList(),((CanvasValue) canvas).img,0,0,128,128));



            return LazyValue.TRUE;
        });

        expr.addLazyFunction("rgb_to_map", 3, (c, t, lv) -> {
            Value rv = lv.get(0).evalValue(c);
            Value gv = lv.get(1).evalValue(c);
            Value bv = lv.get(2).evalValue(c);

            final int colorId;

            if(rv instanceof NumericValue && gv instanceof NumericValue && bv instanceof NumericValue) {
                colorId = MapColor.getFromRGB(((NumericValue) rv).getInt(),((NumericValue) gv).getInt(),((NumericValue) bv).getInt());
            } else {
                throw new InternalExpressionException("'map_color' requires three numbers");
            }

            return (cc, tt) -> new NumericValue(colorId);
        });

        expr.addLazyFunction("block_color", -1, (c, t, lv) -> {
            if(lv.size() == 0) throw new InternalExpressionException("'block_color' requires at least one argument");

            Value blockValue = lv.get(0).evalValue(c);

            int shade = 2;
            if(lv.size() > 1) {
                Value shadeValue = lv.get(1).evalValue(c);
                shade = NumericValue.asNumber(shadeValue).getInt();
            }

            if(!(blockValue instanceof BlockValue)) return LazyValue.FALSE;

            int color = ((BlockValue) blockValue).getBlockState().getMaterial().getColor().id;

            int colorId = MapColor.getShadedId(color,shade);
            return (cc, tt) -> new NumericValue(colorId);
        });
    }
}
