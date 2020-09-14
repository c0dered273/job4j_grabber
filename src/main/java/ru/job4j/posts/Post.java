package ru.job4j.posts;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Post {
    private String href;
    private String desc;
    private LocalDate date;

    public Post(String href, String desc, LocalDate date) {
        this.href = href;
        this.desc = desc;
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        sj.add("href: " + href);
        sj.add("Description: " + desc);
        sj.add("Date: " + date);
        return sj.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(href, post.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href);
    }
}
