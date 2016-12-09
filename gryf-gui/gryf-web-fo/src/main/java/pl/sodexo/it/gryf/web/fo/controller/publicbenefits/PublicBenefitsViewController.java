package pl.sodexo.it.gryf.web.fo.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sodexo.it.gryf.common.dto.other.FileDTO;
import pl.sodexo.it.gryf.common.enums.Privileges;
import pl.sodexo.it.gryf.common.utils.GryfStringUtils;
import pl.sodexo.it.gryf.service.api.publicbenefits.grantapplications.GrantApplicationActionService;
import pl.sodexo.it.gryf.service.api.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.service.api.publicbenefits.reimbursement.ReimbursementsAttachmentService;
import pl.sodexo.it.gryf.service.api.security.SecurityChecker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.sodexo.it.gryf.web.common.util.WebUtils.writeFileToResponse;
import static pl.sodexo.it.gryf.web.fo.utils.UrlConstants.*;

@Controller
@RequestMapping("/publicBenefits")
public class PublicBenefitsViewController {

    @Autowired
    private SecurityChecker securityChecker;

    @Autowired
    private GrantApplicationActionService grantApplicationActionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReimbursementsAttachmentService reimbursementsAttachmentService;

    //PUBLIC METHODS - VIEWS

    @RequestMapping("/enterprises")
    public String getEnterprisesView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/enterprisesIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/individuals")
    public String getIndividualsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_INDIVIDUALS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/individualsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/trainingInstitutions")
    public String getTrainingInstitutionsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/trainingInstitutionsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/training")
    public String getTrainingView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_TI_TRAININGS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/trainingIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/grantApplications")
    public String getApplicationsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/grantApplicationsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/contracts")
    public String getContractsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_CONTRACTS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/contractsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/orders")
    public String getOrdersView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_ORDERS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/ordersIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/trainingInstances")
    public String getTrainingInstancesView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_TI_TRAINING_INSTANCES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/trainingInstancesIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/reimbursements")
    public String getReimbursementsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_DELIVERIES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/reimbursementsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping(PATH_ELECTRONIC_REIMBURSEMENTS)
    public String getElectronicReimbursementsView(Model model) {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_E_REIMBURSEMENTS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + PAGE_ELECTRONIC_REIMBURSEMENTS_SEARCH);
        return DEFAULT_VIEW;
    }

    //PUBLIC METHODS - DOWNLOAD ATTACHMENT

    @RequestMapping("/grantApplications/downloadAttachment")
    public void downloadGrantApplicationAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long attachmentId = Long.valueOf(idParam);

            FileDTO file = grantApplicationActionService.getApplicationAttachmentFile(attachmentId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/orders/downloadAttachment")
    public void downloadOrderAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_ORDERS);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = orderService.getOrderAttachmentFile(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/reimbursements/downloadAttachment")
    public void downloadReimbursementAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = reimbursementsAttachmentService.getReimbursementAttachmentFile(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/reimbursements/trainee/downloadAttachment")
    public void downloadReimbursementTraineeAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityChecker.assertFormPrivilege(Privileges.GRF_PBE_REIMB);

        String idParam = request.getParameter("id");
        if(!GryfStringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = reimbursementsAttachmentService.getReimbursementTraineeAttachmentFile(elementId);
            writeFileToResponse(request, response, file.getInputStream(), file.getName());
        }
    }
}
