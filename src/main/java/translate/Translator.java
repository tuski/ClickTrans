package translate;

import java.util.HashMap;

public class Translator {
    public static HashMap<String, String> threeDigitLang = new HashMap<String, String>();
    public static HashMap<String, String> twoDigitLang = new HashMap<String, String>();

    public Translator() {
        initComboBox();
    }

    private void initComboBox() {
        threeDigitLang.put("Japanese", "jpn");
        threeDigitLang.put("English", "eng");
        threeDigitLang.put("Arabic", "ara");
        threeDigitLang.put("Chinese", "chs");
        threeDigitLang.put("French", "fre");
        threeDigitLang.put("German", "ger");
        threeDigitLang.put("korean", "kor");
        threeDigitLang.put("Russian", "rus");
        threeDigitLang.put("Spanish", "spa");
        threeDigitLang.put("Swedish ", "swe");
        threeDigitLang.put("Turkish  ", "tur");

        twoDigitLang.put("Japanese", "ja");
        twoDigitLang.put("English", "en");
        twoDigitLang.put("Arabic", "ar");
        twoDigitLang.put("Chinese", "zh");
        twoDigitLang.put("French", "fr");
        twoDigitLang.put("German", "de");
        twoDigitLang.put("korean", "ko");
        twoDigitLang.put("Russian", "ru");
        twoDigitLang.put("Spanish", "es");
        twoDigitLang.put("Swedish", "sv");
        twoDigitLang.put("Turkish", "tr");
    }
}
