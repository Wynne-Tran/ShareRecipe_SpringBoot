package com.example.assignment.services;

import com.example.assignment.model.Recipes;
import com.example.assignment.model.Users;
import com.example.assignment.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public void createRecipe(Recipes recipe, Users user){
        recipe.setUser(user);
        recipeRepository.save(recipe);
    }

    public List<Recipes> findByTitle(String title) {
        // TODO Auto-generated method stub
        return  recipeRepository.findByTitleLike("%"+title+"%");
    }

    public List<Recipes> findAllUser(Users user){return recipeRepository.findByUser(user);}

    public Recipes findOne(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipes> findAll(){
            return recipeRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }


}