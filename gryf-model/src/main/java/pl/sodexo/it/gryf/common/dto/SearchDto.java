package pl.sodexo.it.gryf.common.dto;

import java.util.List;

/**
 * Created by tomasz.bilski.ext on 2015-06-17.
 */
public abstract class SearchDto {

    //FIELDS

    protected Integer limit;

    protected List<String> sortColumns;

    protected List<SortType> sortTypes;

    //GETTERS & SETTERS

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<String> getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(List<String> sortColumns) {
        this.sortColumns = sortColumns;
    }

    public List<SortType> getSortTypes() {
        return sortTypes;
    }

    public void setSortTypes(List<SortType> sortTypes) {
        this.sortTypes = sortTypes;
    }

}
