
public class StableMatching implements StableMatchingInterface{
	int n,m,w;
	int[] menGroupCount;
    int[] womenGroupCount;
    int[] womenLowestPrefsIndex; // index of least attractive group of men by women
    int[] menFirstPrefsIndex; // index of most attractive group of women by men
    int[][] menPrefs;
    int[][] womenPrefs;
    // w*m matrix of perference ordre. (W)_j,i = perference order of men group i by women group j
    int[][] womenPrefsOrder; 
    int[][] mar;
    
  // static functions
    
    public static int size(int[] GroupCount) {
    	int n = 0;
    	for(int i=0;i<GroupCount.length;i++)
    		n += GroupCount[i];
    	return n;
    }
    
    public static int min(int a,int b) {
    	return (a>b)?b:a;
    }
    
  // functions of objects
    
    // number of unengaged men in group i
    private int nb_unengaged(int i) {
    	return this.menGroupCount[i];
    }
    
    // number of same relationship women in group j (unengaged or engaged to a man in group i1)
    private int nb_same_relationship(int j, int i1) {
    	return this.mar[i1][j];
    }
    
    private int nb_same_relationship(int j) {
    	return this.womenGroupCount[j];
    }
    
    // the proposal of group y to group x
    private boolean Proposal(int y,int x) {
    	// unengaged women exist in group x
    	if(womenGroupCount[x]>0) {
    		int nb_engage = min(nb_same_relationship(x),nb_unengaged(y));
    		this.menGroupCount[y] -= nb_engage;
    		this.womenGroupCount[x] -= nb_engage;
    		this.n -= nb_engage;
    		this.mar[y][x] += nb_engage;
    		
    		if(womenPrefsOrder[x][y] > womenLowestPrefsIndex[x])
    			womenLowestPrefsIndex[x] = womenPrefsOrder[x][y];
    		return true;
    	}
    	
    	// group x is all engaged
    	if(womenLowestPrefsIndex[x] > womenPrefsOrder[x][y]) {
        	int yprime = womenPrefs[x][womenLowestPrefsIndex[x]];
    		int nb_engage = min(nb_same_relationship(x,yprime),nb_unengaged(y));
    		this.menGroupCount[y] -= nb_engage;
    		this.menGroupCount[yprime] += nb_engage;
    		this.mar[yprime][x] -= nb_engage;
    		this.mar[y][x] += nb_engage;
    		
    		while(mar[womenPrefs[x][womenLowestPrefsIndex[x]]][x]==0)
    			womenLowestPrefsIndex[x]--;
    		return true;
    	}
    	
    	// group y is not more attractive than the group of fiance of x
    	this.menFirstPrefsIndex[y]++;
    	return false;
    }
    
    // choose the men group i containing more than n/2m unengaged men
    private int menGroupChosen(int count) {
    	int i = 0;
    	for(;i<this.m;i++)
    		if(this.menGroupCount[(i+count)%this.m]>this.n/(2*this.m)) break;
    	return (i+count)%this.m;
    }
    
    public int[][] constructStableMatching (
    	    int[] menGroupCount,
    	    int[] womenGroupCount,
    	    int[][] menPrefs,
    	    int[][] womenPrefs
    	  ){
    	//initialisation
    	this.n = size(menGroupCount);
    	this.m = menGroupCount.length;
    	this.w = womenGroupCount.length;
    	this.menGroupCount = menGroupCount;
    	this.womenGroupCount = womenGroupCount;
    	this.menPrefs = menPrefs;
    	this.womenPrefs = womenPrefs;
    	this.womenPrefsOrder = new int[w][m];
    	this.mar = new int[m][w];
    	this.menFirstPrefsIndex = new int[m];
    	this.womenLowestPrefsIndex = new int[w];
    	
    	for(int i=0;i<m;i++) this.menFirstPrefsIndex[i] = 0;
    	for(int i=0;i<w;i++) this.womenLowestPrefsIndex[i] = 0;
    	for(int i=0;i<w;i++)
    		for(int j=0;j<m;j++)
    			this.womenPrefsOrder[i][womenPrefs[i][j]] = j;
    	for(int i=0;i<m;i++)
    		for(int j=0;j<w;j++)
    			this.mar[i][j] = 0;
    	
    	
    	// when there are unengaged men, do iteration
    	    int menGroup = 0;
    	while(this.n>0) {
    		menGroup = this.menGroupChosen(menGroup);
    		int womenGroup = this.menPrefs[menGroup][this.menFirstPrefsIndex[menGroup]];
    		while(!this.Proposal(menGroup, womenGroup))
    			womenGroup = this.menPrefs[menGroup][this.menFirstPrefsIndex[menGroup]];
    	}
    	
    	return this.mar;
    };

}
