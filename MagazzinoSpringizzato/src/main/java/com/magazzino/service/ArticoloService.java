package com.magazzino.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magazzino.model.Articolo;
import com.magazzino.repository.ArticoloRepo;

@Service
public class ArticoloService {

	@Autowired
	ArticoloRepo articoloRepo;

	// metodo che controlla se un articolo è valido
	private boolean isValid(Articolo articolo) {

		if (articolo.getNome().equals("") || articolo.getPrezzo() < 0 || articolo.getQualita() < 0
				|| articolo.getDataDiScadenza() < 0 || articolo.equals(null))
			return false;
		else
			return true;

	}

	// get find all
	public Iterable<Articolo> findAll() {
		return articoloRepo.findAll();
	}

	// find by id
	public Optional<Articolo> findById(Integer id) {
		return articoloRepo.findById(id);
	}

	// salva con controllo
	public String add(Articolo articolo) {
		if (isValid(articolo)) {
			articoloRepo.save(articolo);
			return "Articolo Aggiunto";
		}
		return "Articolo Non Aggiunto";
	}

	// update tramite id
	public String update(Articolo articolo, Integer id) {
		if (!isValid(articolo)) {
			return "Articolo vuoto";
		}

		if (articoloRepo.findById(id).isPresent()) {
			articoloRepo.save(articolo);
		} else {
			return "Articolo non presente all'Id";
		}

		return "Articolo Modificato";

	}

	// delete tramite id
	public String deleteById(Integer id) {
		if (articoloRepo.findById(id).isPresent()) {
			articoloRepo.deleteById(id);
		} else {
			return "Articolo non presente all'Id";
		}

		return "Articolo Eliminato";
	}

	/**
	 * 
	 * @param articolo articolo da far avanazare di giornate
	 * @return articolo avanzato di giornata
	 */
	private Articolo elaborazioneAvanzamento(Articolo articolo) {

		final boolean scaduto = articolo.getDataDiScadenza() == 0;
		final int differenzaDiQualita;
		final int differenzaDiScadenza;
		final int moltiplicatoreDiQualita;

		final String categoria = articolo.getCategoria();

		int dataDiScadenza = articolo.getDataDiScadenza();
		int qualita = articolo.getQualita();

		// controllo la categoria
		switch (articolo.getCategoria()) {
		case "prezioso":
			differenzaDiQualita = 0;
			differenzaDiScadenza = 0;
			break;

		case "biglietto":
			// biglietto segue le indicazioni fornite dalla richiesta

			if (!scaduto) {
				if (articolo.getDataDiScadenza() == 1) {
					// dato che il massimo di qualità possibile è 50,
					// lo sottraiamo e avremo due outcome:
					// 0 se la qualità è 50,
					// numero negativo se la qualità < 50
					// in entrambi i casi, dato che la qualità del biglietto non può superare 50, la
					// qualità verrà impostata a 0
					differenzaDiQualita = -50;
				} else if (dataDiScadenza < 7) {
					differenzaDiQualita = 3;
				} else if (dataDiScadenza < 12) {
					differenzaDiQualita = 2;
				} else {
					differenzaDiQualita = 1;
				}
				differenzaDiScadenza = -1;
			} else {
				differenzaDiScadenza = 0;
				differenzaDiQualita = 0;
			}
			break;

		case "invecchiati":
			// invecchiati deve aumentare di qualità fino a 50
			differenzaDiScadenza = 0;
			differenzaDiQualita = 1;
			break;

		default:
			// se l'articolo è scaduto, allora decrementa di 1 o 2 in base alla sua scadenza
			differenzaDiQualita = !scaduto ? -1 : -2;
			if (!scaduto)
				differenzaDiScadenza = -1;
			else
				differenzaDiScadenza = 0;
			break;
		}
		if (categoria.equals("congiurati"))
			moltiplicatoreDiQualita = 2;
		else
			moltiplicatoreDiQualita = 1;

		dataDiScadenza += differenzaDiScadenza;

		if (!categoria.equals("prezioso"))
			qualita = Math.min(50, Math.max(0, qualita + (moltiplicatoreDiQualita * differenzaDiQualita)));

		articolo.setDataDiScadenza(dataDiScadenza);
		articolo.setQualita(qualita);
		return articolo;

	}

	// metodo avanzamentoDiGiornata
	public String avanzamentoDiGiornata() {

		// prendiamo i dati del database
		Iterable<Articolo> inventario = this.findAll();

		// adesso andiamo ad aggiornare gli articoli presenti in base alla loro
		// categoria
		for (Articolo articolo : inventario) {
			// chiamo l'elaborazione
			Articolo articoloElaborato = elaborazioneAvanzamento(articolo);
			articoloRepo.save(articoloElaborato);
		}
		return "Avanazato di giornata";
	}
}
