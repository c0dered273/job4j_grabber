package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements rowTopics = doc.select(".postslisttopic, td[class=\"altCol\"][style=\"text-align:center\"]");
        for (Element el : rowTopics) {
            if (el.childrenSize() == 0) {
                System.out.println("Date: " + el.text());
                continue;
            }
            System.out.println("Text: " + el.child(0).text());
            System.out.println("href: " + el.child(0).attr("href"));
        }
    }
}