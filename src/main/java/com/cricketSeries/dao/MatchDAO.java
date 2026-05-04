package com.cricketSeries.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cricketSeries.model.Match;
import com.cricketSeries.utility.SqlLoader;

@Repository
public class MatchDAO {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private SqlLoader sql;

	public int insertMatch(Long seriesId, Match match) {
		String query = sql.get("insert_match");

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("series_id", seriesId)
				.addValue("team_a", match.getTeamA()).addValue("team_b", match.getTeamB())
				.addValue("match_date", match.getMatchDate()).addValue("venue", match.getVenue())
				.addValue("match_type", match.getMatchType());

		return jdbcTemplate.update(query, params);

	}

	public List<Match> getAllMatchesBySeriesId(Long id) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
			return jdbcTemplate.query(sql.get("get_all_matches_by_series_id"), params, (rs, rowNum) -> {
				Match match = new Match();
				match.setId(rs.getLong("id"));
				match.setTeamA(rs.getString("team_a"));
				match.setTeamB(rs.getString("team_b"));
				match.setMatchDate(rs.getDate("match_date").toLocalDate());
				match.setVenue(rs.getString("venue"));
				match.setMatchType(rs.getString("match_type"));
				return match;
			});
		} catch (RuntimeException ex) {
			throw new RuntimeException("Series not found with id: " + id);
		}

	}

	public Match getMatchById(Long id) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

			return jdbcTemplate.queryForObject(sql.get("get_match_by_id"), params, (rs, rowNum) -> {
				Match match = new Match();
				match.setId(id);
				match.setTeamA(rs.getString("team_a"));
				match.setTeamB(rs.getString("team_b"));
				match.setMatchDate(rs.getDate("match_date").toLocalDate());
				match.setVenue(rs.getString("venue"));
				match.setMatchType(rs.getString("match_type"));
				return match;
			});
		} catch (RuntimeException ex) {
			throw new RuntimeException("Match not found with id: " + id);
		}
	}

	public int deleteMatch(Long id) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
			return jdbcTemplate.update(sql.get("delete_match"), params);
		} catch (RuntimeException ex) {
			throw new RuntimeException("Match not found with id: " + id);
		}
	}

	public int updateMatch(Long id, Match match) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id)
					.addValue("team_a", match.getTeamA()).addValue("team_b", match.getTeamB())
					.addValue("match_date", Date.valueOf(match.getMatchDate())).addValue("venue", match.getVenue())
					.addValue("match_type", match.getMatchType());

			return jdbcTemplate.update(sql.get("update_match"), params);
		} catch (RuntimeException ex) {
			throw new RuntimeException("Match not found with id: " + match.getId());
		}
	}
}
