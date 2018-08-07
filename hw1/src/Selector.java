
public class Selector {
	
	
	
	public static Word[] select(Word[] array, int k){
	// Problem #1
	// Fill in this method with an O(n*k) time algorithm
	// that returns the k largest elements of array in
	// order from largest to smallest.
	// Note: This should return an array with k elements.
            
        Word [] newAR = new Word[k]; // new array of size k

	for(int i = 0; i < array.length - 1; i++){
            for(int j = i + 1; j <= array.length - 1; j++){
                if(array[i].frequency < array[j].frequency)
                    swap(array,i,j);
            }
        }
        
        for(int i = 0; i < k; i ++){
            newAR[i] = array[i];
            System.out.println(newAR[i]);
        }
        
                return newAR;
		//return null;
	}
        private static void swap(Word[] array, int i, int j){ // borrowed from Selector.java
		if(i == j) return;
		
		Word temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
