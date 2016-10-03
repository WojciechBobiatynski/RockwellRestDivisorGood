package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-17.
 */
@ToString
public class OrderElementComplexTypeGrantApplicationContactDataDTO extends OrderElementDTO {

    //FIELDS

    private List<ContactDataDTO> contacts;

    //GETTERS & SETTERS

    public List<ContactDataDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDataDTO> contacts) {
        this.contacts = contacts;
    }

    //CLASS

    public static class ContactDataDTO {

        //FIELDS

        private Long id;

        private String name;

        private String email;

        private String phone;

        //GETTERS & SETTERS

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
