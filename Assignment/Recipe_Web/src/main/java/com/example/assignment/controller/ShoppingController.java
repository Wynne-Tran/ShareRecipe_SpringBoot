
/* ********************************************************************************
 * Project: Create a Recipe Project Using Spring/Spring Boot
 * Assignment: 1 & 2
 * Author(s): Wynne Tran
 * Student Number: 101161665
 * Date: Nov 4 2021
 * Description:  this page use as controller for all function shopping cart
 ******************************************************************************** */


package com.example.assignment.controller;

import com.example.assignment.model.ReportSale;
import com.example.assignment.model.Shopping_Cart;
import com.example.assignment.model.Users;
import com.example.assignment.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ShoppingController {

    @Autowired
    UserService userService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    ShoppingService shoppingService;
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    ReportSaleService reportSaleService;

    @GetMapping("/view_cart")
    public  String viewCart(HttpSession session, Model model){
        String email = (String) session.getAttribute("email");
        Users user = userService.findOne(email);
        user.setRecipeCount(recipeService.findAllUser(user).size());
        user.setLikeCount(favoriteService.findByEmail(email).size());
        double pay = 0;
        int count = 0;
        List<Shopping_Cart> newList = shoppingService.findByEmail(email);
        for (Shopping_Cart shopping_cart : newList) {
            count += shopping_cart.getQuantity();
            pay += shopping_cart.getTotalPrice();
        }
        if(reportSaleService.findOne(LocalDate.now(), email) != null){
            reportSaleService.deleteReport(LocalDate.now(), email);
        }
        reportSaleService.addReportSale(new ReportSale(user, LocalDate.now(), pay));
        user.setShoppingCount(count);
        model.addAttribute("carts", shoppingService.findByEmail(email));
        model.addAttribute("user", user);
        model.addAttribute("totalPay",pay);
        session.setAttribute("cart", user.getShoppingCount());
        return "view_cart";
    }

    @PostMapping("/addCart")
    public  String addCart(@RequestParam("id") Long id,  HttpSession session, Model model){
        String email = (String)session.getAttribute("email");
        Shopping_Cart checkAvailable = shoppingService.findByRecipes_id(id);
        if (checkAvailable != null){
            checkAvailable.setQuantity(checkAvailable.getQuantity() + 1);
            checkAvailable.setTotalPrice(checkAvailable.getQuantity() * checkAvailable.getRecipes().getPrice());
            int newCount = shoppingService.findByEmail(email).size() + 1;
            userService.findOne(email).setShoppingCount(newCount);
            shoppingService.addToCart(checkAvailable);
            session.getAttribute("cart");
            return "redirect:/viewrecipe";
        }
        Shopping_Cart newCart = new Shopping_Cart(email, id, recipeService.findOne(id));
        newCart.setTotalPrice(newCart.getQuantity() * newCart.getRecipes().getPrice());
        shoppingService.addToCart(newCart);
        model.addAttribute("recipes", recipeService.findAll());
        return "redirect:/viewrecipe";
    }

    @PostMapping("/deleteCart")
    public  String deleteCart(@RequestParam("id") Integer id){
        shoppingService.deleteCart(id);
        return "redirect:/view_cart";
    }

    @PostMapping("/minusQuantityCart")
    public  String minusQuanityCart(@RequestParam("id") Integer id, HttpSession session){
        String email = (String) session.getAttribute("email");
        Shopping_Cart updateCart = shoppingService.findOne(id);
        if(updateCart.getQuantity() > 1) {
            updateCart.setQuantity(updateCart.getQuantity() - 1);
            updateCart.setTotalPrice(updateCart.getQuantity()  * updateCart.getRecipes().getPrice());
            shoppingService.addToCart(updateCart);
            updateCart.getUser().setShoppingCount( updateCart.getUser().getShoppingCount() - 1);

            ReportSale updateReport = reportSaleService.findOne(LocalDate.now(), email);
            updateReport.setPayment(updateCart.getQuantity()  * updateCart.getRecipes().getPrice());
            reportSaleService.addReportSale(updateReport);
            return "redirect:/view_cart";
        }
        return "redirect:/view_cart";
    }

    @PostMapping("/addQuantityCart")
    public  String addQuanityCart(@RequestParam("id") Integer id, HttpSession session){
        Shopping_Cart updateCart = shoppingService.findOne(id);
        String email = (String) session.getAttribute("email");
        if(updateCart.getQuantity()  < 10) {
            updateCart.setQuantity(updateCart.getQuantity() + 1);
            updateCart.getUser().setShoppingCount( updateCart.getUser().getShoppingCount() + 1);
            updateCart.setTotalPrice(updateCart.getQuantity()  * updateCart.getRecipes().getPrice());
            shoppingService.addToCart(updateCart);
            ReportSale updateReport = reportSaleService.findOne(LocalDate.now(), email);
            updateReport.setPayment(updateCart.getQuantity()  * updateCart.getRecipes().getPrice());
            reportSaleService.addReportSale(updateReport);
            return "redirect:/view_cart";
        }
        return "redirect:/view_cart";
    }


}
