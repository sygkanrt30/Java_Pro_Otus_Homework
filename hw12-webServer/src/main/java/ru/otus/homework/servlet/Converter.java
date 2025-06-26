package ru.otus.homework.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;
import ru.otus.homework.hibernate.crm.model.Client;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Converter {
    public List<JsonClient> convertToClientsForSerializable(List<Client> clients) {
        List<JsonClient> jsonClients = new ArrayList<>();
        for (var client : clients) {
            long id = client.getId();
            String name = client.getName();
            String address = client.getAddress().getStreet();
            var phones = new ArrayList<String>();
            for (var phone : client.getPhoneList()) {
                phones.add(phone.getNumber());
            }
            var jsonClient = new JsonClient(id, name, address, phones);
            jsonClients.add(jsonClient);
        }
        return jsonClients;
    }

    @Data
    @AllArgsConstructor
    public static class JsonClient {
        private Long id;
        private String name;
        private String address;
        private List<String> phones;
    }
}
