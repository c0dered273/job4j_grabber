package ru.job4j.posts;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Post {
    private String href;
    private String name;
    private String desc;
    private LocalDateTime date;

    public Post(String href, String name, String desc, LocalDateTime date) {
        this.href = href;
        this.name = name;
        this.desc = desc;
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        sj.add("href: " + href);
        sj.add("Name: " + name);
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
