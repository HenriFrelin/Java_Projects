/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package rsasign;
import java.io.*;
import java.util.*;
import java.math.BigInteger;
/**
 *
 * @author Henri
 */
public class RsaKeyGen {
    public static void main(String[] args){

        //Creating a random Prime for P & Q
        Random r = new Random();
        LargeInteger p = new LargeInteger(256,r);
        LargeInteger q = new LargeInteger(256,r);
        LargeInteger n = p.multiply(q); // creating n by multipling p & q
        //System.out.println(q.getVal());
        LargeInteger pm1 = p.subOne();
        LargeInteger qm1 = q.subOne();
        LargeInteger delta = new LargeInteger(pm1.extend(pm1.multiply(qm1).getVal())); ///////////////////////// StaticmEThod???????
        int count=0;
        LargeInteger e = e();
        LargeInteger d = new LargeInteger(null);
        while(!LargeInteger.XGCD(e,delta).equalsOne() && count<5){
           e = e.add(new LargeInteger(new byte[] {2}));
           count++;
         }
        //d = e.inverse(delta);
        //System.out.println("Issue?");
        commitKeys(e,d,n);
    }

    public static void commitKeys(LargeInteger e, LargeInteger d, LargeInteger n){
        try{
            FileOutputStream pk = new FileOutputStream("pubkey.rsa");
            ObjectOutputStream pw = new ObjectOutputStream(pk);
            pw.writeObject(e);
            pw.writeObject(n);
            pw.close();
            FileOutputStream prk = new FileOutputStream("privkey.rsa");
            ObjectOutputStream prw = new ObjectOutputStream(prk);
            prw.writeObject(d);
            prw.writeObject(n);
            prw.close();
            System.out.println("Wrote public and private keys!");
        }catch(IOException ex){
            System.out.println(ex.toString());
            return;
        }
    }
    public static LargeInteger e(){
        byte b = 3;
        LargeInteger e = new LargeInteger(null);
        LargeInteger i = new LargeInteger(new byte[] {b});
        e = e.add(i);
        return e;
    }


}
