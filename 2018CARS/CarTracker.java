/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 *
 * @author Henri
 */
public class CarTracker{
    
    static CarPQ Q; 
     
    public static void main(String[] args) throws FileNotFoundException {
        Q = new CarPQ();
        Scanner sc = new Scanner(System.in);
        boolean sent = true;
        
         try {
            System.out.print("Enter the test file name : ");

            Scanner input = new Scanner(System.in);

            File file = new File(input.nextLine());

            input = new Scanner(file);

            input.nextLine(); // skip first line
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] s = line.split(":");
                Car car = new Car(s[0],s[1],s[2],Integer.parseInt(s[3]),Integer.parseInt(s[4]),s[5]);
                Q.add(car); 
            }
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        while(sent == true){
            
            System.out.println(" Add car = 1 \n Update Car = 2 \n Remove Car = 3 \n Cheapest Car = 4 \n Lowest Mileage Car = 5 \n Cheapest Car by Make/Model = 6 \n Lowest Mileage Car by Make/Model = 7 \n");
            String ans0 = sc.next();
            int ans = Integer.parseInt(ans0);

            if(ans == 1){
                System.out.println(" Enter the following info... vin, make, model, price, mileage, color");
                System.out.println(" vin: \n");
                String v = sc.next();
                //int v0 = Integer.parseInt(v);
                System.out.println(" make: \n");
                String m = sc.next();
                System.out.println(" model: \n");
                String md = sc.next();
                System.out.println(" price: \n");
                String pr = sc.next();
                int pr0 = Integer.parseInt(pr);
                System.out.println(" mileage: \n");
                String mi = sc.next();
                int mi0 = Integer.parseInt(mi);
                System.out.println(" color: \n");
                String c = sc.next();
                Car car = new Car(v,m,md,pr0,mi0,c);
                Q.add(car);                          
            }
            if(ans == 2){
                System.out.println("Enter the vin: \n");
                String v = sc.next();
                System.out.println("Update Price = 1, Mileage = 2, Color = 3\n");
                String ans2 = sc.next();
                int ans3 = Integer.parseInt(ans2);
                if(ans3 == 1){
                    System.out.println("Enter Price: \n");
                    String p0 = sc.next();
                    int p = Integer.parseInt(p0);
                    Q.changePrice(v, p);
                }
                if(ans3 == 2){
                    System.out.println("Enter Mileage: \n");
                    String mile = sc.next();
                    int mile1 = Integer.parseInt(mile);
                    Q.changeMileage(v, mile1);
                }            
                if(ans3 == 3){
                    System.out.println("Enter Color: \n");
                    String color = sc.next();
                    Q.changeColor(v, color);
                }
                
            }
            if(ans == 3){               
                System.out.println("Enter vin of car to be deleted\n");
                String ans2 = sc.next();
                Q.remove(ans2);               
            }
            if(ans == 4){
                System.out.println("Vin: " + Q.leastPrice().vin + " " + Q.leastPrice().make + " " + Q.leastPrice().model + " : " + Q.leastPrice().price + " : " + Q.leastPrice().mileage + " : " + Q.leastPrice().color);
            }
            if(ans == 5){
                System.out.println("Vin: " + Q.leastMiles().vin + " " + Q.leastMiles().make + " " + Q.leastMiles().model + " : " + Q.leastMiles().price + " : " + Q.leastMiles().mileage + " : " + Q.leastMiles().color);
            }
            if(ans == 6){
                System.out.println("Enter Make: \n");
                String ans1 = sc.next();
                System.out.println("Enter Model: \n");
                String ans2 = sc.next();
                System.out.println("Vin: " + Q.leastPriceMake(ans1, ans2).vin + " " + Q.leastPriceMake(ans1, ans2).make + " " + Q.leastPriceMake(ans1, ans2).model + " : " + Q.leastPriceMake(ans1, ans2).price + " : " + Q.leastPriceMake(ans1, ans2).mileage + " : " + Q.leastPriceMake(ans1, ans2).color);
            }
            if(ans == 7){
                System.out.println("Enter Make: \n");
                String ans1 = sc.next();
                System.out.println("Enter Model: \n");
                String ans2 = sc.next();
                System.out.println("Vin: " + Q.leastMilesMake(ans1, ans2).vin + " " + Q.leastMilesMake(ans1, ans2).make + " " + Q.leastMilesMake(ans1, ans2).model + " : " + Q.leastMilesMake(ans1, ans2).price + " : " + Q.leastMilesMake(ans1, ans2).mileage + " : " + Q.leastMilesMake(ans1, ans2).color);
            }
            
            
        }
        sc.close();       
    }
   
}