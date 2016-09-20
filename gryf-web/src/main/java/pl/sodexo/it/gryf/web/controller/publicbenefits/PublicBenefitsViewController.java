package pl.sodexo.it.gryf.web.controller.publicbenefits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sodexo.it.gryf.Privileges;
import pl.sodexo.it.gryf.dto.FileDTO;
import pl.sodexo.it.gryf.root.service.SecurityCheckerService;
import pl.sodexo.it.gryf.root.service.publicbenefits.grantapplications.GrantApplicationsService;
import pl.sodexo.it.gryf.root.service.publicbenefits.orders.OrderService;
import pl.sodexo.it.gryf.root.service.publicbenefits.reimbursement.ReimbursementsService;
import pl.sodexo.it.gryf.utils.GryfUtils;
import pl.sodexo.it.gryf.utils.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static pl.sodexo.it.gryf.web.ViewResolverAttributes.DEFAULT_VIEW;
import static pl.sodexo.it.gryf.web.ViewResolverAttributes.MAIN_CONTENT_PARAM_NAME;
import static pl.sodexo.it.gryf.web.controller.ControllersUrls.PAGES_PREFIX;



@Controller
@RequestMapping("/publicBenefits")
public class PublicBenefitsViewController {

    @Autowired
    private SecurityCheckerService securityCheckerService;

    @Autowired
    private GrantApplicationsService grantApplicationsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReimbursementsService reimbursementsService;

    //PUBLIC METHODS - VIEWS

    @RequestMapping("/enterprises")
    public String getEnterprisesView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/enterprisesIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/individuals")
    public String getIndividualsView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_ENTERPRISES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/individualsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/trainingInstitutions")
    public String getTrainingInstitutionsView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_TRAINING_INSTITUTIONS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/trainingInstitutionsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/grantApplications")
    public String getApplicationsView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/grantApplicationsIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/orders")
    public String getOrdersView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_ORDERS);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/ordersIndex.jsp");
        return DEFAULT_VIEW;
    }

    @RequestMapping("/reimbursements")
    public String getReimbursementsView(Model model) {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_DELIVERIES);
        model.addAttribute(MAIN_CONTENT_PARAM_NAME, PAGES_PREFIX + "publicbenefits/reimbursementsIndex.jsp");
        return DEFAULT_VIEW;
    }

    //PUBLIC METHODS - DOWNLOAD ATTACHMENT

    @RequestMapping("/grantApplications/downloadAttachment")
    public void downloadGrantApplicationAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_APPLICATIONS);

        String idParam = request.getParameter("id");
        if(!StringUtils.isEmpty(idParam)) {
            Long attachmentId = Long.valueOf(idParam);

            FileDTO file = grantApplicationsService.getApplicationAttachmentFile(attachmentId);
            writeFile(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/orders/downloadAttachment")
    public void downloadOrderAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_ORDERS);

        String idParam = request.getParameter("id");
        if(!StringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = orderService.getOrderAttachmentFile(elementId);
            writeFile(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/reimbursements/downloadAttachment")
    public void downloadReimbursementAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_REIMB);

        String idParam = request.getParameter("id");
        if(!StringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = reimbursementsService.getReimbursementAttachmentFile(elementId);
            writeFile(request, response, file.getInputStream(), file.getName());
        }
    }

    @RequestMapping("/reimbursements/trainee/downloadAttachment")
    public void downloadReimbursementTraineeAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        securityCheckerService.assertFormPrivilege(Privileges.GRF_PBE_REIMB);

        String idParam = request.getParameter("id");
        if(!StringUtils.isEmpty(idParam)) {
            Long elementId = Long.valueOf(idParam);

            FileDTO file = reimbursementsService.getReimbursementTraineeAttachmentFile(elementId);
            writeFile(request, response, file.getInputStream(), file.getName());
        }
    }

    //PRIVATE METHODS

    private void writeFile(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName) throws IOException {
        //MIME TYPE
        ServletContext context = request.getServletContext();
        String mimeType = context.getMimeType(fileName);
        response.setContentType((mimeType == null) ? "application/octet-stream" : mimeType);

        //HEADER
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));

        //COPY STREAM
        OutputStream outStream = response.getOutputStream();
        GryfUtils.copyStream(inputStream, outStream);

        inputStream.close();
        outStream.close();
    }

}
