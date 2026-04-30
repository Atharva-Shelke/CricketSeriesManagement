package com.cricketSeries.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cricketSeries.model.Series;
import com.cricketSeries.dto.MatchDTO;
import com.cricketSeries.dto.SeriesRequestDTO;
import com.cricketSeries.model.Match;
import com.cricketSeries.service.CricketService;

@RestController
@RequestMapping("/cricket")
public class CricketController {

	@Autowired
	private CricketService service;

	@PostMapping("/series")
	public ResponseEntity<String> create(@RequestBody SeriesRequestDTO series) {
		service.createSeries(series);
		return ResponseEntity.status(HttpStatus.CREATED).body("Created");
	}

	@GetMapping("/series")
	public List<Series> getAllSeries() {
		return service.getAllSeries();
	}

	@GetMapping("/series")
	public Series getSeriesById(Long id) {
		return service.getSeriesById(id);
	}

	@DeleteMapping("/series")
	public String deleteSeries(Long id) {
		service.deleteSeries(id);
		return "Series deleted with id: " + id;
	}

	@PutMapping("/series")
	public String updateSeries(Series series) {
		service.updateSeries(series);
		return "Series updated with id: " + series.getId();
	}

	@PostMapping("/match")
	public ResponseEntity<String> createMatch(Long id, @RequestBody MatchDTO match) {
		service.createMatch(id, match);
		return ResponseEntity.status(HttpStatus.CREATED).body("Created");
	}

	@GetMapping("/match")
	public List<Match> getAllMatchesBySeriesId(Long id) {
		return service.getAllMatchesBySeriesId(id);
	}

	@GetMapping("/match")
	public Match getMatchById(Long id) {
		return service.getMatchById(id);
	}

	@DeleteMapping("/match")
	public String deleteMatch(Long id) {
		service.deleteMatch(id);
		return "Match deleted with id: " + id;
	}

	@PutMapping("/match")
	public String updateMatch(Match match) {
		service.updateMatch(match);
		return "Match updated with id: " + match.getId();
	}

}
