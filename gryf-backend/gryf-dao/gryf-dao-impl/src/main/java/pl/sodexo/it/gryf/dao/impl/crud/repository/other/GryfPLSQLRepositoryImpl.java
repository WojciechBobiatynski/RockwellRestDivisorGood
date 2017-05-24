package pl.sodexo.it.gryf.dao.impl.crud.repository.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.sodexo.it.gryf.common.dto.password.ChangePasswordDto;
import pl.sodexo.it.gryf.dao.api.crud.repository.other.GryfPLSQLRepository;
import pl.sodexo.it.gryf.model.api.FinanceNoteResult;
import pl.sodexo.it.gryf.model.enums.DayType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomasz.bilski.ext on 2015-09-04.
 */
@Repository
public class GryfPLSQLRepositoryImpl implements GryfPLSQLRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GryfPLSQLRepositoryImpl.class);

    //FIELDS

    @PersistenceContext
    private EntityManager entityManager;

    //PUBLIC METHODS

    @Override
    public Date getNextBusinessDay(Date date){
        String nativeSql = "select PK_GRF_UTILS.Next_Business_Day(?1) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        return (Date)query.getSingleResult();
    }

    @Override
    public Date getNthBusinessDay(Date date, Integer days){
        String nativeSql = "select PK_GRF_UTILS.Get_Nth_Business_Day(?1, ?2) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        query.setParameter(2, days);
        return (Date)query.getSingleResult();
    }
    
    @Override
    public Date getNthDay(Date date, Integer days, DayType dayType){
        String nativeSql = "select PK_GRF_UTILS.Get_Nth_Day(?1, ?2, ?3) from dual";
        Query query = entityManager.createNativeQuery(nativeSql);
        query.setParameter(1, date);
        query.setParameter(2, days);
        query.setParameter(3, dayType.name());
        return (Date)query.getSingleResult();
    }

    @Override
    public String generateAccountByCode(String code) {
        Query query = entityManager.createNativeQuery("SELECT eagle.t$bank_account.GET_COR_SPP(?1) FROM dual");
        query.setParameter(1, code);
        return (String) query.getSingleResult();
    }

    @Override
    public FinanceNoteResult createDebitNoteForOrder(Long orderId, String userLogin){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PK_GRF_UTILS.Create_Pb_Cus_Note");
        query.registerStoredProcedureParameter("o_inv_id", Double.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_number", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_type", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_date", Date.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_87_invoice_number", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("a_order_id", Double.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("a_sign_user", String.class, ParameterMode.IN);
        query.setParameter("a_order_id", orderId);
        query.setParameter("a_sign_user", userLogin);

        LOGGER.debug(String.format("Zamówienie '%s', użytkownik: %s: start wywołania procedury PK_GRF_UTILS.Create_Pb_Cus_Note", orderId, userLogin));
        query.execute();
        LOGGER.debug(String.format("Zamówienie '%s', użytkownik: %s: stop wywołania procedury PK_GRF_UTILS.Create_Pb_Cus_Note", orderId, userLogin));

        FinanceNoteResult result = new FinanceNoteResult();
        result.setInvoiceId(((Double) query.getOutputParameterValue("o_inv_id")).longValue());
        result.setInvoiceNumber((String) query.getOutputParameterValue("o_invoice_number"));
        result.setInvoiceType((String) query.getOutputParameterValue("o_invoice_type"));
        result.setInvoiceDate((Date) query.getOutputParameterValue("o_invoice_date"));
        result.setWupDebtDocumentNumber((String) query.getOutputParameterValue("o_87_invoice_number"));

        LOGGER.debug(String.format("Zamówienie '%s': rezultat wywołania procedury PK_GRF_UTILS.Create_Pb_Cus_Note"
                + "(o_inv_id='%s', o_invoice_number='%s', o_invoice_type='%s', o_invoice_date='%s')", orderId,
                result.getInvoiceId(), result.getInvoiceNumber(), result.getInvoiceType(), result.getInvoiceDate()));
        return result;
    }

    @Override
    public FinanceNoteResult createCreditNoteForReimbursment(Long ereimbursmentId){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PK_GRF_UTILS.Create_Pb_Rmb_Note");
        query.registerStoredProcedureParameter("o_inv_id", Double.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_number", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_type", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_date", Date.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("a_ermb_id", Double.class, ParameterMode.IN);
        query.setParameter("a_ermb_id", ereimbursmentId);

        LOGGER.debug(String.format("Rozliczenie '%s': start wywołania procedury PK_GRF_UTILS.Create_Pb_Rmb_Note", ereimbursmentId));
        query.execute();
        LOGGER.debug(String.format("Rozliczenie '%s': stop wywołania procedury PK_GRF_UTILS.Create_Pb_Rmb_Note", ereimbursmentId));


        FinanceNoteResult result = new FinanceNoteResult();
        result.setInvoiceId(((Double) query.getOutputParameterValue("o_inv_id")).longValue());
        result.setInvoiceNumber((String) query.getOutputParameterValue("o_invoice_number"));
        result.setInvoiceType((String) query.getOutputParameterValue("o_invoice_type"));
        result.setInvoiceDate((Date) query.getOutputParameterValue("o_invoice_date"));
        LOGGER.debug(String.format("Rozliczenie '%s': rezultat wywołania procedury PK_GRF_UTILS.Create_Pb_Rmb_Note"
                        + "(o_inv_id='%s', o_invoice_number='%s', o_invoice_type='%s', o_invoice_date='%s')", ereimbursmentId,
                        result.getInvoiceId(), result.getInvoiceNumber(), result.getInvoiceType(), result.getInvoiceDate()));

        return result;
    }

    @Override
    public void generateInstancesPrintNumber(String productId, Long numberFrom, Long numberTo){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PK_GRF_UTILS.Gen_Instances");
        query.registerStoredProcedureParameter("a_pbe_prd_id", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("a_first", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("a_last", Long.class, ParameterMode.IN);
        query.setParameter("a_pbe_prd_id", productId);
        query.setParameter("a_first", numberFrom);
        query.setParameter("a_last", numberTo);

        query.execute();
    }


    @Override
    public void flush(){
        entityManager.flush();
    }

    @Override
    public void changePassword(String username, ChangePasswordDto changePasswordDto) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PK_GRF_UTILS.Change_Passwd");
        query.registerStoredProcedureParameter("username", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("password", String.class, ParameterMode.IN);

        query.setParameter("username", username);
        query.setParameter("password", changePasswordDto.getNewPassword());

        query.execute();
    }





}
