public class ChainingHashMap{
	Node[] array;
	int size;
	
	public ChainingHashMap(int size){
		this.size = size;
		array = new Node[size];
	}

	public Integer get(Word key) {
	// Problem #1A
	// Fill in this method to get the value corresponding
	// with the key. Note: if the key is not found, then
	// return null.
            int hash = key.hashCode();
            hash = hash%array.length;
            if(array[hash] == null)
                return null;
            
            if(array[hash].word.equals(key))
                return array[hash].frequency;
            
            Node temp = array[hash];
            while(null != temp.next){
                if(temp.word.equals(key)){
                    return temp.frequency;
                }
                temp = temp.next;
                
            }
            return null;     
	}

	public void put(Word key, Integer value) {
	// Problem #1B
	// Fill in this method to insert a new key-value pair into
	// the map or update the existing key-value pair if the
	// key is already in the map.
            
            int hash = key.hashCode();
            hash = hash%array.length;
            //System.out.println(hash);
 
            Node n = new Node(key, value, null);
            //System.out.println(key);
            if(array[hash] == null)
                array[hash] = n;
                    
            
            else if(array[hash] != null){

                Node temp = array[hash];
                
                while(null != temp.next){ // traverse until empty node! 
                    temp = temp.next; 
                }
                temp.next = n; 
            }
            
            //array[hash] = n; // instantiating the node at hash value in array

	}

	public Integer remove(Word key) {
	// Problem #1C
	// Fill in this method to remove a key-value pair from the
	// map and return the corresponding value. If the key is not
	// found, then return null.
            int hash = key.hashCode();
            hash = hash%array.length;
            
            if(array[hash] == null)
                return null;
            
            if(array[hash].word.equals(key)){
                int temp = array[hash].frequency;
                array[hash] = null;
                return temp;
            }                              

                Node temp = array[hash];
                while(null != temp.next){ // traverse until empty node!                     
                    if(temp.next.word.equals(key)){
                        int t = temp.next.frequency;
                        temp.next = temp.next.next; // delete node
                        return t;                       
                    }
                    temp = temp.next;
                }           
            return null;
	}
	
	// This method returns the total size of the underlying array.
	// In other words, it returns the total number of linkedlists.
	public int getSize(){
		return size;
	}
	
	// This method counts how many keys are stored at the given array index.
	// In other words, it computes the size of the linkedlist at the given index.
	public int countCollisions(int index){
		if(index < 0 || index >= size) return -1;
		
		int count = 0;
		Node temp = array[index];
		while(temp != null){
			temp = temp.next;
			count++;
		}
		
		return count;
	}
	
}