package com.payment.controller; //package decleration

import com.payment.model.Payment;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payments") //Sets a base URL for all request mappings in this controller.
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String viewPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payment/list";
    }

    @GetMapping("/new")
    public String showPaymentForm(Model model) {
        model.addAttribute("payment", new Payment());
        return "payment/form";
    }

    @PostMapping("/save") //Used to pass flash messages (like success/error) during redirects - RedirectAttributes
    public String savePayment(@ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {
        try {
            paymentService.createPayment(payment);
            redirectAttributes.addFlashAttribute("success", "Payment processed successfully!");
            return "redirect:/payments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/new";
        }
    }

    @GetMapping("/{id}") //View a single payment
    public String viewSinglePayment(@PathVariable("id") String id, Model model) {
        paymentService.getPaymentById(id).ifPresent(p -> model.addAttribute("payment", p));
        return "payment/view";
    }
}