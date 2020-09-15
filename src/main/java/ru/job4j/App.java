package ru.job4j;

import ru.job4j.grabber.SqlRuParse;
import ru.job4j.grabber.Post;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        String url = "https://www.sql.ru/forum/job-offers";
        SqlRuParse sqlRuParse = new SqlRuParse();
        List<Post> result = sqlRuParse.list(url);
        result.forEach(System.out::println);
    }
}
