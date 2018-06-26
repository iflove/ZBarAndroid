package net.sourceforge.zbar;

public class BarcodeResult {
    private String contents;
    private BarcodeFormat barcodeFormat;

    public BarcodeResult() {
    }

    public BarcodeResult(String contents, BarcodeFormat barcodeFormat) {
        this.contents = contents;
        this.barcodeFormat = barcodeFormat;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setBarcodeFormat(BarcodeFormat format) {
        barcodeFormat = format;
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public String getContents() {
        return contents;
    }
}