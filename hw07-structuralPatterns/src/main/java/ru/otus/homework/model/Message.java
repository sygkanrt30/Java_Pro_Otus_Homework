package ru.otus.homework.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("All")
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true)
public class Message {
    long id;
    String field1;
    String field2;
    String field3;
    String field4;
    String field5;
    String field6;
    String field7;
    String field8;
    String field9;
    String field10;
    String field11;
    String field12;
    ObjectForMessage field13;

    public Builder toBuilder() {
        return new Builder(
                id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12,
                field13);
    }

    public Message clone() {
        ObjectForMessage objectForMessage = null;
        if (field13 != null) {
            objectForMessage = field13.copy();
        }
        return new Message(
                id,
                field1,
                field2,
                field3,
                field4,
                field5,
                field6,
                field7,
                field8,
                field9,
                field10,
                field11,
                field12,
                objectForMessage);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        final long id;
        String field1;
        String field2;
        String field3;
        String field4;
        String field5;
        String field6;
        String field7;
        String field8;
        String field9;
        String field10;
        String field11;
        String field12;
        ObjectForMessage field13;

        public Builder(long id) {
            this.id = id;
        }

        public Builder field1(String field1) {
            this.field1 = field1;
            return this;
        }

        public Builder field2(String field2) {
            this.field2 = field2;
            return this;
        }

        public Builder field3(String field3) {
            this.field3 = field3;
            return this;
        }

        public Builder field4(String field4) {
            this.field4 = field4;
            return this;
        }

        public Builder field5(String field5) {
            this.field5 = field5;
            return this;
        }

        public Builder field6(String field6) {
            this.field6 = field6;
            return this;
        }

        public Builder field7(String field7) {
            this.field7 = field7;
            return this;
        }

        public Builder field8(String field8) {
            this.field8 = field8;
            return this;
        }

        public Builder field9(String field9) {
            this.field9 = field9;
            return this;
        }

        public Builder field10(String field10) {
            this.field10 = field10;
            return this;
        }

        public Builder field11(String field11) {
            this.field11 = field11;
            return this;
        }

        public Builder field12(String field12) {
            this.field12 = field12;
            return this;
        }

        public Builder field13(ObjectForMessage field13) {
            this.field13 = field13;
            return this;
        }

        public Message build() {
            return new Message(
                    id, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11,
                    field12, field13);
        }
    }
}
