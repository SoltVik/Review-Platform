package platform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.domain.User;
import platform.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


@Service
public class UserService {
    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void add(User user) {
        logger.info("Added user {}", user);
        userRepository.save(user);
    }

    public void save(User user) {
        logger.info("Updated user {}", user);
        userRepository.save(user);
    }

    public User findById(int idx) {
        User result = userRepository.findById(idx).orElse(null);
        return result;
    }


    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        return result;
    }

    public List<List<User>> getUserLists() {
        List<User> users = userRepository.findAll();
        List<List<User>> userList = new ArrayList<>();
        Set<String> letters = new TreeSet<>();
        for (User user : users) {
            String let = "" + user.getUsername().toLowerCase().charAt(0);
            if (let.matches("[0-9]")) {
                let = "#";
            }
            letters.add(let);
        }
        for (String letter : letters) {
            List<User> usersByLetter;
            if (letter.equals("#")) {
                usersByLetter = userRepository.findAllBySpecSymbol();
            } else {
                usersByLetter = userRepository.findAllByLetter(letter);
            }
            userList.add(usersByLetter);
        }
        return userList;
    }

    public List<List<User>> getUserListsByLetter(String letter) {
        letter = letter.toLowerCase();
        List<List<User>> userList = new ArrayList<>();
        List<User> usersByLetter;
        if (letter.equalsIgnoreCase("num")) {
            usersByLetter = userRepository.findAllBySpecSymbol();
        } else {
            usersByLetter = userRepository.findAllByLetter(letter);
        }

        if (!usersByLetter.isEmpty()) {
            userList.add(usersByLetter);
        }

        return userList;
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

}