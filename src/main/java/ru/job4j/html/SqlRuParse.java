package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.posts.Post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;


public class SqlRuParse {
    private final String url;
    private final String topicQuery;
    private final String descQuery;

    public SqlRuParse(String url, String topicQuery, String descQuery) {
        this.url = url;
        this.topicQuery = topicQuery;
        this.descQuery = descQuery;
    }

    public List<Post> parse(int pages) {
        List<Post> rsl = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            String pageUrl = i == 1 ? url : url + "/" + i;
            Elements topics = parseUrl(pageUrl, topicQuery);
            if (topics != null) {
                List<Post> post = parseElements(topics);
                rsl.addAll(post);
            }
        }
        return rsl;
    }

    private Elements parseUrl(String url, String query) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc != null ? doc.select(query) : null;
    }

    private LocalDateTime parseDate(String date) {
        LocalDateTime rsl;
        Map<Long, String> monthsMap = new HashMap<>();
        monthsMap.put(1L, "янв");
        monthsMap.put(2L, "фев");
        monthsMap.put(3L, "мар");
        monthsMap.put(4L, "апр");
        monthsMap.put(5L, "май");
        monthsMap.put(6L, "июн");
        monthsMap.put(7L, "июл");
        monthsMap.put(8L, "авг");
        monthsMap.put(9L, "сен");
        monthsMap.put(10L, "окт");
        monthsMap.put(11L, "ноя");
        monthsMap.put(12L, "дек");
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                .appendPattern("d ")
                .appendText(ChronoField.MONTH_OF_YEAR, monthsMap)
                .appendPattern(" yy")
                .toFormatter();
        DateTimeFormatter timeFormatter = DateTimeFormatter
                .ofPattern("HH:mm");
        String[] spl = date.split(",");
        String dt = spl[0].trim();
        String tm = spl[1].trim();
        LocalDate localDate;
        switch (dt) {
            case "сегодня" -> localDate = LocalDate.now();
            case "вчера" -> localDate = LocalDate.now().minusDays(1L) ;
            default -> localDate = LocalDate.parse(dt, dateFormatter);
        }
        LocalTime localTime = LocalTime.parse(tm, timeFormatter);
        rsl = LocalDateTime.of(localDate, localTime);
        return rsl;
    }

    private List<Post> parseElements(Elements elements) {
        List<Post> rsl = new ArrayList<>();
        for (Element el : elements) {
            if (el.childrenSize() > 5) {
                Elements childEls = el.child(1).children();
                if (childEls.size() > 0) {
                    String href = childEls.first().attr("href");
                    String name = childEls.first().text();
                    String date = el.child(5).text();
                    String desc = "";
                    Elements descEls = parseUrl(href, descQuery);
                    if (descEls != null) {
                        desc = descEls.get(1).text();
                    }
                    rsl.add(new Post(href, name, desc, parseDate(date)));
                }
            }
        }
        return rsl;
    }
}