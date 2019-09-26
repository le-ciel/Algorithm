import java.io.*;

public class DP {
	int N;
	int L;
	final int nb_lettre = 26;
	final int Max = Integer.MAX_VALUE-2;
	int[][][] table = new int[nb_lettre][][];
	
	public DP(String filename) {
		try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String lines = in.readLine();
            String[] units = lines.split(" ");
            this.N = Integer.parseInt(units[0]);
            this.L = Integer.parseInt(units[1]);
            
            for(int i=0;i<this.nb_lettre;i++) 
            	this.table[i] = new int[N][3];
            
            for(int j=0;j < N-1;j++) {
            	units = in.readLine().split(" ");
            	for(int i=0;i<this.nb_lettre;i++) {
            		this.table[i][Integer.parseInt(units[1])-1][0] = Integer.parseInt(units[0])-1;
            	}
            }
            
            for(int i=0;i<L;i++) {
            	units = in.readLine().split(" ");
            	char[] labels = units[1].toCharArray();
            	
            	// initialize all the leafs with label empty set
                for(int j=0;j<this.nb_lettre;j++) {
            		// cost of empty set is 0
                     this.table[j][Integer.parseInt(units[0])-1][2] = 0;
                     // cost of unempty set is MAX
                     this.table[j][Integer.parseInt(units[0])-1][1] = this.Max;
                }
                
            	if(labels[0]!='$') {
            		 for(int j=0;j<labels.length;j++) {
            			int num = ((int)labels[j])-65;
            			// cost of empty set is MAX
            			this.table[num][Integer.parseInt(units[0])-1][2] = this.Max;
            			// cost of unempty set is 0
                      	this.table[num][Integer.parseInt(units[0])-1][1] = 0;
            		 }
            	}
            }
            
            in.close();
        } catch (IOException e) {
        }
	}
	
	
	public int min_weight() {
		int res = 0;
		for(int i=0;i<this.nb_lettre;i++) {

			for(int j=this.N-1;j>0;j--) {
				// cost of father labeling empty set is 
				// min(cost of son labeling empty set, cost of son labeling letter + 1)
				this.table[i][this.table[i][j][0]][1] += 
						Math.min(this.table[i][j][1], this.table[i][j][2]+1);
				// cost of father labeling letter is 
				// min(cost of son labeling empty set + 1, cost of son labeling letter)
				this.table[i][this.table[i][j][0]][2] += 
						Math.min(this.table[i][j][1]+1, this.table[i][j][2]);
			}
			res += Math.min(this.table[i][0][1],this.table[i][0][2]);
		}
		return res;
	}
	
	
	public static void main(String[] args) {
		for(int num=1;num<=10;num++) {
			long startTime = System.nanoTime();
			DP label = new DP("labeling."+ num +".in");
			int result = label.min_weight();
			System.out.println("Optimal solution of labeling."+ num+" : " +result+
					"  Time cost : "+ Long.toString((System.nanoTime()-startTime)/1000000)+" milliseconds");
			
		}
	}

}
