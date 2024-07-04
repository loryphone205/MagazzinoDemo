package com.magazzino.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.magazzino.model.Articolo;
import com.magazzino.service.ArticoloService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/articolo")
public class ArticoloController {

	@Autowired
	ArticoloService articoloService;

	// get all di articoli
	@GetMapping(path = "/all")
	public Iterable<Articolo> getAll() {
		return articoloService.findAll();
	}

	// get in base all'id
	@GetMapping(path = "/{id}")
	public Optional<Articolo> getById(@PathVariable Integer id) {
		return articoloService.findById(id);
	}

	// post
	@PostMapping(path = "/add")
	public String add(@RequestBody Articolo articolo) {
		return articoloService.add(articolo);
	}

	// put
	@PutMapping(path = "/add/{id}")
	public String updateById(@RequestBody Articolo articolo, @PathVariable Integer id) {
		return articoloService.update(articolo, id);
	}

	// deleteById
	@DeleteMapping(path = "/deleteById/{id}")
	public String deleteById(@PathVariable Integer id) {
		return articoloService.deleteById(id);
	}
	
	// avanzaDiGiornata
	@PutMapping(path = "/avanzaDiGiornata")
	public String avanzaDiGiornata() {
		return articoloService.avanzamentoDiGiornata();
	}

}
