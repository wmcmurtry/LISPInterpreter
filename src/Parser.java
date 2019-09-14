/*
 * Will McMurtry
 * CSE 6341
 * Project3
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.File;

class Node{
	String key;
	Node left;
	Node right;

	public Node(String key){
		this.key = key;
		this.left = null;
		this.right = null;
	}
}

class SExpr{
	public static Node currentSExpr = null;
}

public class Parser{

	static int openParenCount = 0;
	static int closeParenCount = 0;


	// method for printing
	static void prettyPrint(Node root){

		if (root.left == null && root.right == null){
			System.out.print(root.key);
		}
		else{
			System.out.print("(");
			prettyPrint(root.left);
			System.out.print(" . ");
			prettyPrint(root.right);
			System.out.print(")");
		}
	}
	
	// a function to parse a list using production rule <List> ::= <Expr><List> | NIL
	static Node parseList(){
	
		// use production <List> ::= NIL	
		if (Scanner.getCurrent().getType().equals("closeParen")){
			Node nil = new Node("NIL");
			return nil;
		}

		// use production <List> ::= <Expr><List>
		else{
			Node left = parseExpr();
			Node right = parseList();
			Node root = new Node("");
			root.left = left;
			root.right = right;
			return root;
		}
	}

	// recursive descent parsing
	// <Expr> ::= atom | ( <List> )
	static Node parseExpr(){

		// use production <Expr> ::= atom
		if (Scanner.getCurrent().getType().equals("numericAtom") || Scanner.getCurrent().getType().equals("literalAtom")){
			Node root = new Node(Scanner.getCurrent().getValue());
			Scanner.moveToNext();
			return root;
		}

		// use production <Expr> ::= ( <List> )
		else if (Scanner.getCurrent().getType().equals("openParen")){
			openParenCount++;
			Scanner.moveToNext(); // consume openParen
			Node root = new Node("");
			Node newRoot = new Node("");

			// in the case of empty list, return NIL here
			if (Scanner.getCurrent().getType().equals("closeParen")){
				root.key = "NIL";
			}
			else{

				// Parse interior nodes I-sub-1 to I-sub-n
				while (!(Scanner.getCurrent().getType().equals("closeParen"))){
					newRoot = parseList();
					Scanner.moveToNext();
					return newRoot;	
				}
			}

			Scanner.moveToNext(); // consume closeParen
			closeParenCount++;
			return root;
		}
		else{
			//report parse error
			//if invalid token
			if (Scanner.getCurrent().getType().equals("ERROR")){
				System.out.println("PARSE ERROR, invalid token" + Scanner.getCurrent().getValue());
				System.exit(1);
			}

			// if unbalanced paranthesis
			if (closeParenCount != openParenCount){
				System.out.println("Unbalanced paranthesis. Open: " + openParenCount + " Close: " + closeParenCount);
				System.exit(1);
			}
			System.out.print("Other error");
			System.exit(1);
			return new Node("");
		}

	}

	public static Node getCurrent(){
		return SExpr.currentSExpr;
	}

	// parse until we reach end of file
	static void parseStart(){
		do{
			Node root = parseExpr();
			SExpr.currentSExpr = root;
			HashMap<String, Node> aList = new HashMap<>();
			root = Interpreter.eval(root, aList);
			Interpreter.pPrint(root);
			System.out.println();
		} while (!(Scanner.getCurrent().getType().equals("EOF")));

	}

	// initialize scanner and start parsing
	public static void main(String[] args) throws FileNotFoundException{
		File file = new File("/Users/mcmurtry/eclipse-workspace/Interpreter/src/input.txt");
		System.setIn(new FileInputStream(file));
		Scanner.init();
		parseStart();
		

	}
}
