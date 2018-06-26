package me.zbar;

import android.graphics.Rect;
import android.text.TextUtils;

import net.sourceforge.zbar.BarcodeFormat;
import net.sourceforge.zbar.BarcodeResult;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public final class ZBarScanner {
    static {
        System.loadLibrary("iconv");
    }

    private final ImageScanner imageScanner;

    public ZBarScanner() {
        this.imageScanner = new ImageScanner();
        this.imageScanner.setConfig(0, Config.X_DENSITY, 3);
        this.imageScanner.setConfig(0, Config.Y_DENSITY, 3);

        this.imageScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
        for (BarcodeFormat format : BarcodeFormat.ALL_FORMATS) {
            this.imageScanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    public ZBarScanner(ImageScanner imageScanner) {
        this.imageScanner = imageScanner;
    }

    public BarcodeResult getBarcodeResult(byte[] imgData, int width, int height, Rect cropRect) {
        Image barcode = new Image(width, height, "Y800");
        barcode.setData(imgData);
        if (cropRect != null) {
            barcode.setCrop(cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
        }
        if (imageScanner == null) {
            return null;
        }
        if (imageScanner.scanImage(barcode) != 0) {
            SymbolSet syms = imageScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                    return new BarcodeResult(symData, BarcodeFormat.getFormatById(sym.getType()));
                }
            }
        }
        return null;
    }
}
