package com.magazzino;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

import com.magazzino.model.Articolo;
import com.magazzino.repository.ArticoloRepo;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class TestRestAvanzamento {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ArticoloRepo articoloRepo;

	@BeforeEach
	private void tearDown() {
		articoloRepo.deleteAll();
	};

	/*
	 * Questo test verifica che la chiamata mi restituisca un iterable che comprende
	 * tutti gli elementi del database
	 * ID: TC001
	 */
	@Test
	public void shouldTestThatWeGetAnIterableWithAllArticolo() {
		// creo due articoli
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		Articolo a2 = new Articolo();
		a2.setNome("Lorenzo");
		a2.setDataDiScadenza(2);
		a2.setPrezzo(10);
		a2.setQualita(12);
		a2.setCategoria("biglietti");

		articoloRepo.save(a1);
		articoloRepo.save(a2);

		String url = "http://localhost:" + port + "/articolo/all";

		ResponseEntity<Iterable<Articolo>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<Iterable<Articolo>>() {
				});

		ArrayList<Articolo> inventario = new ArrayList<>();

		for (Articolo articolo : response.getBody()) {
			inventario.add(articolo);
		}

		assertEquals(2, inventario.size());

	}

	/*
	 * Questo test verifica che il metodo getById restituisca il secondo elemento
	 * del database
	 * ID: TC002
	 */
	@Test
	public void shouldReturnSecondArticoloFromDatabase() {
		// creo due articoli
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		Articolo a2 = new Articolo();
		a2.setNome("Lorenzo");
		a2.setDataDiScadenza(2);
		a2.setPrezzo(10);
		a2.setQualita(12);
		a2.setCategoria("biglietti");

		Integer id = articoloRepo.save(a2).getId();

		String url = "http://localhost:" + port + "/articolo/" + id;

		articoloRepo.save(a1);

		ResponseEntity<Articolo> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<Articolo>() {
				});

		assertEquals("Lorenzo", response.getBody().getNome());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/*
	 * Questo test verifica che l'elemento venga aggiunto nel database grazie alla
	 * POST
	 * ID: TC003
	 */
	@Test
	public void shouldAddArticolo() {
		String url = "http://localhost:" + port + "/articolo/add";

		// creo un articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		ResponseEntity<String> response = restTemplate.postForEntity(url, a1, String.class);

		assertEquals("Articolo Aggiunto", response.getBody());
	}

	/*
	 * Questo test verifica che il nome di un aritcolo venga modificato
	 * correttamente all'id 1
	 * ID: TC004
	 */
	@Test
	public void shouldEditArticoloNome() {
		// creo un articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		Integer id = articoloRepo.save(a1).getId();

		String url = "http://localhost:" + port + "/articolo/add/" + id;

		Articolo a2 = new Articolo();
		a2.setId(id);
		a2.setNome("Lorenzo");
		a2.setDataDiScadenza(45);
		a2.setPrezzo(500);
		a2.setQualita(12);
		a2.setCategoria("boglietto");

		restTemplate.put(url, a2);

		assertEquals(a2.getNome(), articoloRepo.findById(id).get().getNome());

	}

	/*
	 * Questo test verifica che la delete funzioni correttamente
	 * ID: TC005
	 */
	@Test
	public void shouldDeleteArticolo() {
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		Integer id = articoloRepo.save(a1).getId();

		String url = "http://localhost:" + port + "/articolo/deleteById/" + id;

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertEquals("Articolo Eliminato", response.getBody());
	}

	/*
	 * Questo test verifica che la giornata passi per tutti gli articoli di 1
	 * ID: TC006
	 */
	@Test
	public void shouldAvanzaDiGiornataBy1() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(45);
		a1.setPrezzo(500);
		a1.setQualita(12);
		a1.setCategoria("invecchiati");

		Articolo a2 = new Articolo();
		a2.setNome("Luca");
		a2.setDataDiScadenza(45);
		a2.setPrezzo(500);
		a2.setQualita(12);
		a2.setCategoria("congiurati");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();
		Integer id2 = articoloRepo.save(a2).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// a1 è invecchiato, quindi la qualità deve salire a 13
		assertEquals(13, articoloRepo.findById(id1).get().getQualita());

		// a2 è congiurati, quindi la qualità scenderà di 2 e la dataDiScadenza di 1
		assertEquals(10, articoloRepo.findById(id2).get().getQualita());
		assertEquals(44, articoloRepo.findById(id2).get().getDataDiScadenza());

		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che all'avanzare di un giorno, sia qualità che
	 * dataDiScadenza diminuiscano di 1 Presupponendo che qualità e scadenza siano
	 * entrambi > 0
	 */
	@Test
	public void shouldLowerQualitaAndDataDiScadenzaBy1() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(30);
		a1.setPrezzo(500);
		a1.setQualita(30);
		a1.setCategoria("owo");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che qualita e dataDiScadenza decremntino di 1
		assertEquals(29, articoloRepo.findById(id1).get().getQualita());
		assertEquals(29, articoloRepo.findById(id1).get().getDataDiScadenza());

		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che una volta raggiunta qualita uguale a 0 non sfoci in
	 * numeri negativi
	 */
	@Test
	public void shouldNotGetQualitaToNegativeNumbers() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(30);
		a1.setPrezzo(500);
		a1.setQualita(0);
		a1.setCategoria("owo");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che qualita non decrementi sotto lo 0
		assertEquals(0, articoloRepo.findById(id1).get().getQualita());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

	}

	/**
	 * Questo test verifica che una volta raggiunta dataDiScadenza uguale a 0 non
	 * sfoci in numeri negativi
	 */
	@Test
	public void shouldNotGetDataDiScadenzaToNegativeNumbers() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(0);
		a1.setPrezzo(500);
		a1.setQualita(10);
		a1.setCategoria("owo");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che dataDiScadenza non decrementi sotto lo 0
		assertEquals(0, articoloRepo.findById(id1).get().getDataDiScadenza());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che una volta raggiunta dataDiScadenza uguale a 0, la
	 * qualita si riduce di 2
	 */
	@Test
	public void shouldReduceQualityByTwoWhenDataDiScadenzaEqualsZero() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(0);
		a1.setPrezzo(500);
		a1.setQualita(10);
		a1.setCategoria("owo");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che dataDiScadenza non decrementi sotto lo 0
		assertEquals(8, articoloRepo.findById(id1).get().getQualita());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che la categoria venga gestita correttamente, ovvero: La
	 * qualità aumenta di 1 se l'articolo appartiene alla categoria "invecchiati"
	 */
	@Test
	public void shouldAdvanceInQuality() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(10);
		a1.setPrezzo(500);
		a1.setQualita(10);
		a1.setCategoria("invecchiati");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la dataDiScadenza non vari, ma la qualità aumenti di 1
		assertEquals(10, articoloRepo.findById(id1).get().getDataDiScadenza());
		assertEquals(11, articoloRepo.findById(id1).get().getQualita());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che la qualita non superi 50 partendo da un oggeto con
	 * qualita 49
	 */
	@Test
	public void shouldNotExceedQualitaBy50() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Luca");
		a1.setDataDiScadenza(10);
		a1.setPrezzo(500);
		a1.setQualita(49);
		a1.setCategoria("invecchiati");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la dataDiScadenza non vari, ma la qualità aumenti di 1 fino
		// a 50, invece di incrementare fino 51
		assertEquals(10, articoloRepo.findById(id1).get().getDataDiScadenza());
		assertEquals(50, articoloRepo.findById(id1).get().getQualita());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	/**
	 * Questo test verifica che l'articolo di categoria "prezioso" non cambi
	 * dataDiScadenza e qualita
	 */
	@Test
	public void shouldNotModifyDataDiScadenzaAndQualita() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Oro");
		a1.setDataDiScadenza(0);
		a1.setPrezzo(500);
		a1.setQualita(23);
		a1.setCategoria("prezioso");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la dataDiScadenza e qualita non variano.
		assertEquals(0, articoloRepo.findById(id1).get().getDataDiScadenza());
		assertEquals(23, articoloRepo.findById(id1).get().getQualita());
		assertEquals("Avanazato di giornata", response.getBody());
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

	}

	/**
	 * Questo test verifica che l'articolo di categoria biglietto rispetto le
	 * seguenti condizioni all'avanzamento della giornata: Se dataDiScadenza è
	 * maggiore di 10, qualita aumenta di 1 Se dataDiScadenza è strattamente minore
	 * di 10, qualita aumenta di 2 Se dataDiScadenza è strettamente minore di 5,
	 * qualita aumenta di 3 Se dataDiScadenza è ugualea a 0, qualita sarà 0;
	 */
	@Test
	public void shouldIncrementQualitaUntilDataDiScadenzaReachesZero() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Concerto Maneskin");
		a1.setDataDiScadenza(11);
		a1.setPrezzo(50.2);
		a1.setQualita(23);
		a1.setCategoria("biglietto");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 2, e dataDiScadenza diminuisce di 1
		assertEquals(23 + 2, articoloRepo.findById(id1).get().getQualita());
		assertEquals(10, articoloRepo.findById(id1).get().getDataDiScadenza());

		// avanziamo di giornata
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 6, e dataDiScadenza diminuisce di 3
		assertEquals(25 + 2 + 2 + 2, articoloRepo.findById(id1).get().getQualita());
		assertEquals(7, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 2, e dataDiScadenza diminuisce di 1
		assertEquals(31 + 2, articoloRepo.findById(id1).get().getQualita());
		assertEquals(6, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 3, e dataDiScadenza diminuisce di 1
		assertEquals(36, articoloRepo.findById(id1).get().getQualita());
		assertEquals(5, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 3, e dataDiScadenza diminuisce di 1
		assertEquals(36 + 3, articoloRepo.findById(id1).get().getQualita());
		assertEquals(4, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// qualita dovrebbe aumentare di 9, e dataDiScadenza diminuisce di 3
		assertEquals(39 + 3 + 3 + 3, articoloRepo.findById(id1).get().getQualita());
		assertEquals(4 - 3, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// entrambi i valori dovrebbero essere 0
		assertEquals(0, articoloRepo.findById(id1).get().getQualita());
		assertEquals(1 - 1, articoloRepo.findById(id1).get().getDataDiScadenza());

	}

	/*
	 * Questo test verifica che gli articoli di tipo "congiurati" la qualità
	 * decrementa al doppio della velocità
	 */
	@Test
	public void shouldDecreaseCongiuratiAtDoubleTheSpeed() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Uova");
		a1.setDataDiScadenza(3);
		a1.setPrezzo(12.21);
		a1.setQualita(49);
		a1.setCategoria("congiurati");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la qualità, dopo una giornata, decremeneti di 2
		assertEquals(47, articoloRepo.findById(id1).get().getQualita());
		assertEquals(2, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);
		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la dataDiScadenza decremnti fino a 0, e che la qualità sia
		// 43
		assertEquals(43, articoloRepo.findById(id1).get().getQualita());
		assertEquals(0, articoloRepo.findById(id1).get().getDataDiScadenza());

		response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		assertEquals(39, articoloRepo.findById(id1).get().getQualita());
		assertEquals(0, articoloRepo.findById(id1).get().getDataDiScadenza());

	}

	/*
	 * Questo test verifca che il biglietto non decrementi ulteriormente la
	 * dataDiScadenza oltre 0, e qualita rimane a 0
	 */
	@Test
	public void shouldNotGoBeyond0() {
		String url = "http://localhost:" + port + "/articolo/avanzaDiGiornata";

		// creo articolo
		Articolo a1 = new Articolo();
		a1.setNome("Uova");
		a1.setDataDiScadenza(0);
		a1.setPrezzo(12.21);
		a1.setQualita(0);
		a1.setCategoria("biglietto");

		// salviamo gli articoli
		Integer id1 = articoloRepo.save(a1).getId();

		// avanziamo di giornata
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

		// ci aspettiamo che la qualità, dopo una giornata, decremeneti di 2
		assertEquals(0, articoloRepo.findById(id1).get().getQualita());
		assertEquals(0, articoloRepo.findById(id1).get().getDataDiScadenza());
	}

}
