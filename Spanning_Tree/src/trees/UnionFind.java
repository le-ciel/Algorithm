package trees;

import graph.Place;

import java.util.HashMap;

// Q1

public class UnionFind {
	//parent relation, parent.put(src,dst) indicates that src points to dst
    private HashMap<Place,Place> parent;
    
    public UnionFind( ){
        this.parent = new HashMap<Place,Place>();
    }
    
    public Place find( Place src ){
    	Place p = this.parent.get(src);
    	if(p==null) return src; 
    	if(p.equals(src)) return p;
        Place r = this.find(p);
        this.parent.put(src, r);
        return r;
        
    }
    
    public void union( Place v0, Place v1 ){
        Place r0 = this.find(v0);
        Place r1 = this.find(v1);
        if(r0.equals(r1)) return;
        this.parent.put(r0, r1);
    }
}
