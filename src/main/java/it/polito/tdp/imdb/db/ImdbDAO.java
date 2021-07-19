package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map <Integer, Director> idMap){
		String sql = "SELECT * FROM directors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				idMap.put(director.getId(), director);
				}
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	
	public List<Director> listaRegistiAnno (int anno, Map <Integer, Director> idMap) {
		String sql = "SELECT Distinct directors.* "
				+ "FROM directors , movies_directors , movies "
				+ "WHERE directors.id = movies_directors.director_id AND movies_directors.movie_id = movies.id "
				+ "AND movies.year = ? ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("id")))
				
				result.add(idMap.get(res.getInt("id")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Adiacenza> getAdiacenze (int anno, Map <Integer, Director> idMap) {
		String sql = "SELECT  d1.id AS id1, d2.id AS id2, (COUNT(r1.actor_id)) AS peso "
				+ "FROM directors d1, directors d2, movies_directors md1, movies m1, roles r1, movies_directors md2, movies m2, roles r2 "
				+ "WHERE d1.id <> d2.id AND d1.id=md1.director_id AND md1.movie_id=m1.id AND m1.id=r1.movie_id AND "
				+ "d2.id=md2.director_id AND md2.movie_id=m2.id AND m2.id=r2.movie_id AND r1.actor_id=r2.actor_id "
				+ "AND m1.year=m2.year AND m1.year= ? "
				+ "GROUP BY d1.id, d2.id " ;
				
		List <Adiacenza> result = new ArrayList<Adiacenza> ();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					result.add(new Adiacenza(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), res.getInt("peso")));
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
