package ru.job4j;

import ru.job4j.html.SqlRuParse;
import ru.job4j.posts.Post;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        String url = "https://www.sql.ru/forum/job-offers";
        String topicQuery = "tr";
        String descQuery = ".msgBody";
        SqlRuParse sqlRuParse = new SqlRuParse(url, topicQuery, descQuery);
        List<Post> result = sqlRuParse.parse(1);
        result.forEach(System.out::println);
    }
}
