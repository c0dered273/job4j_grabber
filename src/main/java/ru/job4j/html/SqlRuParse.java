package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class SqlRuParse {
    public static void main(String[] args) {
        parsePages(5);
    }

    public static void printElements(Elements elements) {
        if (elements != null) {
            for (Element el : elements) {
                if (el.childrenSize() == 0) {
                    System.out.println("Date: " + el.text());
                    System.out.println();
                    continue;
                }
                System.out.println("Text: " + el.child(0).text());
                System.out.println("href: " + el.child(0).attr("href"));
            }
        } else {
            System.out.println("No such elements found");
        }
    }

    public static Elements parsePage(String url, String query) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc != null ? doc.select(query) : null;
    }

    public static void parsePages(int count) {
        String url = "https://www.sql.ru/forum/job-offers";
        String query = ".postslisttopic, td[class=\"altCol\"][style=\"text-align:center\"]";
        for (int i = 1; i <= count; i++) {
            String pageUrl = url + "/" + i;
            Elements el = parsePage(pageUrl, query);
            printElements(el);
        }
    }
}