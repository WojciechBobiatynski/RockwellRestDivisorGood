package pl.sodexo.it.gryf.dao.impl.crud.repository.other;

import org.springframework.stereotype.Repository;
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
    public FinanceNoteResult createCreditNoteForOrder(Long orderId){
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PK_GRF_UTILS.Create_Pb_Cus_Note");
        query.registerStoredProcedureParameter("o_inv_id", Double.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_number", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_type", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("o_invoice_date", Date.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("a_order_id", Double.class, ParameterMode.IN);
        query.setParameter("a_order_id", orderId);

        query.execute();

        FinanceNoteResult result = new FinanceNoteResult();
        result.setInvoiceId(((Double) query.getOutputParameterValue("o_inv_id")).longValue());
        result.setInvoiceNumber((String) query.getOutputParameterValue("o_invoice_number"));
        result.setInvoiceType((String) query.getOutputParameterValue("o_invoice_type"));
        result.setInvoiceDate((Date) query.getOutputParameterValue("o_invoice_date"));
        return result;
    }

    @Override
    public void flush(){
        entityManager.flush();
    }



    
}
