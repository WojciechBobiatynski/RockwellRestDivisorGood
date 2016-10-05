package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import lombok.ToString;
import pl.sodexo.it.gryf.common.dto.other.DictionaryDTO;

import java.util.List;

/**
 * DTO for element type Combobox
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
@ToString
public class OrderElementComboboxDTO extends OrderElementDTO {

    //FIELDS

    private List<DictionaryDTO> items;

    private DictionaryDTO selectedItem;

    //GETTERS & SETTERS

    public List<DictionaryDTO> getItems() {
        return items;
    }

    public void setItems(List<DictionaryDTO> items) {
        this.items = items;
    }

    public DictionaryDTO getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(DictionaryDTO selectedItem) {
        this.selectedItem = selectedItem;
    }
}
