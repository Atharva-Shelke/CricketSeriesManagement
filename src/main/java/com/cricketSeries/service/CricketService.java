package com.cricketSeries.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cricketSeries.dao.MatchDAO;
import com.cricketSeries.dao.SeriesDAO;
import com.cricketSeries.dto.MatchDTO;
import com.cricketSeries.dto.SeriesRequestDTO;
import com.cricketSeries.model.Match;
import com.cricketSeries.model.Series;

@Service
public class CricketService {

	@Autowired
	private SeriesDAO seriesDao;
	private MatchDAO matchDao;

	public void createSeries(SeriesRequestDTO series) {
		Long id = seriesDao.insertSeries(series);

//        for (Match m : series.getMatches()) {
//            dao.insertMatch(id, m);
//        }
	}

	public List<Series> getAllSeries() {
		return seriesDao.getAllSeries();
	}

	public Series getSeriesById(Long id) {
		return seriesDao.getSeriesById(id);
	}

	public void deleteSeries(Long id) {
		seriesDao.deleteSeries(id);
	}

	public void updateSeries(Series series) {
		seriesDao.updateSeries(series);
	}

	public void createMatch(Long seriesId, MatchDTO match) {
		matchDao.insertMatch(seriesId, match);
	}

	public List<Match> getAllMatchesBySeriesId(Long id) {
		return matchDao.getAllMatchesBySeriesId(id);
	}

	public Match getMatchById(Long id) {
		return matchDao.getMatchById(id);
	}

	public void deleteMatch(Long id) {
		matchDao.deleteMatch(id);
	}

	public void updateMatch(Match match) {
		matchDao.updateMatch(match);
	}
}
