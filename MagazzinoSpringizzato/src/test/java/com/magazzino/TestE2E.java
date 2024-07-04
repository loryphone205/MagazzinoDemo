package com.magazzino;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.magazzino.base.BaseSeleniumE2ETest;
import com.magazzino.model.Articolo;
import com.magazzino.repository.ArticoloRepo;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class TestE2E extends BaseSeleniumE2ETest {
	
	@Autowired
	ArticoloRepo articoloRepo;

	@Test
	void shouldCheckThatFirstItemInTableIsPresent() {
		Articolo a = new Articolo();
		a.setNome("Daniele Pasqualetti");
		a.setDataDiScadenza(9);
		a.setPrezzo(200);
		a.setCategoria("speciale");
		a.setQualita(9);
		
		a = articoloRepo.save(a);
		
		driver.navigate().refresh();
		
		Integer id = a.getId();
		// quesot test deve verificare che l'elemento nella prima posizione della
		// tabella abbia i seguenti campi:
		// nome: Daniele Pasqualetti
		// dataDiScadenza: 9
		// prezzo: 200
		// qualita: 9
		// categoria: speciale
		var nElenco = driver.findElement(By.id("nElenco")).getText();
		assertThat(nElenco).isEqualTo(id.toString());
		var nome = driver.findElement(By.id("nome")).getText();
		assertThat(nome).isEqualTo("Daniele Pasqualetti");
		var dataDiScadenza = driver.findElement(By.id("dataDiScadenza")).getText();
		assertThat(dataDiScadenza).isEqualTo("9");
		var prezzo = driver.findElement(By.id("prezzo")).getText();
		assertThat(prezzo).isEqualTo("€200,00");
		var qualita = driver.findElement(By.id("qualita")).getText();
		assertThat(qualita).isEqualTo("9");
		var categoria = driver.findElement(By.id("categoria")).getText();
		assertThat(categoria).isEqualTo("speciale");
	}

}
