
/* ********************************************************************************
 * Project: Create a Recipe Project Using Spring/Spring Boot
 * Assignment: 1 & 2
 * Author(s): Wynne Tran
 * Student Number: 101161665
 * Date: Nov 4 2021
 * Description:  this page is a repository database of Shopping Cart
 ******************************************************************************** */

package com.example.assignment.repositories;

import com.example.assignment.model.Shopping_Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingRepository  extends JpaRepository<Shopping_Cart, Integer> {

    List<Shopping_Cart> findByUserEmail(String email);

    @Query("SELECT c FROM Shopping_Cart c WHERE (c.recipe_id = :id and c.user_email = :email)")
    Shopping_Cart findShopping_CartByRecipe_idAndUser_email(Long id, String email);

    Shopping_Cart findByRecipes_id(Long id);

}
