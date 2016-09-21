package pl.sodexo.it.gryf.model.publicbenefits.grantapplications;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import pl.sodexo.it.gryf.model.DictionaryEntity;
import pl.sodexo.it.gryf.model.GryfEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tomasz.bilski.ext
 */
@Entity
@Table(name = "GRANT_APPLICATION_VERSIONS", schema = "APP_PBE")
@NamedQueries({
        @NamedQuery(name = GrantApplicationVersion.FIND_BY_APPLICATION, query = "select v from GrantApplicationVersion v inner join v.applications a where a.id = :applicationId"),
        @NamedQuery(name = GrantApplicationVersion.FIND_BY_PROGRAM_IN_DATE, query = "select distinct v from GrantApplicationVersion v " +
                                                                                        "inner join v.applicationsInProgram e " +
                                                                                        "where e.id.programId = :grantProgramId " +
                                                                                        "and (e.dateFrom is null or e.dateFrom <= :date) " +
                                                                                        "and (e.dateTo is null or :date <= e.dateTo)")})
public class GrantApplicationVersion extends GryfEntity implements DictionaryEntity {

    //STATIC FIELDS - NAMED QUERY

    public static final String FIND_BY_APPLICATION = "GrantApplicationVersion.findByApplication";
    public static final String FIND_BY_PROGRAM_IN_DATE = "GrantApplicationVersion.findByProgramInDate";

    //STATIC FIELDS - ATRIBUTES

    public static final String NAME_ATTR_NAME = "name";

    //STATIC FIELDS - COMPONENT NAME FIELDS

    private static final String APPLICATION_DTO_PACKAGE_PREFIX = "pl.sodexo.it.gryf.common.dto.publicbenefits.grantapplications.detailsform.versions.";
    private static final String APPLICATION_DTO_QUALIFIED_CLASS_PREFIX = "GrantApplication";
    private static final String APPLICATION_DTO_QUALIFIED_CLASS_SUFFIX = "DTO";
    private static final String APPLICATION_SERVICE_SUFFIX = "Service";
    private static final String APPLICATION_SERVICE_PREFIX = "grantApplication";
    private static final String APPLICATION_FILE_PREFIX = "GrantApplication";

    //FIELDS

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COMPONENT_NAME")
    private String componentName;

    @JsonManagedReference("applications")
    @OneToMany(mappedBy = "applicationVersion")
    private List<GrantApplication> applications;

    @JsonManagedReference("applicationsInProgram")
    @OneToMany(mappedBy = "applicationVersion")
    private List<GrantApplicationInProgram> applicationsInProgram;

    @JsonManagedReference("attachmentRequiredList")
    @OneToMany(mappedBy = "applicationVersion")
    private List<GrantApplicationAttachmentRequired> attachmentRequiredList;

    //PUBLIC METHODS

    public List<String> getMandatoryAttachmentNames(){
        return Collections.singletonList("Formularz informacji do pomocy de minimis");
    }

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

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    //LIST METHODS

    private List<GrantApplication> getInitializedApplications() {
        if (applications == null)
            applications = new ArrayList<>();
        return applications;
    }

    public List<GrantApplication> getApplications() {
        return Collections.unmodifiableList(getInitializedApplications());
    }

    private List<GrantApplicationInProgram> getInitializedApplicationsInProgram() {
        if (applicationsInProgram == null)
            applicationsInProgram = new ArrayList<>();
        return applicationsInProgram;
    }

    public List<GrantApplicationInProgram> getApplicationsInProgram() {
        return Collections.unmodifiableList(getInitializedApplicationsInProgram());
    }

    private List<GrantApplicationAttachmentRequired> getInitializedApplicationRequiredList() {
        if (attachmentRequiredList == null)
            attachmentRequiredList = new ArrayList<>();
        return attachmentRequiredList;
    }

    public List<GrantApplicationAttachmentRequired> getAttachmentRequiredList() {
        return Collections.unmodifiableList(getInitializedApplicationRequiredList());
    }

    //DICTIONARY METHODS

    public Object getDictionaryId() {
        return id;
    }

    public String getDictionaryName() {
        return name;
    }

    public String getOrderField(){
        return NAME_ATTR_NAME;
    }

    public String getActiveField() {
        return null;
    }

    //COMPONENT METHODS

    public String getDtoClassName(){
        return getDtoClassName(componentName);
    }

    public String getServiceBeanName(){
        return getServiceBeanName(componentName);
    }

    public String getTemplateName(){
        return getTemplateName(componentName);
    }

    public static String getDtoClassName(String value){
        return (value != null) ? (APPLICATION_DTO_PACKAGE_PREFIX + APPLICATION_DTO_QUALIFIED_CLASS_PREFIX +
                value + APPLICATION_DTO_QUALIFIED_CLASS_SUFFIX) : null;
    }

    public static String getServiceBeanName(String value){
        return (value != null) ? (APPLICATION_SERVICE_PREFIX + value + APPLICATION_SERVICE_SUFFIX) : null;
    }

    public static String getTemplateName(String value){
        return (value != null) ? (APPLICATION_FILE_PREFIX + value) : null;
    }

    //EQUALS & HASH CODE

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return Objects.equals(id, ((GrantApplicationVersion) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) % 102;
    }

}
