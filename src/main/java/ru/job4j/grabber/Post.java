package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Post {
    private long id;
    private String name;
    private String desc;
    private String link;
    private LocalDateTime date;

    public Post(String name, String desc, String link, LocalDateTime date) {
        this.name = name;
        this.desc = desc;
        this.link = link;
        this.date = date;
    }

    public Post(long id, String name, String desc, String link, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.link = link;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
        sj.add("Id: " + id);
        sj.add("Name: " + name);
        sj.add("Description: " + desc);
        sj.add("Link: " + link);
        sj.add("Date: " + date);
        return sj.toString();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                link.equals(post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}