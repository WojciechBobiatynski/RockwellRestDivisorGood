package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplication;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationBasicData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactData;
import pl.sodexo.it.gryf.model.publicbenefits.grantapplications.GrantApplicationContactType;
import pl.sodexo.it.gryf.model.publicbenefits.orders.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-09-17.
 */
@ToString
public class OrderElementComplexTypeGrantApplicationContactDataDTO extends OrderElementDTO {

    //FIELDS

    private List<ContactDataDTO> contacts;

    //CONSTRUCTORS

    public OrderElementComplexTypeGrantApplicationContactDataDTO(){
    }

    public OrderElementComplexTypeGrantApplicationContactDataDTO(OrderElementDTOBuilder builder) {
        super(builder);
        this.contacts = new ArrayList<>();

        Order order = builder.getOrder();
        GrantApplication application = order.getApplication();
        GrantApplicationBasicData basicData = application.getBasicData();
        for (GrantApplicationContactData contact : basicData.getContacts()) {

            if(contact.getContactType() == GrantApplicationContactType.CONTACT) {
                ContactDataDTO contactDTO = new ContactDataDTO();
                contactDTO.setName(contact.getName());
                contactDTO.setEmail(contact.getEmail());
                contactDTO.setPhone(contact.getPhone());
                this.contacts.add(contactDTO);
            }
        }
    }

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
