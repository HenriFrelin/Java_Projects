/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
	public static void main(String[] args){
		Datum[] dataArray = DataReader.loadData();
		ChainingHashMap map = new ChainingHashMap(1000);
		
		// Populate the map with words and their corresponding frequencies
		for(int i=0; i<dataArray.length; i++)
			map.put(dataArray[i].word, dataArray[i].frequency);
		
		// Evaluate the effectiveness of the hash function
		int sizeOfLargestList = collisionTest(map);
		int numberOfEmptyLists = sparsityTest(map);
		
		// Print the results
		System.out.println("The size of the largest linkedlist is: " + sizeOfLargestList);
		System.out.println("The total number of empty linkedlists is: " + numberOfEmptyLists);
                Word d = new Word("above");         
                System.out.println("The word 'above' appears: " + map.get(d) + " times!"); // testing get function!
                System.out.println("Removing the word 'above'. The value: " + map.remove(d) + " was returned and it was deleted!"); // testing remove function! 
                System.out.println("Now if we try to call the get method on 'above', it returns: " + map.get(d));
	}
	
	public static int collisionTest(ChainingHashMap map){
	// Problem #2A
	// Fill in this method to compute the size of the largest
	// linkedlist. You must use the getSize and countCollisions
	// methods to get full credit.
            int largest = 0;
            int i;
            for(i = 0; i < map.getSize(); i++){
                if(map.countCollisions(i) > largest)
                    largest = map.countCollisions(i);                   
            }           
            
            return largest;
	}
	
	public static int sparsityTest(ChainingHashMap map){
	// Problem #2B
	// Fill in this method to compute the number of empty
	// linkedlists. You must use the getSize and countCollisions
	// methods to get full credit.
            int empties = 0;
            int i;
            for(i = 0; i < map.getSize(); i++){
                if(map.countCollisions(i) == 0)
                    empties ++;
            }
            return empties;
	}
}