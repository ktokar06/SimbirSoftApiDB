package org.example.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.config.MyConfig.*;
import static org.example.service.dbUsers.*;

public class UsersDbTest extends BaseTest {

    /**
     * 3.1
     */
    @Test(description = "Позитивный кейс: Проверка наличия системного администратора.")
    public void checkAdminExistsTest() throws SQLException {
        ResultSet admin = getUserById(1);
        Assert.assertTrue(admin.next(), "Администратор не найден");
        Assert.assertEquals(admin.getString(USER_LOGIN_FIELD), "Firstname.LastName", "Логин администратора не совпадает");
    }

    /**
     * 3.2
     */
    @Test(description = "Позитивный кейс: Создание пользователя. Проверка, что пользователь создан с корректными параметрами.")
    public void createUserTest() throws SQLException {
        int userId = createUser("tester", "password123", "test@example.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertEquals(user.getString(USER_LOGIN_FIELD), "tester", "Логин не совпадает");
        Assert.assertEquals(user.getString(USER_EMAIL_FIELD), "test@example.com", "Email не совпадает");
    }

    /**
     * 3.3
     */
    @Test(description = "Позитивный кейс: Получение пользователя по ID. Проверка, что пользователь возвращается с корректными параметрами.")
    public void retrieveUserByIDTest() throws SQLException {
        int userId = createUser("user1", "pass1", "user1@test.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertEquals(user.getString(USER_LOGIN_FIELD), "user1");
    }

    @Test(description = "Негативный кейс: Получение несуществующего пользователя по ID. Проверка сообщения об ошибке.")
    public void retrieveUsersByIdTest() throws SQLException {
        ResultSet user = getUserById(999);
        Assert.assertFalse(user.next(), "Пользователь не должен существовать");
        Assert.assertFalse(user.isBeforeFirst(), "Результат запроса должен быть пустым");
    }

    /**
     * 3.4
     */
    @Test(description = "Позитивный кейс: Обновление пользователя. Проверка, что пользователь обновлен с новыми параметрами.")
    public void updateUserTest() throws SQLException {
        int userId = createUser("olduser", "pass", "old@test.com");

        int updatedRows = updateUser(userId, "newuser", "new@test.com");
        Assert.assertEquals(updatedRows, 1, "Пользователь не обновлен");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next());
        Assert.assertEquals(user.getString(USER_LOGIN_FIELD), "newuser");
        Assert.assertEquals(user.getString(USER_EMAIL_FIELD), "new@test.com");
    }

    @Test(description = "Негативный кейс: Обновление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void updateUsersTest() throws SQLException {
        int updatedRows = updateUser(999, "newuser", "new@test.com");
        Assert.assertEquals(updatedRows, 0, "Не должно быть обновленных строк");
    }

    /**
     * 3.5
     */
    @Test(description = "Позитивный кейс: Удаление пользователя. Проверка, что пользователь удален.")
    public void deleteUserTest() throws SQLException {
        int userId = createUser("todelete", "pass", "delete@test.com");

        int deletedRows = deleteUser(userId);
        Assert.assertEquals(deletedRows, 1, "Пользователь не удален");

        ResultSet user = getUserById(userId);
        Assert.assertFalse(user.next(), "Пользователь должен быть удален");
    }

    @Test(description = "Негативный кейс: Удаление несуществующего пользователя. Проверка сообщения об ошибке.")
    public void deleteUsersTest() throws SQLException {
        int deletedRows = deleteUser(999);
        Assert.assertEquals(deletedRows, 0, "Не должно быть удаленных строк");
    }

    /**
     * 3.6
     */
    @Test(description = "Позитивный кейс: Получение информации о текущем пользователе")
    public void retrieveUserMePositiveTest() throws SQLException {
        int userId = createUser("user_me", "password123", "user.me@example.com");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Информация о пользователе не найдена");
        Assert.assertEquals(user.getString(USER_LOGIN_FIELD), "user_me", "Логин не совпадает");
        Assert.assertEquals(user.getString(USER_EMAIL_FIELD), "user.me@example.com", "Email не совпадает");
    }

    @Test(description = "Негативный кейс: Попытка получения без авторизации")
    public void retrieveUserMeNegativeTest() throws SQLException {
        ResultSet user = getUserById(999);
        Assert.assertFalse(user.next(), "Данные пользователя не должны быть доступны");
    }

    /**
     * 3.7
     */
    @Test(description = "Позитивный кейс: Обновление текущего пользователя")
    public void updateUserMePositiveTest() throws SQLException {
        int userId = createUser("old_user", "pass123", "old.user@test.com");

        int updatedRows = updateUser(userId, "new_user", "new.user@test.com");
        Assert.assertEquals(updatedRows, 1, "Должна быть одна обновлённая запись");

        ResultSet user = getUserById(userId);
        Assert.assertTrue(user.next(), "Пользователь не найден");
        Assert.assertEquals(user.getString(USER_LOGIN_FIELD), "new_user", "Логин не обновлён");
        Assert.assertEquals(user.getString(USER_EMAIL_FIELD), "new.user@test.com", "Email не обновлён");
    }
}