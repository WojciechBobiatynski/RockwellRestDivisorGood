package pl.sodexo.it.gryf.common.dto.publicbenefits.orders.detailsform.elements;

import pl.sodexo.it.gryf.common.dto.DictionaryDTO;
import pl.sodexo.it.gryf.common.utils.GryfUtils;

import java.util.List;

/**
 * DTO for element type Combobox
 * Created by Michal.CHWEDCZUK.ext on 2015-07-23.
 */
public class OrderElementComboboxDTO extends OrderElementDTO {

    //FIELDS

    private List<DictionaryDTO> items;

    private DictionaryDTO selectedItem;

    //CONSTRUCTORS

    public OrderElementComboboxDTO() {
    }

    public OrderElementComboboxDTO(final OrderElementDTOBuilder builder, List<DictionaryDTO> items) {
        super(builder);
        this.items = items;
        selectedItem = GryfUtils.find(items, new GryfUtils.Predicate<DictionaryDTO>() {
            public boolean apply(DictionaryDTO input) {
                return input.getId().equals(builder.getElement().getValueVarchar());
            }
        });
    }

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
