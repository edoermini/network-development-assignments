package com.bankaccount.generator.namesgen;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Edoardo Ermini
 */
public class NameGenerator {
    private static final String BASE_URL = "https://www.fakenamegenerator.com/";

    public String generate(Country country) {
        Document doc    = null;
        String code     = getCountryCode(country);
        String endpoint = "gen-random-" + code + "-" + code +".php";

        try {
            doc = Jsoup.connect(BASE_URL + endpoint).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert doc != null;
        Element details = doc.getElementById("details");
        Element address = details.getElementsByClass("address").first();
        Element nameElement = address.getElementsByTag("h3").first();

        return nameElement.text();
    }

    private String getCountryCode(Country country) {
        switch (country) {
            case Italy :        return "it";
            case France :       return "fr";
            case Germany :      return "gr";
            case UnitedStates:  return "us";
        }

        return null;
    }
}
