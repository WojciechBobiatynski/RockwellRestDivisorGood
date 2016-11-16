package pl.sodexo.it.gryf.web.ind.controller.reimbursment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static pl.sodexo.it.gryf.web.ind.util.IndPageConstant.PAGE_REIMBURSMENT_PIN_SEND_SUCCESS;

/**
 * Kontroler obsługujący akcje wysyłki pinu do rozliczenia szkolenia
 * na pocztę uczestnika
 *
 * Created by adziobek on 16.11.2016.
 */
@Controller
public class IndividualReimbursmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualReimbursmentController.class);

    @RequestMapping(value = "/ind/reimbursmentPin/send/{trainingInstanceId}", method = RequestMethod.POST)
    public String sendReimbursmentPin(@PathVariable("trainingInstanceId") Long trainingInstanceId) {

        return PAGE_REIMBURSMENT_PIN_SEND_SUCCESS;
    }
}