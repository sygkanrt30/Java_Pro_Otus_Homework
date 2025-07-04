package crm.service;

import static org.assertj.core.api.Assertions.assertThat;

import base.AbstractHibernateTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
@SuppressWarnings("java:S125")
class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        // given
        var client = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        // when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        // then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        // when
        var savedClientUpdated = loadedSavedClient.get();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        // then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        // when
        var clientList = dbServiceClient.findAll();

        // then
        assertThat(clientList).hasSize(1);
        assertThat(clientList.getFirst()).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }
}
