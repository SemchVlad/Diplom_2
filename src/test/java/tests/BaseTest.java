package tests;

import client.UserClient;
import data.FactoryTestData;
import io.restassured.response.Response;
import model.User;
import model.UserResponse;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

public class BaseTest {
    protected UserResponse userResponse;
    protected User user;
    UserClient userClient = new UserClient();

    @Before
    public void setUp() throws IOException {
        user = FactoryTestData.createNewTestUser();
        Response response = userClient.createUser(user);
        userResponse = response.as(UserResponse.class);
    }

    @After
    public void tearDown() {
        if (userResponse.getAccessToken() != null) {
            userClient.deleteUser(userResponse.getAccessToken());
        }
        userResponse = null;
        user = null;
    }
}
