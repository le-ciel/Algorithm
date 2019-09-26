package trees;

import java.util.*;

import graph.*;

public class SpanningTree {
    
    public static Collection<Edge> kruskal(UnionFind u, EuclideanGraph g){
    	Comparator<Edge> c = new EdgeComparator();
    	Collection<Edge> res = new LinkedList<Edge>();
    	List<Edge> col = g.getAllEdges();
    	Collections.sort(col, c);
    	
    	for(Edge e : col) {
    		if(!u.find(e.source).equals(u.find(e.target))) {
    			res.add(e);
    			u.union(e.source, e.target);
    		}
    	}
    	return res;
    }
    
    public static Collection<Collection<Edge>> kruskal(EuclideanGraph g){
    	HashMap<Place,Collection<Edge>> edgelist = new HashMap<Place,Collection<Edge>>();
    	UnionFind u = new UnionFind();
    	Collection<Edge> res = kruskal(u,g);
    	
    	for(Edge e : res) {
    		if(edgelist.get(u.find(e.source))==null) 
    			edgelist.put(u.find(e.source), new LinkedList<Edge>()) ; 
    		edgelist.get(u.find(e.source)).add(e);
    	}
    	return edgelist.values();
    }
    
    public static Collection<Edge> primTree(HashSet<Place> nonVisited, Place start, EuclideanGraph g){
    	PriorityQueue<Edge> q = new PriorityQueue<Edge>(new EdgeComparator());
    	Collection<Edge> res = new LinkedList<Edge>();
    	q.addAll(g.edgesOut(start));
    	nonVisited.remove(start);
    	while(!q.isEmpty()) {
    		Edge a = q.poll();
    		if(nonVisited.contains(a.target)) {
    			res.add(a);
    			q.addAll(g.edgesOut(a.target));
    			nonVisited.remove(a.target);
    		}
    	}
    	return res;
    }
    
    public static Collection<Collection<Edge>> primForest(EuclideanGraph g){
    	HashSet<Collection<Edge>> edgelist = new HashSet<Collection<Edge>>();
    	HashSet<Place> nonVisited = new HashSet<Place>(g.places());
    	
    	while(!nonVisited.isEmpty()) {
    		Collection<Edge> tree = primTree(nonVisited,take(nonVisited),g);
    		if(!tree.isEmpty())
    			edgelist.add(tree);
    	}
    	
    	return edgelist;
    }
    
    public static Place take(HashSet<Place> nonVisited) {
    	Iterator<Place> it = nonVisited.iterator();
        return it.next();
    }
   
}
