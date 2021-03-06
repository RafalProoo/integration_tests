package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Przepracowany");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }

    @Test
    public void shouldFindUserByNameAllCaps() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JAN", "ExampleData",
                "ExampleData");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByNameAllLowerCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "ExampleData",
                "ExampleData");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByNameVariousLetterCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jAn", "ExampleData",
                "ExampleData");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldNotSpecifiedUser() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("ExampleData",
                "ExampleData", "ExampleData");
        assertThat(users.contains(user), equalTo(false));
    }

    @Test
    public void shouldFindUserByMailVariousLetterCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("ExampleData",
                "ExampleData", "jOhN@dOmAIn.com");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByLastNameVariousLetterCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("ExampleData",
                "prZEPraCoWAny", "ExampleData");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByGivenData() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jAN", "prZEPraCoWAny",
                "jOhN@dOmain.com");
        assertThat(users.contains(user), equalTo(true));
    }
}
