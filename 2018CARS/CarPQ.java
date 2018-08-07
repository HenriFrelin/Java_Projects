/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author Henri
 */
public class CarPQ {
    //initialization//
    //Car car;
    PriorityQueue<Car> Mi; // lowest mileage priority queue 
    PriorityQueue<Car> Pr; // lowest cost priority queue 
    
    public CarPQ(){
        Car c = null;
        Mi = new PriorityQueue<Car>(11, c.milesComp());
        Pr = new PriorityQueue<Car>(11, c.priceComp());
    }
    
    public void add(Car c){ // add a car to the queue(s)       
        Pr.offer(c);
        Mi.offer(c);
    }
    
    public void changePrice(String v, int p){
        Iterator<Car> i = Pr.iterator();
        while(true){ //iterating over que to find vin match 
            Car curr = (Car)i.next();
            if(curr.vin.equals(v)){            
                Pr.remove(curr); // removing from both ques to edit car's prices 
		Mi.remove(curr);
                curr.price = p;
                Pr.add(curr); 
		Mi.add(curr);
                break;
            }
        }
    }
    
    public void changeMileage(String vin, int p){
        Iterator<Car> i = Mi.iterator();
        while(true){ //iterating over que to find vin match 
            Car curr = (Car)i.next();
            if(curr.vin.equals(vin)){
                Pr.remove(curr); // removing from both ques to edit car's prices 
		Mi.remove(curr);
                curr.mileage = p;
                Pr.add(curr); 
		Mi.add(curr);
                break;
            }
        }
    }
    
    public void changeColor(String vin, String c){
        Iterator<Car> i = Mi.iterator();
        while(true){ //iterating over que to find vin match 
            Car curr = (Car)i.next();
            if(curr.vin.equals(vin)){
                Pr.remove(curr); // removing from both ques to edit car's prices 
		Mi.remove(curr);
                curr.color = c;
                Pr.add(curr); 
		Mi.add(curr);
                break;
            }
        }
    }
    
    public void remove(String v){
        Iterator<Car> i = Pr.iterator();
        while(true){ //iterating over que to find vin match 
            Car curr = (Car)i.next();
            if(curr.vin.equals(v)){
                Pr.remove(curr); // removing from both ques to edit car's prices 
		Mi.remove(curr); 
                break;
            }
        }
    }
    
    public Car leastPrice(){
        return Pr.peek();
    }
    
    public Car leastMiles(){
        return Mi.peek();
    }
    
    public Car leastPriceMake(String make, String model){
        Iterator<Car> i = Pr.iterator();
        Car least = null;
        while(i.hasNext()){
            Car curr = (Car)i.next();
            if(curr.model.equals(model) && curr.make.equals(make)){
                if(least == null)
                    least = curr;
                if(least.price > curr.price)
                    least = curr;                
            }
        }
        return least;
    }
    
    public Car leastMilesMake(String make, String model){
        Iterator<Car> i = Mi.iterator();
        Car least = null;
        while(i.hasNext()){
            Car curr = (Car)i.next();
            if(curr.model.equals(model) && curr.make.equals(make)){
                if(least == null)
                    least = curr;
                if(least.mileage > curr.mileage)
                    least = curr;                
            }
        }
        return least;
    }
}
