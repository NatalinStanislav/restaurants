package com.natalinstanislav.restaurants.web.user;

import com.natalinstanislav.restaurants.model.User;
import com.natalinstanislav.restaurants.repository.user.UserRepository;
import com.natalinstanislav.restaurants.web.AbstractControllerTest;
import com.natalinstanislav.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.natalinstanislav.restaurants.TestUtil.readFromJson;
import static com.natalinstanislav.restaurants.UserTestData.*;
import static com.natalinstanislav.restaurants.VoteTestData.ALL_VOTES_FROM_USER3;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/" + ADMIN_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getByMail() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/" + "byEmail?email=" + admin.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getWithVotes() throws Exception {
        user3.setVotes(ALL_VOTES_FROM_USER3);
        perform(MockMvcRequestBuilders.get("/admin/users/" + USER3_ID + "/withVotes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_VOTES_MATCHER.contentJson(user3));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/admin/users/" + USER3_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertNull(userRepository.get(USER3_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/admin/users/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ALL_USERS));
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/admin/users/" + USER3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.get(USER3_ID), updated);
    }

    @Test
    void create() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUser)))
                .andExpect(status().isCreated());
        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.get(newId), newUser);
    }
}