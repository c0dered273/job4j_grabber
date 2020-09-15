package ru.job4j.grabber;

import java.util.List;

public interface Parse {
    List<SqlRuPost> list(String link);

    SqlRuPost detail(String link);
}