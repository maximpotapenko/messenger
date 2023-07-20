package messanger.user.repository;

import messanger.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsUserEntityByUsername(String username);

    /**
     * TO BE IMPROVED
     */
    @Query(value ="SELECT *, LENGTH(username) - LENGTH(:username) AS difference FROM users" +
            " WHERE username LIKE %:username% ORDER BY difference OFFSET :offset LIMIT :limit", nativeQuery = true)
    List<User> findClosestUsersByUsernameLike(String username, int offset, int limit);
}
