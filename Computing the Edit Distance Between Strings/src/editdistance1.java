import java.util.*;

public class editdistance1 implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;
    int X;
    int[][] result;
    int m;
    int n;
    int mil;

    public editdistance1 (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    public int min(int a, int b, int c){
    	if (a<b){
    		if(a<c){
    			return a;
    		}
    		else {return c;}
    	}
    	else{
    		if(c<b){
    			return c;
    		}
    		else{return b;}
    	}
    }
    
    public int[] indicedirect(int a, int b){
    	int j;
    	int i;
    	if (m>n){
    		mil=m-n+X;
    		i= mil + a-b;
    		j=b;}
    	else{
    		mil=n-m+X;
    		i=a;
    		j=mil+b-a;
    	}
    	int[] result = new int [2];
    	result[0]=i;
    	result[1]=j;
    	return result;
    }
    
    public int[] indiceinverse(int i, int j){
    	int a;
    	int b;
    	if (m>n){
    		mil=m-n+X;
    		a=mil+i-j;
    		b=j;}
    	else{
    		mil=n-m+X;
    		b=mil+j-i;
    		a=i;
    	}
    	int[] result = new int [2];
    	result[0]=a;
    	result[1]=b;
    	System.out.print("i="+i+"  ; j="+j+"  ; a="+a+"   ; b="+b);
    	return result;
    }
    
    
  //m>n
    public int[][] getEditDistancea(String s1, String s2) {
    	n= s1.length();
	    m= s2.length();
	    mil=m-n+X;
		int hauteur=2*mil;
		int largeur=m;
		int[][] result= new int[hauteur+1][largeur+1];
		for (int b=0; b<=largeur; b++){
			for (int a=0; a<=hauteur; a++){
				if(a+b<mil){
					result[a][b]=MAX;
				}
				if(a+b>=mil+n){
					result[a][b]=MAX;
				}
				if(a+b==mil){
					result[a][b]=b*c_i;
				}
				if(mil<a+b && a+b<mil+n){
					if(b==0){
						result[a][b]=(a-mil)*c_d;
					}
					else{
						if (s1.charAt(a+b-mil-1)==s2.charAt(b-1)){
							result[a][b]=result[a][b-1];
						}
						else{
							if(a>0 && a<2*mil){
								result[a][b]=min(result[a][b-1]+c_r, result[a-1][b]+c_d, result[a+1][b-1]+c_i);
							}
							if (a==0){
								result[a][b]=min(result[a][b-1]+c_r, result[a][b-1]+c_r, result[a+1][b-1]+c_i);
							}
							else{
								result[a][b]=min(result[a][b-1]+c_r, result[a-1][b]+c_d, result[a][b-1]+c_r);
							}
						}
					}
				}
			}}
	    return result;
	}
    
    
    
    
 //n>=m
    public int[][] getEditDistanceb(String s1, String s2) {
    	    	n= s1.length();
    		    m= s2.length();
    		    System.out.print(s1.length());
    		    mil=n-m+X;
    			int hauteur=n;
    			int largeur=2*mil;
    			int[][] result= new int[hauteur+1][largeur+1];
    			for (int a=0; a<=hauteur; a++)
    				for (int b=0; b<=largeur; b++){
    					System.out.print(a); 
    					if (a+b<mil){
    						result[a][b]= MAX;
    					}
    					if (a+b==mil){
    						result[a][b]=a*c_d;
    					}
    					if(a+b>mil){
    						if(a==0){
    							result[a][b]=(b-mil)*c_i;
    						}
    						else{
    							   							
    							if  (a+b-mil-1<m){
    								if( s1.charAt(a-1)==s2.charAt(a+b-mil-1)){
    								result [a][b]= result [a-1][b];}
    								}
    							else{
    								if (b>0 && b<2*mil){
    									result[a][b]= min(result[a-1][b]+c_r, result[a][b-1]+c_i, result[a-1][b+1]+c_d);
    								}
    								if(b==0){
    									result[a][b]= min(result[a-1][b]+c_r, result[a-1][b]+c_r, result[a-1][b+1]+c_d);
    								}
    								else{
    									result[a][b]= min(result[a-1][b]+c_r, result[a][b-1]+c_i, result[a][b-1]+c_i);
    								}
    							}
    						}
   
    					}
    				}
    		    return result;
    		}
    
    public int[][] getEditDistanceDP(String s1, String s2) {
		int n= s1.length();
	    int m= s2.length();
	    if (m<n){
	    	int k= m-n;
	    	X=1;
	    	int dmnprec=-2;
	    	int dmn=-1;
	    	while (dmn!=dmnprec){
	    		X=X*2;
	    		int[][] d =getEditDistancea(s1,s2);
	    		dmnprec=dmn;
	    		dmn=d[k+X][m-1];
	    		result=d;}
	    	}
	    else {
	    	int k=n-m;
	    	X=1;
	    	int dmnprec=-2;
	    	int dmn=-1;
	    	while (dmn!=dmnprec){
	    		X=X*2;
	    		int[][] d =getEditDistanceb(s1,s2);
	    		dmnprec=dmn;
	    		dmn=d[n-1][k+X];
	    		result=d;}
	    }
	    return result;
	    }
		


	public List<String> getMinimalEditSequence(String s1, String s2) {
		LinkedList<String> ls = new LinkedList<>();
		int[][] d = this.getEditDistanceDP(s1, s2);

		int m = s1.length();
		int n = s2.length(); 

		while (m > 0 || n > 0) {
			if (m > 0 && n > 0 && s1.charAt(m - 1) == s2.charAt(n - 1) && d[m][n] == d[m - 1][n - 1]) {
				m--;
				n--;
			} else if (m > 0 && n > 0 && s1.charAt(m - 1) != s2.charAt(n - 1) && d[m][n] == d[m - 1][n - 1] + c_r) {
				ls.add("replace(" + (m - 1) + "," + s2.charAt(n - 1) + ")");
				m--;
				n--;
			} else if (m > 0 && d[m][n] == d[m - 1][n] + c_d) {
				ls.add("delete(" + (m - 1) + ")");
				m--;
			} else if (n > 0 && d[m][n] == d[m][n - 1] + c_i) {
				ls.add("insert(" + (m) + "," + s2.charAt(n - 1) + ")");
				n--;
			}
		}
		return ls;
	}
	


public void main(){
	//EditDistance k = new EditDistance(3,2,1);
	String s1="abcd";
	//System.out.print(k.getEditDistanceDP(s1, "adcb"));
	System.out.print(s1.charAt(1));
	System.out.print("n=" + s1.length());
	//EditDistance l = new EditDistance(3,2,6);
	//System.out.print(l.getEditDistanceDP("abcd", "adcb"));
}
	
}


