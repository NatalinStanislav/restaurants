package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.Role;
import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import com.natalinstanislav.restaurants.util.exception.ErrorType;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.TestUtil.userHttpBasic;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.ALL_VOTES_FROM_USER3;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/" + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/")
                .with(userHttpBasic(user0)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getByMail() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/" + "byEmail?email=" + admin.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getWithVotes() throws Exception {
        user3.setVotes(ALL_VOTES_FROM_USER3);
        perform(MockMvcRequestBuilders.get("/admin/users/" + USER3_ID + "/withVotes")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_VOTES_MATCHER.contentJson(user3));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/users/" + USER3_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertNull(userRepository.get(USER3_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/users/" + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ALL_USERS));
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/users/" + USER3_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "password3")))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.get(USER3_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/users")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());
        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.get(newId), newUser);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch("/admin/users/" + USER3_ID)
                .with(userHttpBasic(admin))
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.get(USER3_ID).isEnabled());
    }

    @Test
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post("/admin/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(invalid, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void updateInvalid() throws Exception {
        User invalid = new User(user3);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put("/admin/users/" + USER3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(invalid, "password3")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(user3);
        updated.setEmail("admin@gmail.com");
        perform(MockMvcRequestBuilders.put("/admin/users/" + USER3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated, "password3")))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(ErrorType.DATA_ERROR.name()));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        User expected = new User(null, "New", "user0@yandex.ru", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post("/admin/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(ErrorType.DATA_ERROR.name()));
    }
}