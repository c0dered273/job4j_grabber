package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PsqlStore.class);
    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            logger.error("Connection to database error", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Post save(Post post) {
        String updateQuery = "INSERT INTO post(name, description, link, created)" +
                " VALUES(?, ?, ?, ?) RETURNING Id";
        try (PreparedStatement st = cnn.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, post.getName());
            st.setString(2, post.getDesc());
            st.setString(3, post.getLink());
            st.setTimestamp (4, Timestamp.valueOf(post.getDate()));
            int affectedRaws = st.executeUpdate();
            if (affectedRaws == 0) {
                throw new SQLException("No affected rows.");
            }
            ResultSet keys = st.getGeneratedKeys();
            if (keys.next()) {
                post.setId(keys.getLong(1));
            } else {
                throw new SQLException("No id obtained.");
            }
            logger.info("New post added to database");
            logger.info(post.toString());
        } catch (SQLException e) {
            logger.error("Insert new post statement error", e);
        }
        return post;
    }

    @Override
    public List<Post> getAll() {
        List<Post> rsl = new ArrayList<>();
        String selectQuery = "SELECT * FROM post";
        try (PreparedStatement st = cnn.prepareStatement(selectQuery)) {
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                rsl.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Select all posts error", e);
        }
        return rsl;
    }

    @Override
    public Post findById(long id) {
        Post rsl = null;
        String selectQuery = "SELECT * FROM post WHERE Post.id = ?";
        try (PreparedStatement st = cnn.prepareStatement(selectQuery)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                rsl = parseResultSet(resultSet);
            } else {
                logger.info("Item with id = \"{}\" not found", id);
            }
        } catch (SQLException e) {
            logger.error("Select by id error", e);
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    private Post parseResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String desc = rs.getString("description");
        String link = rs.getString("link");
        LocalDateTime date = rs.getTimestamp("created").toLocalDateTime();
        return new Post(id, name, desc, link, date);
    }
}