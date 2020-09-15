package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;


public class SqlRuParse implements Parse {

    @Override
    public List<SqlRuPost> list(String link) {
        List<SqlRuPost> rsl = new ArrayList<>();
        String topicQuery = ".postslisttopic";
        Elements elements = getElements(link, topicQuery);
        if (elements != null) {
            for (Element el : elements) {
                String href = el.child(0).attr("href");
                SqlRuPost newPost = detail(href);
                if (newPost != null) {
                    rsl.add(newPost);
                }
            }
        }
        return rsl;
    }

    @Override
    public SqlRuPost detail(String link) {
        SqlRuPost rsl = null;
        String detailsQuery = ".msgTable";
        Elements details = getElements(link, detailsQuery);
        if (details != null && details.size() > 0) {
            String name = details.first().getElementsByClass("messageHeader").textNodes().get(0).text();
            String desc = details.first().getElementsByClass("msgBody").get(1).text();
            String date = details.first().getElementsByClass("msgFooter").textNodes().get(0).text();
            date = date.substring(0, date.lastIndexOf("["));
            rsl = new SqlRuPost(link, name, desc, parseDate(date));
        }
        return rsl;
    }

    private Elements getElements(String url, String query) {
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
}