package com.magazzino.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.magazzino.model.Articolo;
import com.magazzino.repository.ArticoloRepo;
import com.magazzino.service.ArticoloService;

@Controller
public class ArticoloControllerNonRest {
	@Autowired
	ArticoloService articoloService;

	// prendiamo i dati dalla repo
	@Autowired
	ArticoloRepo articoloRepo;

	// get
	@GetMapping("/all")
	public String getAll(Model model) {
		model.addAttribute("inventario", articoloRepo.findAll());
		return "table";
	}

	@GetMapping("/avanzamento")
	public String avanzamentoDiGiornata(Model model) {
		articoloService.avanzamentoDiGiornata();
		return "avanzamento";
	}

	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("articolo", new Articolo());
		return "form";
	}

	@PostMapping("/add")
	public String aggiungiArticolo(@ModelAttribute Articolo articolo, Model model) {
		model.addAttribute("addOrNoAdd", articoloService.add(articolo));
		return "aggiunta";
	}
}
