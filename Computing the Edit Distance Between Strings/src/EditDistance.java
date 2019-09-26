import java.util.*;

public class EditDistance implements EditDistanceInterface {
     
    int c_i, c_d, c_r;
    static int MAX = Integer.MAX_VALUE;
    static int UNDEF = -1;

    public EditDistance (int c_i, int c_d, int c_r) {
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
        
    public int[][] getEditDistanceDP(String s1, String s2) {
    	int l1 = s1.length();
    	int l2 = s2.length();
        int[][] mat = new int[l1+1][l2+1];
        for(int i = 0;i <= l1;i++) mat[i][0] = i*this.c_d;
        for(int j = 0;j <= l2;j++) mat[0][j] = j*this.c_i;
        for(int i = 1;i <= l1;i++)
        	for(int j = 1;j <= l2;j++) {
        		int d = (s1.charAt(i-1)==s2.charAt(j-1))?mat[i-1][j-1]:mat[i-1][j-1]+this.c_r;
        		mat[i][j] = Math.min(Math.min(mat[i-1][j]+this.c_d, mat[i][j-1]+this.c_i),d);
        	}
        return mat;
    }

/*    public List<String> getMinimalEditSequence1(String s1, String s2) {
    	LinkedList<String> ls = new LinkedList<>();
    	int m = s1.length();
    	int n = s2.length();
    	int[][] mat = this.getEditDistanceDP(s1, s2);
        
        while(m>0||n>0) {
        	if(m>0&&n>0) {
        	    if(s1.charAt(m-1)==s2.charAt(n-1)) {
        		    m--;
        		    n--;
            	}
        	    else if(mat[m][n]==mat[m-1][n]+this.c_d) {
            		m--;
            		ls.addFirst("delete("+ n +")");
            	}
        	    else if(mat[m][n]==mat[m][n-1]+this.c_i) {
            		n--;
            		ls.addFirst("insert("+ n +","+ s2.charAt(n) +")");
            	}
        	    
            	else if(mat[m][n]==mat[m-1][n-1]+this.c_r) {
            		m--;
            		n--;
            		ls.addFirst("replace("+ n +","+ s2.charAt(n) +")");
            	}
        	}
        	else {
        		if(m==0) {
        			n--;
            		ls.addFirst("insert("+ n +","+ s2.charAt(n) +")");
        		}
        		else if(n==0) {
        			m--;
            		ls.addFirst("delete("+ n +")");
        		}
        	}
        }
        
        return ls;
    }
*/    
    public List<String> getMinimalEditSequence(String s1, String s2){
    	LinkedList<String> ls = new LinkedList<>();
    	int m = s1.length();
    	int n = s2.length();
    	int x = 1;
    	int d = Math.abs(m-n);
    	
    	
        if(m>=n) {
        	int[][] mat = this.generateTable_V(s1, s2, x);
        	while(mat[d+x+m-n][n]>x*(this.c_i+this.c_d)) {
        		x = 2*x;
        		mat = this.generateTable_V(s1, s2, x);
        	}
        	
        	int m1 = d+x+m-n, n1 = n;
        	while(m1!=d+x||n1!=0) {
        		if(m1>0&&mat[m1][n1]==mat[m1-1][n1]+this.c_d) {
        			m1--;
        			ls.addFirst("delete("+ n1 +")");
        		}
        		else if(n1>0&&m1+n1-d-x>0&&s1.charAt(m1+n1-d-x-1)==s2.charAt(n1-1)) {
        			n1--;
        		}
        		else if(n1>0&&mat[m1][n1]==mat[m1][n1-1]+this.c_r) {
        			n1--;
        			ls.addFirst("replace("+ n1 +","+ s2.charAt(n1) +")");
        		}
        		else if(m1<2*(d+x)&&n1>0&&mat[m1][n1]==mat[m1+1][n1-1]+this.c_i) {
        			m1++;
        			n1--;
        			ls.addFirst("insert("+ n1 +","+ s2.charAt(n1) +")");
        		}
        	}
    	}
        else {
        	int[][] mat = this.generateTable_H(s1, s2, x);
        	while(mat[m][d+x+n-m]>x*(this.c_d+this.c_i)) {
        		x = 2*x;
        		mat = this.generateTable_H(s1, s2, x);
        	}
        	
        	while(m!=0||n!=0) {
        		if(d+x+n-m>0&&mat[m][d+x+n-m]==mat[m][d+x+n-m-1]+this.c_i) {
        			n--;
        			ls.addFirst("insert("+ n +","+ s2.charAt(n) +")");
        		}
        		else if(m>0&&n>0&&s2.charAt(n-1)==s1.charAt(m-1)) {
        			m--;
        			n--;
        		}
        		else if(m>0&&mat[m][d+x+n-m]==mat[m-1][d+x+n-m]+this.c_r) {
        			m--;
        			n--;
        			ls.addFirst("replace("+ n +","+ s2.charAt(n) +")");
        		}
        		else if(m>0&&d+x+n-m<2*(d+x)&&mat[m][d+x+n-m]==mat[m-1][d+x+n-m+1]+this.c_d) {
        			m--;
        			ls.addFirst("delete("+ n +")");
        		}
        	}
        }
        return ls;
    }
    
    // when m >= n
    public int[][] generateTable_V(String s1, String s2, int x){
    	int m = s1.length();
    	int n = s2.length();
    	int d = Math.abs(m-n);
    	int s = d + x;

        	int[][] mat = new int[2*s + 1][n + 1];
        	for(int i=0;i<s;i++) mat[i][0] = MAX;
        	for(int i=s;i<=2*s;i++) mat[i][0] = (i-s)*this.c_d;
        	for(int j=1;j<=n;j++)
        		for(int i=0;i<=2*s;i++) {
        			if(i+j<s) mat[i][j] = MAX;
        			else if(i+j==s) mat[i][j] = j*this.c_i;
        			else {
        				int tmp1 = ((i+j-s-1<m)&&(s1.charAt(i+j-s-1)==s2.charAt(j-1)))?mat[i][j-1]:mat[i][j-1]+this.c_r;
        				int tmp2 = (i>0)?(mat[i-1][j]+this.c_d):MAX;
        				int tmp3 = (i<2*s)?(mat[i+1][j-1]+this.c_i):MAX;
        				mat[i][j] = Math.min(Math.min(tmp1, tmp2), tmp3);
        			}
        		}
    	
        	return mat;
    }
    
    // when m < n
    public int[][] generateTable_H(String s1, String s2, int x){
    	int m = s1.length();
    	int n = s2.length();
    	int d = Math.abs(m-n);
    	int s = d + x;

    		int[][] mat = new int[m + 1][2*s + 1];
        	for(int j=0;j<s;j++) mat[0][j] = MAX;
        	for(int j=s;j<=2*s;j++) mat[0][j] = (j-s)*this.c_i;
        	for(int i=1;i<=m;i++)
        		for(int j=0;j<=2*s;j++) {
        			if(i+j<s) mat[i][j] = MAX;
        			else if(i+j==s) mat[i][j] = i*this.c_d;
        			else {
        				int tmp1 = ((i+j-s-1<n)&&(s2.charAt(i+j-s-1)==s1.charAt(i-1)))?mat[i-1][j]:mat[i-1][j]+this.c_r;
        				int tmp2 = (j>0)?(mat[i][j-1]+this.c_i):MAX;
        				int tmp3 = (j<2*s)?(mat[i-1][j+1]+this.c_d):MAX;
        				mat[i][j] = Math.min(Math.min(tmp1, tmp2), tmp3);
        			}
        		}
    	
        	return mat;
    }
};
