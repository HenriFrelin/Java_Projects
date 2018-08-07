/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package rsasign;
import java.util.Random;
import java.math.BigInteger;
import java.io.*;
import java.io.Serializable;

public class LargeInteger implements Serializable{

	private final byte[] ONE = {(byte) 1};
  private final static LargeInteger one = new LargeInteger(new byte[] {1});

	private byte[] val;

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		if(b == null) {
			val = new byte[65]; //Default class is 65*8 0 bits
		}
    else {
			val = b;

		}
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
		//System.out.println(val);
	}

	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
        public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	public static byte[] extend(byte[] extension) {
		byte[] newv = new byte[extension.length + 1];

		for (int i = 0; i < extension.length; i++) {
			newv[i + 1] = extension[i];
		}
		return newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */

	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
                if(val == null) return null;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */
	public LargeInteger multiply(LargeInteger n) {
                //System.out.println("FILL OUT MULTIPLY METHOD");
		// YOUR CODE HERE (replace the return, too...)
                if(n == null)
                    return null;
		byte[] ndata = n.getVal();
                if(ndata == null)
                    return null;
                LargeInteger total = new LargeInteger (null);
                LargeInteger tC = new LargeInteger (copy(val));
                LargeInteger nC = new LargeInteger(copy(n.getVal()));
                //System.out.println(total);
                while(!nC.zero()){
                    if(!nC.even()){
                        total = total.add(new LargeInteger(extend(tC.getVal())));
										}
                    tC = tC.lShift(1);
                    nC = nC.rShift(1);

                }
								//System.out.println("HERE THO???");
                return total;
	}
        private static byte[] copy(byte[] b){
            byte[] r = new byte[b.length];
            for(int j = 0; j < b.length; j++){
                r[j] = b[j];
            }
            return r;
        }
        public boolean zero(){
            for(int j = 0; j < val.length; j++){
                if(val[j] != 0)
                    return false;
            }
            return true;
        }
        public boolean even(){
            byte b = (byte) (val[val.length-1]&0x1);
            return b == 0;
        }
        public LargeInteger rShift(int x){
            byte[] data = val;

            for(int j = 0; j < x; j++){
                boolean c = false;

                for(int k = 0; k < data.length; k++){
                    byte lb = (byte) (data[k] & 1);
                    boolean haveB = (data[k] & 0x80) !=0;
                    data[k] = (byte) ((data[k] & 0xFF) >> 1);

                    if(c)
                        data[k] |= 128;
                    if(lb == 0)
                        c = false;
                    else
                        c = true;
                }
                data[0] &= 0x7F;
            }
           return new LargeInteger(data);
        }
        public LargeInteger lShift(int x){
            boolean c = false;
            byte[] data = val;
            for(int j = 0; j < x; j++){
                c = false;
                int m = data[0] & 128;
                if(m == 0)
                    data = copy(data);
                else
                    data = extend(data);
                for(int k = data.length-1; k >= 0; k--){
                    byte mb = (byte) (data[k] & 128);
                    data[k] <<= 1;
                    if(c)
                        data[k] |= 1;
                    if(mb == 0)
                        c = false;
                    else
                        c = true;
                }
            }
           return new LargeInteger(data);
        }
        public LargeInteger mod(LargeInteger n){
            if(comp(n) == -1)
                return this;
            else if(comp(n) == 0)
                return new LargeInteger(null);

            LargeInteger d = this.div(new LargeInteger(n.getVal()));
            LargeInteger m = d.multiply(new LargeInteger(n.getVal()));
            LargeInteger r = this.subtract(new LargeInteger(m.getVal()));
            return r;
        }
        public int comp(LargeInteger n){
            LargeInteger tS = this.trim();
            LargeInteger nS = n.trim();
            if(tS.bitLen() > nS.bitLen())
                return 1;
            else if(tS.bitLen() < nS.bitLen())
                return -1;
            int len = tS.getVal().length;
            byte[] tD = tS.getVal();
            byte[] nD = nS.getVal();
            byte thisBit = (byte) 0x40;
            for(int i = 0; i < len; i++){
                byte b1 = (byte) ((tD[i] & 0x80) == 0?0:1);
                byte b2 = (byte) ((nD[i] & 0x80) == 0?0:1);
                if(b1 > b2)
                    return 1;
                else if(b1 < b2)
                    return -1;
                for(int k = 6; k >= 0; k--){
                    byte b3 = (byte) ((tD[i] & thisBit) == 0?0:1);
                    byte b4 = (byte) ((nD[i] & thisBit) == 0?0:1);
                    if(b3 > b4)
                        return 1;
                    else if(b3 < b4)
                        return -1;
                    thisBit >>= 1;
                }
                thisBit = (byte) 0x40;
            }
            return 0;
        }
        public LargeInteger div(LargeInteger n){
            if(comp(n) == -1)
                return new LargeInteger(null);
            else if(comp(n) == 0)
                return one;
            LargeInteger r = new LargeInteger(null);
            LargeInteger q = new LargeInteger(null);
            q = q.bitNum(this.bitLen()/8);
            for(int j = 0; j < this.bitLen(); j++){
                r = r.lShift(1);
                if(getBit(j) == true)
                    r = r.setLB(true);
                LargeInteger rC = r.trim();
                if(rC.comp(n) >= 0){
                    r = r.subtract(n);
                    q = q.setBit(j);
                }
            }
            return q;
        }
        public boolean getBit(int bitN){
            int byteN = bitN/8;
            int bitI = 7 - (bitN - (byteN*8));
            int bitVal = (int) Math.pow(2, bitI);
            byte end = (byte) (val[byteN] & bitVal);
            return end !=0;
        }
        public int bitLen(){
            return val.length*8;
        }
        public LargeInteger bitNum(int n){
            byte [] b = new byte[n];
            return new LargeInteger(b);
        }
        public LargeInteger setLB(boolean b){
            byte[] data = copy(val);
            if(b)
                data[data.length-1] |= 0x1;
            else
                data[data.length-1] &= 0xFE;
            return new LargeInteger(data);
        }
        public LargeInteger setBit(int bitN){
            int byteN = bitN/8;
            int bitI = 7 - (bitN - (byteN*8));
            int bitVal = (int) Math.pow(2, bitI);
            byte[] end = copy(val);
            end[byteN] |=  bitVal;
            return new LargeInteger(end);
        }
	public boolean equalsOne(){
            if(val[val.length-1] != 1)
                return false;
            for(int j = 0; j < val.length-1; j++){
                if(val[j] != 0)
                    return false;
            }
            return true;
        }
        public LargeInteger inverse(LargeInteger n){
            LargeInteger delta = new LargeInteger(copy(n.getVal()));
            delta = delta.add(one);
            while(!delta.mod(this).zero()){
                delta = delta.add(n);
            }
            return delta.div(this);
        }
        public LargeInteger subOne(){
            byte[] end = copy(val);
            end[end.length-1] &= 0xFE;
            return new LargeInteger(end);
        }
        public LargeInteger or(LargeInteger n){
            byte[] nD = n.getVal();
            byte[] end = new byte[val.length];
            for(int j = 0; j < val.length; j++){
                int c = 1;
                for(int k = 7; k >= 0; k--){
                    byte b0 = 0;
                    byte b1 = 0;
                    if(j >= nD.length){
                        b0 = (byte) (val[val.length-j-1]&c);
                        b1 = 0;
                    }
                    else{
                        b0 = (byte) (val[val.length-j-1]&c);
                        b1 = (byte) (nD[nD.length-j-1]&c);
                    }
                    byte write = 0;
                    if(b0 != 0&&b1 == 0)
                        write = 0;
                    else if(b0 != 0 && b1 == 0)
                        write = (byte) c;
                    else if(b0 != 0 && b1 != 0)
                        write = (byte) c;
                    else if(b0 == 0 && b1 != 0)
                        write = (byte) c;
                    end[val.length-j-1] |= write;
                    c *= 2;
                }
            }
            return new LargeInteger(end);
        }
        public LargeInteger and(){
            byte[] nVal = copy(val);
            boolean a = (nVal[nVal.length-1] & 0x1) !=0;
            for(int j = 0; j < nVal.length; j++){
                nVal[j] = 0;
            }
            if(a)
                nVal[nVal.length-1] = 0x1;
            return new LargeInteger(nVal);
        }
        public LargeInteger trim(){
            int bytes = 0;
            for(int j = 0; j < val.length; j++){
                if(val[j] == 0)
                    bytes++;
                else
                    break;
            }
            byte [] nVal = new byte[val.length-bytes];
            for(int j = 0; j < nVal.length; j++){
                nVal[j] = val[j+bytes];
            }
            return new LargeInteger(nVal);
        }
        public boolean equal(LargeInteger n){
            if(n == null)
                return false;
            byte[] vData = n.getVal();
            if(val.length != vData.length || val == null)
                return false;
            int len = Math.max(val.length, vData.length);

            for(int j = 0; j < len; j++){
                if(j < val.length && j < vData.length){
                    if(val[j] != vData[j])
                        return false;
                }else{
                    if(j >= val.length){
                        if(vData[j] != 0)
                            return false;
                    }else if(j >= vData.length){
                        if(val[j] != 0)
                            return false;
                    }

                }
            }
            return true;
        }
	/**
	 * Run the extended Euclidean algorithm on this and other
	 *
     * @param x
     * @param y
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */

	 public static LargeInteger XGCD(LargeInteger x, LargeInteger y) {
		// YOUR CODE HERE (replace the return, too...)

                if(x.zero()){
                    return y;
								}
                if(y.zero()){
                int count;
                for(count = 0; x.or(y).and().zero();count++){
                    x = x.rShift(1);
                    y = y.rShift(1);
                }
								int z = 0;
                while(x.and().zero()){
                    x = x.rShift(1);
										z++;
										if( z > 5)
										break;
                }
                do{
                    while(y.and().zero()){
                        y = y.rShift(1);
                    }
										if(x.comp(y) == 1){
											LargeInteger t = y;
											y = x;
											x = t;
										}
										y = y.subtract(x);
                }while(!y.zero());
                return x.lShift(count);
							}
							return x;
	 }

	 /**
	  * Compute the result of raising this to the power of power mod modulus
          * @param power
          * @param modulus
	  * @return this^power mod modulus
	  */
	 public LargeInteger modularExp(LargeInteger power, LargeInteger modulus) {
		// YOUR CODE HERE (replace the return, too...)
                boolean modB = (modulus != null);
                LargeInteger end = new LargeInteger(null);
                end = end.add(one);
                LargeInteger b = new LargeInteger(copy(val));
                LargeInteger pC = new LargeInteger(copy(power.getVal()));

                while(!pC.zero()){
                    if(!pC.even()){
                        if(modB)
                            end = end.multiply(b).mod(modulus);
                        else
                            end = end.multiply(b);
                    }
                    pC = pC.rShift(1);
                    if(modB)
                        b = b.multiply(b).mod(modulus);
                    else
                        b = b.multiply(b);
                }
                return end;
	 }
}
