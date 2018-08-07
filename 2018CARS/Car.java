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
public class Car{
    String vin;
    String make;
    String model;
    int price;
    int mileage;
    String color;
    
    public Car(String v, String m, String md, int pr, int mi, String c){
        vin = v;
        make = m;
        model = md;
        price = pr;
        mileage = mi;
        color = c;
    }
    
    // standard comparator functions for que implementaion 

    public static Comparator<Car> priceComp(){
        return new Comparator<Car>(){
            @Override
            public int compare(Car x, Car y){
                if(x.price == y.price)
                    return 0;
                else if(x.price < y.price)
                    return -1;
                return 1;
            }
        };
    }
    public static Comparator<Car> milesComp(){
        return new Comparator<Car>(){
            @Override
            public int compare(Car x, Car y){
                if(x.mileage == y.mileage){
                    return 0;
                }
                else if(x.mileage < y.mileage){
                    return -1;
                }
                return 1;
            }
        };
    }

}
