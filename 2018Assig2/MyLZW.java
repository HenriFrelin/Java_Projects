/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package mylzw;

/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9;         // codeword width
    private static boolean ratioCheck = false;
    private static double ratio = 0;
    private static double ratioHold = 0;
    private static String setting = "n";

    public static void compress() {
        int in = 0;
        int out = 0;
        if(setting.equals("n"))  // check if this is needed??
            BinaryStdOut.write('n', 8);
        if(setting.equals("m"))
            BinaryStdOut.write('m', 8);
        if(setting.equals("r"))
            BinaryStdOut.write('r', 8);
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
            in += t*8;
            out += W;
            ratio = in/out;
            L = (int)Math.pow(2, W); //codewidth resizing


            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);

            if((int)Math.pow(2, W) == code && W < 16){
                W++; // increment codeword width
                L = (int)Math.pow(2, W);
                st.put(input.substring(0, t + 1), code++);
            }
            // no special case for N as N mode does not alter the functionality further
            if(setting.equals("r") && code == (int)Math.pow(2, 16)){
                st = new TST<Integer>();
                int i = 0;
                while(i < R){ // TEST HERE
                     st.put("" + (char) i, i);
                     i++;
                }
                L = 512;
                W = 9;
                code = R + 1;
            }
            if(setting.equals("m") && code == (int)Math.pow(2, 16)){
                if(!ratioCheck){
                    ratioHold = ratio;
                    ratioCheck = true;
                }
                double newRatio = ratioHold/ratio;
                if(newRatio > 1.1){
                    st = new TST<Integer>();
                    int i = 0;
                    while(i < R){
                         st.put("" + (char) i, i);
                         i++;
                    }
                    L = 512;
                    W = 9;
                    code = R + 1;
                    ratio = 0;
                    ratioHold = 0;
                    ratioCheck = false;
                }
            }

            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }

    public static void expand() {
        int in = 0;
        int out = 0;
        char set = BinaryStdIn.readChar(8);
        if(set == 'n')  // reverse of compression mode setting!
            setting = "n";
        if(set == 'm')
            setting = "m";
        if(set == 'r')
            setting = "r";
        System.out.println(setting);
        String[] st = new String[(int)Math.pow(2, 16)];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            in += val.length()*8;
            out += W;
            ratio = in/out;
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);

            if (codeword == R) break;
            String s = st[codeword];
            if(i == codeword) s = val + val.charAt(0);   // special case hack
            if(i < L-1) st[i++] = val + s.charAt(0);
            if(W < 16 && i == L-1){
                W = W + 1;
                st[i++] = val + s.charAt(0); // same as line 141
                L = (int)Math.pow(2, W);
            }
            val = s;
            if(i == (int)Math.pow(2, 16) && setting.equals("m")){
                if(!ratioCheck){
                    ratioHold = ratio;
                    ratioCheck = true;
                }
                double newRatio = ratioHold/ratio;
                System.err.println(newRatio);
                if(newRatio > 1.1){
                    st = new String[(int)Math.pow(2, 16)];
                    //int i = 0;
                    while(i < R){ // TEST HERE
                         st[i] = "" + (char)i;
                         i++;
                    }
                    st[i++] = "";
                    L = 512;
                    W = 9;
                    codeword = BinaryStdIn.readInt(W);
                    if (codeword == R)
                        return;
                    val = st[codeword];
                    ratio = 0;
                    ratioHold = 0;
                    ratioCheck = false;
                }
            }
            if(i == (int)Math.pow(2, 16) && setting.equals("r")){ // TEST W/O CODE HERE /////////////
                st = new String[(int)Math.pow(2, 16)];
                //int i = 0;
                while(i < R){ // TEST HERE
                     st[i] = "" + (char)i;
                     i++;
                }
                st[i++] = "";
                L = 512;
                W = 9;
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R)
                    return;
                val = st[codeword];

            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("+")){
          //System.out.println("Expanding...");
            expand();
        }
        if (args[0].equals("-") && args[1].equals("n")){
            //System.out.println("Compressing in Do Nothing Mode...");
            setting = "n";
            compress();
        }
        if (args[0].equals("-") && args[1].equals("m")){
            //System.out.println("Compressing in Monitor Mode...");
            setting = "m";
            compress();
        }
        if (args[0].equals("-") && args[1].equals("r")){
            //System.out.println("Compressing in Reset Mode...");
            setting = "r";
            compress();
        }

        //else throw new IllegalArgumentException("Illegal command line argument");
        //compress();
        System.exit(0);
    }

}
