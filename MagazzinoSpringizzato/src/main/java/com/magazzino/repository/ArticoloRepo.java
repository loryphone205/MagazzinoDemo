package com.magazzino.repository;

import org.springframework.data.repository.CrudRepository;

import com.magazzino.model.Articolo;

public interface ArticoloRepo extends CrudRepository<Articolo, Integer>{

}
