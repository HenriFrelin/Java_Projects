public class Tree<K extends Comparable,D> {

    private Node<K,D> root;
    
    public Tree() { root = null; }

    public Tree(K[] keys, D[] data) {
	root = buildTree(keys, data, 0, keys.length-1);
    }

    private Node<K,D> buildTree(K[] keys, D[] data, int lo, int hi) {
	if(lo > hi)
	    return null;
	int m = (hi - lo) / 2 + lo;
	return new Node(keys[m], data[m], 
			buildTree(keys, data, lo, m-1),
			buildTree(keys, data, m+1, hi));
    }

    private D find(K key, Node<K,D> x) {
	if(x == null)
	    return null;
	int c = key.compareTo(x.key); 
	if(c == 0)
	    return x.data;
	else if(c < 0) 
	    return find(key, x.left);
	else // c > 0
	    return find(key, x.right);
    } 

    public D find(K key) {
	return find(key, root);
    }

    private Node<K,D> add(K key, D data, Node<K,D> root) {
	// returns the tree with the added record.
	if(root == null)
	    return new Node<K,D>(key,data);
	int c = key.compareTo(root.key); 
	if(c == 0) {
	    System.err.println("Error: duplicate key: "+key);
	    System.exit(1);
	    return null;
	}
	else if(c < 0) {
	    root.left =  add(key, data, root.left);
	    return root;
	}
	else { // c > 0
	    root.right = add(key, data, root.right);
	    return root;
	}
    }

    public void add(K key, D data) {
	root = add(key, data, root);
    }

    private void modify(K key, D data, Node<K,D> root) {
	if(root == null) {
	    System.err.println("Error: key not found: "+key);
	    System.exit(1);
	}
	int c = key.compareTo(root.key);
	if(c == 0)
	    root.data = data;
	else if(c < 0) 
	    modify(key, data, root.left);
	else // c > 0
	    modify(key, data, root.right);
    }

    public void modify(K key, D data) {
	modify(key, data, root);
    }

    private String toString(Node<K,D> root) {
	if(root == null)
	    return "";
	return toString(root.left) 
	    + "(" + root.key + "," 
	    + root.data + ")" 
	    + toString(root.right);
    }
    

    private String toStringReversed(Character [] c, int i, StringBuilder s) {
        //System.out.println(i);
        if(i <= 0){
            return s.toString();
        }
        s.append(" ");
	return toStringReversed(c,--i,s.append(c[i]));         		
    }
 

    public String toString() {
	return toString(root);
    }
    
    //public String toStringReversed() {
//	return toStringReversed(root);
    //}
       
    private Node ceiling(Node x, K key) {
        if (x == null) 
            return null;
        int comp = key.compareTo(x.key);
        if (comp == 0) 
            return x;
        if (comp < 0) { 
            Node t = ceiling(x.left, key); 
            if (t != null) 
                return t;
            else 
                return x; 
        } 
        return ceiling(x.right, key); 
    } 
    
    private Node floor(Node x, K key) {
        if (x == null) 
            return null;
        int comp = key.compareTo(x.key);
        if (comp == 0) 
            return x;
        if (comp < 0)  // key < than root data
            return floor(x.left, key);
        Node t = floor(x.right, key); 
        if (t != null) 
            return t;
        else 
            return x; 

    } 
    
    public int diameter(Node root) {
        if (root == null)
        return 0;

    int rootD = getHeight(root.left) + getHeight(root.right) + 1;
    int leftD = diameter(root.left);
    int rightD = diameter(root.right);
    return Math.max(rootD, Math.max(leftD, rightD));
    }

    public static int getHeight(Node root) {
    if (root == null)
        return 0;
    return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
}    
    
    private Node<K,D> findLeftmost(Node<K,D> root) {
	// Assumes root != null.
	return root.left == null 
	    ? root
	    : findLeftmost(root.left);
    }

    private Node<K,D> removeLeftmost(Node<K,D> root) {
	if(root.left == null) 
	    return root.right;
	else {
	    root.left = removeLeftmost(root.left);
	    return root;
	}
    }
   
    
    public int numBetween(K key1, K key2, Node root, int t, boolean start){
        //System.out.println(t);
        if (root == null) 
            return t;
        int comp0 = root.key.compareTo(key1);
        int comp1 = root.key.compareTo(key2);
        //System.out.println("KEY1: " + key1 + " KEY2: " + key2 +  " ROOT: " + root.key);
        //System.out.println(root.key);
        if (comp1 == 0) {                    
            return t; 
        }
        if(comp0 == 0){
            start = true; 
            t++;
        }
        if(start == true)
            return numBetween(key1, key2, root.left, t, start) + numBetween(key1, key2, root.right, t, start);
        
        if (root.left != null) {
            if(start == true){
                t++;
            }  
            return  numBetween(key1, key2, root.left, t, start);
            
        }
        if (root.right != null) {
            if(start == true){
                t++;
            }   
            return numBetween(key1, key2, root.right, t, start); 
            
        }
        
                
        return t;
         
    }
    
   // public int numBetween(K key1, K key2, Node root, int t, boolean start){
       // if(root == null)
	    //return t;
	//numBetween(key1, key2, root.left, t, start) + numBetween(key1, key2, root.right, t, start);
   // }
    private Node<K,D> delete(K key, Node<K,D> root) {
	if(root == null) {
	    System.err.println("Error: key not found");
	    System.exit(1);
	    return null;
	}
	int c = key.compareTo(root.key);
	if(c == 0) {
	    if(root.left == null)
		return root.right;
	    else if(root.right == null)
		return root.left;
	    else {
		Node<K,D> t = root;
		root = findLeftmost(root.right);
		root.right = removeLeftmost(t.right);
		root.left = t.left;
		return root;
	    }
	}
	else if(c < 0) {
	    root.left = delete(key, root.left);
	    return root;
	}
	else { // c > 0
	    root.right = delete(key, root.right);
	    return root;
	}
    }

    public void delete(K key) {
	root = delete(key, root);
    }

    public void numBetween(K key, K key1) {
        boolean s = false;
        int temp = 2;
	int x = numBetween(key,key1,root, temp, s);
        //System.out.println("Root is: " + root.key);
        System.out.println("Num Between(" + key + " & " + key1 + "):" + x);
    }
    public void diameter() {
	int x = diameter(root);
        System.out.println("Diameter: " + x);
    }
    public void ceiling(K key) {
        if(ceiling(root, key) == null){
            System.out.println("Ceiling(" + key + "): NULL");
        }
        else
            System.out.println("Ceiling(" + key + "): " + ceiling(root, key).key);
    }
    
    public void floor(K key) {
        if(floor(root, key) == null){
            System.out.println("Floor(" + key + "): NULL");
        }
        else
            System.out.println("Floor(" + key + "): " + floor(root, key).key);
    }
    

    public static void main(String[] args) {
     
        StringBuilder sb = new StringBuilder();
	Character[] keys = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        int i = keys.length;
	String[] data= { "Atlanta", "Boston", "Charleston", "Denver", "East Hampton", "Foxborough", "Galveston" };
	Tree<Character, String> tree = new Tree<Character, String>(keys, data);
	System.out.println(tree);
        System.out.println("Reversed Tree:" + tree.toStringReversed(keys,i,sb));
        tree.toStringReversed(keys,i,sb);
        System.out.println();
        tree.diameter();  
        System.out.println();
	tree.delete('C');
	tree.delete('D');
	tree.add('P', "Pittsburgh");
	tree.modify('A', "Austin");
        tree.ceiling('C');   
        tree.ceiling('F'); 
        tree.ceiling('Z');
        System.out.println();            
        tree.floor('C');   
        tree.floor('F'); 
        tree.floor('Z');
        System.out.println();
	System.out.println(tree);       
        tree.numBetween('A', 'A');
        tree.numBetween('A', 'P');
	System.out.println(tree.find('A'));
	System.out.println(tree.find('D')); 
    }
}