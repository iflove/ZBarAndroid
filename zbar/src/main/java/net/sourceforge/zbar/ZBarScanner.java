package net.sourceforge.zbar;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;

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

    public BarcodeResult getBarcodeResult(byte[] previewImgData, int width, int height, Rect cropRect) {
        if (previewImgData == null) {
            return null;
        }
        Image barcode = new Image(width, height, "Y800");
        barcode.setData(previewImgData);
        if (cropRect != null) {
            barcode.setCrop(cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
        }
        return this.getBarcodeResult(barcode);
    }

    public BarcodeResult getBarcodeResult(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            int picWidth = bitmap.getWidth();
            int picHeight = bitmap.getHeight();
            Image barcode = new Image(picWidth, picHeight, "RGB4");
            int[] pix = new int[picWidth * picHeight];
            bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
            barcode.setData(pix);
            return getBarcodeResult(barcode.convert("Y800"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BarcodeResult getBarcodeResult(Image y800Barcode) {
        if (imageScanner != null && imageScanner.scanImage(y800Barcode) != 0) {
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
