package entity;

import org.dizitart.no2.objects.Id;

import java.io.Serializable;

public class TranslatedText implements Serializable {
    public TranslatedText() {
    }

    private String fromText;

    private String toText;

    public TranslatedText(String fromText, String toText) {
        this.fromText = fromText;
        this.toText = toText;
    }

    public String getFromText() {
        return fromText;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public String getToText() {
        return toText;
    }

    public void setToText(String toText) {
        this.toText = toText;
    }
}
