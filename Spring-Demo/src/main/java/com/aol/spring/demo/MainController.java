package com.aol.spring.demo;

import com.aol.spring.demo.models.Chat;
import com.aol.spring.demo.models.ChatsDao;
import com.aol.spring.demo.models.User;
import com.aol.spring.demo.models.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by ram on 26/02/16.
 */
@Controller
public class MainController {

    // Private fields

    @Autowired
    private UserDao _userDao;

    @Autowired
    private ChatsDao _chatDao;

    private static final Logger logger = LoggerFactory
            .getLogger(MainController.class);

    @RequestMapping("/")
    public ModelAndView index(@CookieValue(value = "name", defaultValue = "") String name,
                              @CookieValue(value = "email", defaultValue = "") String email) {
        if ("".equals(name) || "".equals(email)) {
            return loginPage();
        } else {
            return chatsPage();
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if ("name".equals(cookie.getName()) || "email".equals(cookie.getName())) {
                // clear cookie
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam String name,
                                   @RequestParam String email) {
        try {
            // create new user object
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setTimestamp(new Date().getTime());

            // save user in db (if new)
            if (_userDao.getByEmail(email) == null) {
                _userDao.save(user);
            }

            // save in cookie
            Cookie cookie = new Cookie("name", name);
            Cookie cookie1 = new Cookie("email", email);
            response.addCookie(cookie);
            response.addCookie(cookie1);
        } catch (Exception e) {
            logger.error("Exception in creating user: ", e.getStackTrace());
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/chats", method = RequestMethod.GET)
    public ModelAndView chatsPage() {
        return new ModelAndView("chats");
    }

    @ResponseBody
    @RequestMapping(value = "/get-all-chats", method = RequestMethod.GET)
    public List<Chat> getAllChats() {
        try {
            return _chatDao.getAll();
        } catch (Exception e) {
            logger.error("Exception in fetching chats: ", e.getStackTrace());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/get-all-users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        try {
            return _userDao.getAll();
        } catch (Exception e) {
            logger.error("Exception in fetching users: ", e.getStackTrace());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/post-chat", method = RequestMethod.POST)
    public ModelAndView postChat(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam String message,
                           @CookieValue(required = true) String name,
                           @CookieValue(required = true) String email) {
        try {
            // fetch user info
            User user = _userDao.getByEmail(email);

            if (user == null) {
                return logout(request, response);
            }

            // create new chat object
            Chat chat = new Chat();
            chat.setMessage(message);
            chat.setUser(user);
            chat.setTimestamp(new Date().getTime());

            // save chat in db
            _chatDao.save(chat);
        } catch (Exception e) {
            logger.error("Exception in saving chat: ", e.getStackTrace());
        }
        return null;
    }

}
