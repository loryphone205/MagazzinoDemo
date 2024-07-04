package com.magazzino;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.magazzino.model.Articolo;
import com.magazzino.service.ArticoloService;

import jakarta.annotation.Resource;

@SpringBootTest
class MagazzinoSpringizzatoApplicationTests {

	@Resource
	private ArticoloService articoloService;
	
	/*
	 * Questo test deve salvare un articolo all'interno del nostro database
	 */
	@Test
	public void shouldSaveArticolo() {
		Articolo a = new Articolo();
		a.setNome("Luca");
		articoloService.add(a);
		
		Articolo a2 = articoloService.findById(1).get();
		
		assertEquals("Luca", a2.getNome());
		
	}
	
}
