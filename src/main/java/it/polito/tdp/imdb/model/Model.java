package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	Graph <Director, DefaultWeightedEdge> grafo;
	ImdbDAO dao;
	Map <Integer, Director> idMap;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		this.dao.listAllDirectors(idMap);;
		
	}

	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.listaRegistiAnno(anno, idMap));
		for(Adiacenza a: dao.getAdiacenze(anno, idMap))
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
	}

	public int nVertici() {	
		return this.grafo.vertexSet().size();	
		
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public Collection <Director> directorsAnno() {
		
		return this.grafo.vertexSet();
	}
	
	public List<Vicini> registiAdiacenti (Director d) {
		List<Director> vicino = Graphs.neighborListOf(this.grafo, d);
		List<Vicini> result = new ArrayList<> ();
			for(Director v : vicino) {
				result.add(new Vicini(v, this.grafo.getEdgeWeight(this.grafo.getEdge(d, v))));
			}
				Collections.sort(result);
				return result;
		//			List <Director> temp = Graphs.neighborListOf(this.grafo, d);
//			return temp;
		

	}
}
