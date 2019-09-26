import java.io.*;
import java.util.*;

//Programming Project of Inf421: Optimal Tree Labeling Problem
public class OTL {
	int N;
	int L;
	final int nb_lettre = 26;
	final int Max = Integer.MAX_VALUE-2;
	boolean[] flag = new boolean[nb_lettre];//
	int[][][] table = new int[nb_lettre][][];
	DoubleList leaves;
	HashMap<Integer,HashSet<Integer>> tree;
	
	public OTL(String filename) {
		Arrays.fill(flag, false);//
		this.leaves=new DoubleList();
		this.tree=new HashMap<Integer, HashSet<Integer>>();
		try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String lines = in.readLine();
            String[] units = lines.split(" ");
            this.N = Integer.parseInt(units[0]);
            this.L = Integer.parseInt(units[1]);
            
            for(int i=0;i<this.nb_lettre;i++) 
            	this.table[i] = new int[N][2];
            
            for(int j=0;j < N-1;j++) {
            	units = in.readLine().split(" ");
            	int index1 = Integer.parseInt(units[0]);
            	int index2 = Integer.parseInt(units[1]);
            	if(!tree.containsKey(index1)){
            		tree.put(index1,new HashSet<Integer>());
            	}
            	if(!tree.containsKey(Integer.parseInt(units[1]))){
            		tree.put(index2,new HashSet<Integer>());
            	}
            		tree.get(index1).add(index2);
            		tree.get(index2).add(index1);
            }
            
            for(int i=0;i<L;i++) {
            	units = in.readLine().split(" ");
            	char[] labels = units[1].toCharArray();
            	this.leaves.add(Integer.parseInt(units[0]));
            	// initialize all the leafs with label empty set
                for(int j=0;j<this.nb_lettre;j++) {
            		// cost of empty set is 0
                     this.table[j][Integer.parseInt(units[0])-1][1] = 0;
                     // cost of unempty set is MAX
                     this.table[j][Integer.parseInt(units[0])-1][0] = this.Max;
                }
                
            	if(labels[0]!='$') {
            		 for(int j=0;j<labels.length;j++) {
            			int num = ((int)labels[j])-65;
            			this.flag[num] = true;  //
            			// cost of empty set is MAX
            			this.table[num][Integer.parseInt(units[0])-1][1] = this.Max;
            			// cost of unempty set is 0
                      	this.table[num][Integer.parseInt(units[0])-1][0] = 0;
            		 }
            	}
            }
            in.close();
        } catch (IOException e) {
        }
	}

	public int min_weight() {
		for(int i:tree.keySet()) {
			if(tree.get(i).size()==1&&!leaves.contains(i)) {
				leaves.add(i);
			}
		}
	
		int res = 0;
		int root = 0;

		while(!leaves.isEmpty()) {
			int j=leaves.poll();
			if(leaves.isEmpty()) {
				root=j;
				break;
			}
			int father = 0;
			for(int i: tree.get(j)) {
				father = i;
				break;
			}
			tree.get(j).remove(father);
			for (int i = 0; i < this.nb_lettre; i++) {
				if (this.flag[i]) {//
					// cost of father labeling empty set is
					       //min(cost of son labeling empty set, cost of son labeling letter + 1)
					this.table[i][father-1][0] += Math.min(this.table[i][j-1][0], this.table[i][j-1][1] + 1);
					// cost of father labeling letter is
					      //min(cost of son labeling empty set + 1, cost of son labeling letter)
					this.table[i][father-1][1] += Math.min(this.table[i][j-1][0] + 1, this.table[i][j-1][1]);
				}
			}
			// update this tree, delete the leaf just treated, delete the edge between this leaf and his father.
			//Verify if this father is a new leaf
			tree.remove(j);
			tree.get(father).remove(j);
			
			if(tree.get(father).size()==1) {
				leaves.add(father);
			}
			
		}
		for(int i=0;i<nb_lettre;i++) {
			if(this.flag[i])
				res += Math.min(this.table[i][root-1][0], this.table[i][root-1][1]);
		}
		return res;
	}
	
	public static void main(String[] args) {
		for(int num=0;num<=10;num++) {
			long startTime = System.nanoTime();
			OTL label = new OTL("labeling."+ num +".in");
			System.out.println("Optimal solution of labeling."+ num+" : " +label.min_weight()+
					"  Time cost : "+ Long.toString((System.nanoTime()-startTime)/1000000)+" milliseconds");
		}
	}
	        
}
