package pl.sodexo.it.gryf.web.fo.utils;

/**
 * Created by Michal.CHWEDCZUK.ext on 2015-07-21.
 */
public final class UrlConstants {

    private UrlConstants() {}

    public static final String PUBLIC_BENEFITS_REST = "/rest/publicBenefits";
    public static final String DICTIONARIES_REST = "/rest/dictionaries";

    public static final String PAGES_PREFIX = "/WEB-INF/pages/";

    /**
     * It's a path to a base file which should be used for all views.
     */
    public static final String DEFAULT_VIEW = "/WEB-INF/pages/index.jsp";

    /**
     * It's a path to a login page.
     */
    public static final String LOGIN_VIEW = "/WEB-INF/pages/login.jsp";

    /**
     * An attribute name which should be put in to the model in a controller
     * to store link for individual page content.
     */
    public static final String MAIN_CONTENT_PARAM_NAME = "pageMainContent";

    public static final String PATH_ELECTRONIC_REIMBURSEMENTS = "/electronic/reimbursements";
    public static final String PAGE_ELECTRONIC_REIMBURSEMENTS_SEARCH = "publicbenefits/electronicReimbursementsIndex.jsp";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_LIST =  "/list";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_STATUSES_LIST =  "/statuses";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_TYPES_LIST =  "/types";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_FIND =  "/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CHANGE_STATUS = "/status";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CORRECTION_DATE = "/correctionDate/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CORRECTIONS_LIST = "/correction/list/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_ATT = "/downloadAttachment";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CREATE_DOCUMENTS = "/createDocuments/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_PRINT_REPORTS = "/printReports/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CONFIRM = "/confirm/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CANCEL = "/cancel/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_CORR_ATT = "/downloadCorrAttachment";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_DOWNLOAD_REPORT_FILE = "/downloadReportFile";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_CREATE_EMAIL_FROM_TEMPLATE= "/email/create/";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_SEND_EMAILS= "/email/send";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_ATT_SAVE= "/att/save";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_REJECT= "/reject";
    public static final String PATH_ELECTRONIC_REIMBURSEMENTS_AUTO_CONFIRM = "/automatic/confirm/";

    public static final String PATH_UNRESERVED_POOL_REIMBURSEMENTS = "/unrsv/reimbursements";
    public static final String PATH_UNRESERVED_POOL_REIMBURSEMENTS_FIND =  "/";
    public static final String PATH_UNRESERVED_POOL_CREATE_DOCUMENTS = "/createDocuments/";
    public static final String PATH_UNRESERVED_POOL_PRINT_REPORTS = "/printReports/";
    public static final String PATH_UNRESERVED_POOL_EXPIRE = "/expire/";
    public static final String PATH_UNRESERVED_POOL_CREATE_EMAIL_FROM_TEMPLATE= "/email/create/";

    public static final String PATH_TRAINING_INSTANCE = "/trainingInstance";
    public static final String PATH_TRAINING_INSTANCE_LIST =  "/list";
    public static final String PATH_TRAINING_INSTANCE_STATUSES_LIST =  "/statuses";
    public static final String PATH_TRAINING_INSTANCE_DETAILS_FIND =  "/details";

    public static final String PATH_CONTRACTS_FIND_POOL_INSTANCES = "/poolInstances";
    public static final String PATH_CONTRACTS_RESIGN = "/resign";

    public static final String CHANGE_PASSWORD_PATH = "/pass/change";
    public static final String CHANGE_PASSWORD_INDEX = "/administration/password/password.jsp";
}
