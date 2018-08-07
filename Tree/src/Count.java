import java.util.Scanner;

public class Count {
    public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	Tree<String,Integer> db = new Tree<>();
	while (sc.hasNext()) {
	    String word = sc.next();
	    Integer count = db.find(word);
	    if(count == null)
		db.add(word, 1);
	    else
		db.modify(word, count+1);
	}
	System.out.println(db);
    }
}