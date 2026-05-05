package com.cricketSeries.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.cricketSeries.dto.MatchResponseDTO;
import com.cricketSeries.dto.SeriesResponseDTO;
import com.cricketSeries.model.Series;
import com.cricketSeries.utility.SqlLoader;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Repository
public class SeriesDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private SqlLoader sql;

	private final ObjectMapper mapper = new ObjectMapper();

	public long insertSeries(Series series) {
		String query = sql.get("insert_series");

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", series.getName())
				.addValue("location", series.getLocation()).addValue("startDate", series.getStartDate())
				.addValue("endDate", series.getEndDate());

		KeyHolder keyHolder = new GeneratedKeyHolder();

		int rows = jdbcTemplate.update(query, params, keyHolder, new String[] { "id" });

		if (rows == 0) {
			throw new RuntimeException("Series not inserted.");
		}

		return keyHolder.getKey().longValue();
	}

	public List<Series> getAllSeries() {
		return jdbcTemplate.query(sql.get("get_all_series"), (rs, rowNum) -> {
			Series series = new Series();
			series.setId(rs.getLong("id"));
			series.setName(rs.getString("name"));
			series.setLocation(rs.getString("location"));
			series.setStartDate(rs.getDate("start_date").toLocalDate());
			series.setEndDate(rs.getDate("end_date").toLocalDate());
			return series;
		});

	}

	public List<SeriesResponseDTO> getAllSeriesWithMatches() {

		return jdbcTemplate.query(sql.get("get_all_series_with_matches"), (rs, rowNum) -> {
			SeriesResponseDTO series = new SeriesResponseDTO();
			series.setId(rs.getLong("id"));
			series.setName(rs.getString("name"));
			series.setLocation(rs.getString("location"));
			series.setStartDate(rs.getDate("start_date").toLocalDate());
			series.setEndDate(rs.getDate("end_date").toLocalDate());

			try {
				String matchesJson = rs.getString("matches");

				List<MatchResponseDTO> matches = mapper.readValue(matchesJson,
						new TypeReference<List<MatchResponseDTO>>() {
						});
				series.setMatches(matches);

			} catch (Exception ex) {
				throw new RuntimeException("Error parsing matches JSON", ex);
			}

			return series;
		});

	}

	public Series getSeriesById(long seriesId) {
		try {

			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", seriesId);

			return jdbcTemplate.queryForObject(sql.get("get_series_by_id"), params, (rs, rowNum) -> {
				Series series = new Series();
				series.setId(rs.getLong("id"));
				series.setName(rs.getString("name"));
				series.setLocation(rs.getString("location"));
				series.setStartDate(rs.getDate("start_date").toLocalDate());
				series.setEndDate(rs.getDate("end_date").toLocalDate());
				return series;
			});
		} catch (EmptyResultDataAccessException ex) {
			throw new RuntimeException("Series not found with id: " + seriesId);
		}
	}

	public SeriesResponseDTO getSeriesByIdWithMatches(long seriesId) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", seriesId);

			return jdbcTemplate.queryForObject(sql.get("get_series_by_id_with_matches"), params, (rs, rowNum) -> {
				SeriesResponseDTO series = new SeriesResponseDTO();
				series.setId(rs.getLong("id"));
				series.setName(rs.getString("name"));
				series.setLocation(rs.getString("location"));
				series.setStartDate(rs.getDate("start_date").toLocalDate());
				series.setEndDate(rs.getDate("end_date").toLocalDate());

				try {
					String matchesJson = rs.getString("matches");

					if (matchesJson != null) {
						List<MatchResponseDTO> matches = mapper.readValue(matchesJson,
								new TypeReference<List<MatchResponseDTO>>() {
								});
						series.setMatches(matches);
					}
				} catch (Exception ex) {
					throw new RuntimeException("Error parsing matches JSON", ex);
				}
				return series;
			});
		} catch (EmptyResultDataAccessException ex) {
			throw new RuntimeException("Series not found with id: " + seriesId);
		}
	}

	public int deleteSeries(long seriesId) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", seriesId);
			return jdbcTemplate.update(sql.get("delete_series"), params);
		} catch (RuntimeException ex) {
			throw new RuntimeException("Series not found with id: " + seriesId);
		}
	}

	public int updateSeries(Series series) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", series.getName())
					.addValue("location", series.getLocation()).addValue("startDate", series.getStartDate())
					.addValue("endDate", series.getEndDate()).addValue("id", series.getId());

			return jdbcTemplate.update(sql.get("update_series"), params);
		} catch (RuntimeException ex) {
			throw new RuntimeException("Series not found with id: " + series.getId());
		}
	}
}