package platform.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.enabled = 1 ORDER BY u.username ASC")
    List<User> findAllEnabled();

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE ?1% ORDER BY u.username ASC")
    List<User> findAllByLetter(String letter);

    @Query(value="SELECT * FROM users WHERE LOWER(username) ~ '^[0-9]' ORDER BY username ASC", nativeQuery = true)
    List<User> findAllBySpecSymbol();


}
