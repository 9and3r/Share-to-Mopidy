package mopidy.to.share.and3r.sharetomopidy.utils;

import android.support.v7.graphics.Palette;

public class PaletteManager {


    public static Palette.Swatch getVibrantSwatch(Palette p){
        Palette.Swatch s = p.getVibrantSwatch();
        if (s != null){
            return s;
        }else{
            s = p.getLightVibrantSwatch();
            if (s != null){
                return s;
            }else{
                s = p.getMutedSwatch();
                if (s != null){
                    s = p.getLightMutedSwatch();
                    return s;
                }else{
                    return null;
                }
            }
        }
    }

    public static Palette.Swatch getVibrantDarkSwatch(Palette p){
        Palette.Swatch s = p.getDarkVibrantSwatch();
        if (s != null){
            return s;
        }else{
            s = p.getDarkMutedSwatch();
            if (s != null){
                return s;
            }else{
                s = p.getVibrantSwatch();
                if (s != null){
                    s = p.getMutedSwatch();
                    return s;
                }else{
                    return null;
                }
            }
        }
    }
}
