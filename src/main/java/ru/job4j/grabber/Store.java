package ru.job4j.grabber;

import java.util.List;

public interface Store {
    Post save(Post post);

    List<Post> getAll();

    Post findById(long id);
}