package scarpet_canvas;

import carpet.script.value.Value;
import net.minecraft.nbt.Tag;

public class CanvasValue extends Value {
    byte[] img = new byte[128*128];

    public CanvasValue() {
        super();
    }


    @Override
    public String getTypeString() {
        System.out.println("got type string");
        return "canvas";
    }

    @Override
    public Tag toTag(boolean b) {
        return null;
    }

    @Override
    public String getString() {
        return "canvas";
    }

    @Override
    public boolean getBoolean() {
        return true;
    }


    public void setPixel(int x, int y, byte col) {
        if(x >= 0 && x < 128 && y >= 0 && y < 128) {
            img[y*128 + x] = col;
        }
    }

    public byte getPixel(int x, int y) {
        if(x >= 0 && x < 128 && y >= 0 && y < 128) {
            return img[y*128 + x];
        } else {
            return 0;
        }
    }
}