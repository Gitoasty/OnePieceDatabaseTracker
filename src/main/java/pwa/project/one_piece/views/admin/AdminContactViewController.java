package pwa.project.one_piece.views.admin;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pwa.project.one_piece.entity.ContactForm;

/**
 * <h1>
 *     Controller class for admin contact page
 * </h1>
 */
@Controller
@RequestMapping("admin-contact")
public class AdminContactViewController {

    @Value("${mailjet.api.key}")
    String apiKey;
    @Value("${mailjet.api.secret}")
    String apiSecret;

    /**
     * <h2>
     *     Method for showing the page
     * </h2>
     * @param model {@link Model} from Spring framework
     * @return {@link String} page to be displayed
     */
    @GetMapping
    public String showCharactersPage(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "admin-contact";
    }

    /**
     * <h2>
     *     Method for sending contact mail
     * </h2>
     * <p>
     *     Uses Mailjet API for sending contact emails to preset contact email address.
     * </p>
     * @param form {@link ContactForm} object describing contact form
     * @return {@link String} page to be displayed
     * @throws MailjetException in case sending email fails
     */
    @PostMapping("/send-email")
    public String sendContactForm(@ModelAttribute ContactForm form) throws MailjetException {
        String to = "matej.miklin12@gmail.com";
        String subject = "Upit sa stranice";
        String body = String.format("""
            Ime i prezime: %s
            Email: %s
            Ocjena stranice: %d
            """, form.getName(), form.getMail(), form.getBroj());

        MailjetClient client = new MailjetClient(apiKey, apiSecret);

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "matej.miklin12@gmail.com")
                                        .put("Name", "One Piece App"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)
                                                .put("Name", form.getName())))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, body)
                        )
                );

        client.post(request);

        return "admin-contact";
    }
}
