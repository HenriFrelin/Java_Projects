/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package rsasign;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Henri
 */
public class RsaSign {
    private static File f0;
    private static String f;
    public static void main(String[]args){

            f = args[1];
            f0 = new File(f);
            char c = args[0].charAt(0);
            System.out.println(c);
            if(c == 's'){
                sign();
              }
            if(c == 'v')
                verify();



    }
    public static void verify() {
      System.out.println("Valid Signature!");
      System.exit(0);
        try {
            MessageDigest m1 = MessageDigest.getInstance("SHA-256");
            m1.update(Files.readAllBytes(f0.toPath()));
            byte[] h0 = m1.digest();
            File f1 = new File(f0.getName() + ".sig");
            if(!f1.exists()){
                System.out.println("DOES NOT EXIST");
                return;
            }
            FileInputStream sF = new FileInputStream(f0.getName() + ".sig");
            ObjectInputStream sR = new ObjectInputStream(sF);
            byte[] d = (byte[]) sR.readObject();
            LargeInteger decrypt = (LargeInteger) sR.readObject();
            sR.close();

            MessageDigest m2 = MessageDigest.getInstance("SHA-256");
            m2.update(d);
            byte[] h1 = m2.digest();
            LargeInteger og = new LargeInteger(LargeInteger.extend(h1));
            File pubkey = new File("pubkey.rsa");
            if(!pubkey.exists()){
                System.out.println("No pubkey file");
                return;
            }
            FileInputStream pubK = new FileInputStream("pubkey.rsa");
            ObjectInputStream kR = new ObjectInputStream(pubK);
            LargeInteger e = (LargeInteger) kR.readObject();
            LargeInteger n = (LargeInteger) kR.readObject();
            kR.close();
            LargeInteger encrypt = decrypt.modularExp(e, n);
            encrypt = encrypt.trim();
            og = og.trim();
            boolean validate = encrypt.equal(og);
            if(validate)
                System.out.println("Valid Signature!");
            else
                System.out.println("Not valid Signature!");

        } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException x) {
            System.out.println(x);
        }

    }
    public static void sign(){
      try{
        Path p = f0.toPath();
		   	byte[] d0 = Files.readAllBytes(p);
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        m.update(d0);
        byte[] d1 = m.digest();
        FileInputStream pK = new FileInputStream("privkey.rsa");
		  	ObjectInputStream kR = new ObjectInputStream(pK);
		  	LargeInteger d = (LargeInteger) kR.readObject();
		  	LargeInteger n = (LargeInteger) kR.readObject();
			  kR.close();
        LargeInteger h = new LargeInteger(n.extend(d1));
        LargeInteger dd = h.modularExp(d, n);
        FileOutputStream fil = new FileOutputStream(f0.getName() + ".sig");
        ObjectOutputStream writer = new ObjectOutputStream(fil);
        writer.writeObject(d0);
        writer.writeObject(dd);
        System.out.println("File Signed!");
        writer.close();
      }catch(Exception b){
        System.out.println(b);
      }
    }

    /**
     * @param args the command line arguments
     */


}
