
/* ********************************************************************************
 * Project: Create a Recipe Project Using Spring/Spring Boot
 * Assignment: 1
 * Author(s): Wynne Tran
 * Student Number: 101161665
 * Date: Nov 4 2021
 * Description:  this page is a controller that displays all recipes, we can delete recipes of current user,
 and current user can like recipes from other users, then view favorite recipes on current user's profile page.
 ******************************************************************************** */

package com.example.assignment.controller;

import com.example.assignment.model.Favorite;
import com.example.assignment.model.Recipes;
import com.example.assignment.services.FavoriteService;
import com.example.assignment.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
public class viewRecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private FavoriteService favoriteService;


    @GetMapping("/viewrecipe")
    public  String viewRecipe(Model model, HttpSession session){
        String email = (String)session.getAttribute("email");
        List<Recipes> fixLike = recipeService.findAll();
        for(Recipes recipe  : fixLike){
                if(Objects.equals(recipe.getUser().getEmail(), email)){
                    recipe.setFavorite_like("1");
                }
        }
        model.addAttribute("recipes", recipeService.findAll());
        return "viewrecipe";
    }

    @PostMapping("/viewrecipe")
    public  String addToFavorite(Model model, HttpSession session, @RequestParam("id") Long id){
        String email = (String)session.getAttribute("email");
        Favorite newFavorite = new Favorite(email, id, recipeService.findOne(id));
        recipeService.findOne(id).setFavorite_like("1");
        favoriteService.addToFavorite(newFavorite);
        model.addAttribute("recipes", recipeService.findAll());
        return "redirect:/viewrecipe";
    }

    @PostMapping("/deleteRecipe")
    public  String deleteRecipe(@RequestParam("id") Long id){
        recipeService.deleteRecipe(id);
        return "redirect:/viewrecipe";
    }

    @PostMapping("/deleteFavRecipe")
    public  String deleteFavRecipe(@RequestParam("id") Long id, HttpSession session){
        System.out.println(id);
        String email = (String)session.getAttribute("email");
        int favId = favoriteService.findFavId(id, email).getId();
        System.out.println("favId: " + favId);
        recipeService.findOne(id).setFavorite_like("0");
        favoriteService.deleteFavorite(favId);
        return "redirect:/viewrecipe";
    }
}
