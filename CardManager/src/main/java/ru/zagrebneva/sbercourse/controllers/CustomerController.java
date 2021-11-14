package ru.zagrebneva.sbercourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.zagrebneva.sbercourse.dao.CustomerDAO;
import ru.zagrebneva.sbercourse.models.Card;
import ru.zagrebneva.sbercourse.models.Customer;
import javax.validation.Valid;


@Controller
@RequestMapping("/customers")
@SessionAttributes(types = Card.class)
public class CustomerController {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("customers", customerDAO.index());
        return "customers/index";
    }

    @GetMapping("/new")
    public String newCustomer(@ModelAttribute("customer") Customer customer) {
        return "customers/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("customer") @Valid Customer customer,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "customers/new";
        }
        Customer existedCustomer = customerDAO.getCustomer(customer);
        if (existedCustomer != null) {
            return "redirect:/customers/" + existedCustomer.getId();
        }
        customerDAO.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Customer customer = customerDAO.show(id);
        model.addAttribute("customer", customer);
        return "customers/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("customer", customerDAO.show(id));
        return "customers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("customer") @Valid Customer customer,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "customers/edit";
        customerDAO.update(id, customer);
        return "redirect:/customers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        customerDAO.delete(id);
        return "redirect:/customers";
    }

    // Work with credit cads

    @GetMapping("/{id}/card/new")
    public String newCard(@PathVariable("id") int id, Model model) {
        Customer customer = customerDAO.show(id);
        Card card = new Card(customer);
        model.addAttribute("card", card);
        return "cards/new";
    }

    @PostMapping("/{id}/card/new")
    public String createCard(@ModelAttribute("card") @Valid Card card,
                         BindingResult bindingResult,
                             SessionStatus status) {
        if (bindingResult.hasErrors()) {
            return "cards/new";
        }

        customerDAO.saveCard(card);
        status.setComplete();
        return "redirect:/customers/" + card.getCustomer().getId();
    }

    @GetMapping("/{id}/card/{card_id}")
    public String showCard(@PathVariable("id") int id,
                          @PathVariable("card_id") int cardId,
                          Model model) {
        Card card = customerDAO.showCard(cardId);
        model.addAttribute("card", card);
        System.out.println(card);
        System.out.println(card.getCustomer());
        return "cards/show";
    }

    @DeleteMapping("/{id}/card/{card_id}")
    public String deleteCard(@PathVariable("id") int id,
                             @PathVariable("card_id") int cardId) {
        customerDAO.deleteCard(cardId);
        return "redirect:/customers/" + id;
    }

}
