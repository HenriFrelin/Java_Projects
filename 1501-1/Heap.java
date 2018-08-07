
public class Heap {
    
	public Word[] myHeap;
        
        
	public Heap(Word[] array){
		buildHeap(array);
                myHeap = array;
	}
        public int size;
	public void buildHeap(Word[] array){
	// Problem #2
	// Fill in this method with an O(n) time algorithm
	// that builds an n element complete binary heap.
	// Note: You are allowed to add and modify fields
        // and helper methods.

            for (int k = 0; k <= array.length - 1; k++) {
                makeHeap(array, k);
            } 
            
            for (Word array1 : array) {
                System.out.println(array1);
            }
            myHeap = array;
        
	}
        
        public void makeHeap(Word[] array, int i){
            int max;
            size = array.length;
            int L = i * 2;     // 'L' gives the index to the left child
            int R = i * 2 + 1; // 'R' gives the index to the right child
            int P = (i-1) / 2; // 'P' gives the index to the parent
            
            if (((L < array.length) && (array[L].frequency > array[i].frequency))){
                max = L;
            }else{
                max = i;
            }
            
            if (((R < array.length) && (array[R].frequency > array[max].frequency))) {
                max = R;
            }
            
            if (max != i) {
                swap(array, i, max);
                
                makeHeap(array, max);
            }
            
        }
        
        private static void swap(Word[] array, int i, int j){ // borrowed from Selector.java
		if(i == j) return;
		
		Word temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public Word removeMax(){
	// Problem #3
	// Fill in this method with an O(log(n)) time algorithm
	// that removes the root element, restores the heap
	// structure, and finally returns the removed root element.
            //System.out.println("Root" + myHeap[0]);
            //System.out.println("size" + size);
            //System.exit(0);
            myHeap[0] = myHeap[--size];

                   percolateDown();
	           //System.out.println("HERE");
		return myHeap[0];
	}
        
        public void percolateDown(){            
            int max;
            int i = 0;
            Word top = myHeap[i];
            while(i < size/2){
                int L = i * 2 + 1;     // 'L' gives the index to the left child
                int R = L + 1; // 'R' gives the index to the right child
                int P = (i-1) / 2; // 'P' gives the index to the parent
                if(myHeap[L].frequency < myHeap[R].frequency && R < size)
                    max = R;
                else
                    max = L;
                if(top.frequency >= myHeap[max].frequency)
                    break;
                myHeap[i] = myHeap [max];
                break;
            }
            myHeap[i] = top;
            //System.out.println(array[0]);
            
        }
	
	public Word[] select(int k){
		Word[] result = new Word[k];
		for(int i = 0; i < k; i++){
			result[i] = this.removeMax();
                        
		}
		return result;
	}
}
