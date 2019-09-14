/*
 * Will McMurtry
 * CSE 6341
 * Project3
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

class dList{
	HashMap<String, Node> params;
	HashMap<String, Node> body;
	HashMap<String, Node> aList;
	
		public dList() {
				this.params = new HashMap<>();
				this.body = new HashMap<>();
				this.aList = new HashMap<>();
		}
	
}



class list{
	public static dList currentDlist = new dList();
}

public class Interpreter{
	
	static boolean bound(String x, HashMap<String, Node> aList) {
		return aList.containsKey(x);
	}
	
	static Node getVal(String x, HashMap<String, Node> aList) {
		return aList.get(x);
	}

	static void pPrint(Node current){
		if (current.left == null && current.right == null){
			System.out.print(current.key);
		}
		else{
			System.out.print("(");
			while (current.right != null){
				pPrint(current.left);
				current = current.right;
				if (current.right != null){
					System.out.print(" ");
				}
			}
			if (current.key.equals("NIL")){
				System.out.print(")");
				return;
			}
			else{
				System.out.print(" . " + current.key + ")");
				return;
			}
		}
	}
			


	static boolean tryParseInt(String str){
		try{
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	static Node eq(Node s1, Node s2){
		if (s1.left != null || s1.right != null || s2.left != null || s2.right != null){
			System.out.print("eq undefined if s1 or s2 has length >1.");
			System.exit(1);
		} else{
			if (s1.key.equals(s2.key)){
				return new Node("T");
			} else{
				return new Node("NIL");
			}
		}
		return new Node("");
	}

	static Node plus(Node s1, Node s2){
		if (!tryParseInt(s1.key) || !tryParseInt(s2.key)){
			System.out.println("Cannot do plus on non-numeric atom");
			System.exit(1);
		}
		int sum = Integer.parseInt(s1.key) + Integer.parseInt(s2.key);
		return new Node(Integer.toString(sum));
	}

	static Node minus(Node s1, Node s2){
		if (!tryParseInt(s1.key) || !tryParseInt(s2.key)){
			System.out.println("Cannot do minus on non-numeric atom");
			System.exit(1);
		}
		int sum = Integer.parseInt(s1.key) - Integer.parseInt(s2.key);
		return new Node(Integer.toString(sum));
	}

	static Node times(Node s1, Node s2){
		if (!tryParseInt(s1.key) || !tryParseInt(s2.key)){
			System.out.println("Cannot do times on non-numeric atom");
			System.exit(1);
		}
		int sum = Integer.parseInt(s1.key) * Integer.parseInt(s2.key);
		return new Node(Integer.toString(sum));
	}

	static Node less(Node s1, Node s2){
		if (!tryParseInt(s1.key) || !tryParseInt(s2.key)){
			System.out.println("Cannot do less on non-numeric atom");
			System.exit(1);
		}
		int sum = Integer.parseInt(s1.key) - Integer.parseInt(s2.key);
		if (sum < 0){
			return new Node("T");
		} else{
			return new Node("NIL");
		}
	}

	static Node greater(Node s1, Node s2){
		if (!tryParseInt(s1.key) || !tryParseInt(s2.key)){
			System.out.println("Cannot do greater on non-numeric atom");
			System.exit(1);
		}
		int sum = Integer.parseInt(s1.key) - Integer.parseInt(s2.key);
		if (sum > 0){
			return new Node("T");
		} else{
			return new Node("NIL");
		}
	}


	static Node nulll(Node current){
		if (current.left == null && current.right == null && current.key.equals("NIL")){
			return new Node("T");
		} else {
			return new Node("NIL");
		}
	}

	static Node atom(Node current){
		
		if (current.right == null && current.left == null){
			return new Node("T");
		} else{
			return new Node("NIL");
		}
	}

	static Node intt(Node current){
		if (current.left == null && current.right == null && tryParseInt(current.key)){
			return new Node("T");
		} else{
			return new Node("NIL");
		}
	}

	static Node cons(Node left, Node right){
		Node root = new Node("");
		root.left = left;
		root.right = right;
		return root;
	}


	static Node car(Node current){
		if (current.left == null){
			System.out.println("Cannot call CAR on a tree with one node.");
			System.exit(1);
		}
		return current.left;
	}

	static Node cdr(Node current){
		if (current.right == null || current.key.equals("NIL" )){
			System.out.println("Cannot call CDR on a tree with one node.");
			System.exit(1);
		}
		return current.right;
	}
	
	static Node evlist(Node node, HashMap<String, Node> aList) {
		if (node.key.equals("NIL")) {
			return new Node("NIL");
		}

		return cons(eval(car(node), aList), evlist(cdr(node), aList));
	}

	static int length(Node current){
		if (current.key.equals("NIL")){
			return 0;
		}
		return 1 + length(cdr(current));
	}
	
	static Node apply(Node command, Node root, HashMap<String, Node> aList) {
		
		if (atom(command).key.equals("NIL")) {
			System.out.println("Error: Non-atomic expression");
			System.exit(1);
		}

		
		String expression = command.key;
		
		if (expression.equals("CAR")){
			if (length(root) != 1){
				System.out.print("CAR undefined when length != 1");
				System.exit(1);
			}

			return car(car(root));//, aList));
		}

		if (expression.equals("CDR")) {
			if (length(root) != 1){
				System.out.print("CDR undefined when length != 1");
				System.exit(1);
			}
			return cdr(car(root));//, aList));
		}

		if (expression.equals("CONS")){
			if (length(root) != 2){
				System.out.print("CONS undefined when length != 2");
				System.exit(1);
			}
			return cons(car(root), car(cdr(root)));
		}
		
		if (expression.equals("PLUS")){
			if (length(root) != 2){
				System.out.print("PLUS undefined when length != 2");
				System.exit(1);
			}
			return plus(car(root), car(cdr(root)));

		}

		if (expression.equals("MINUS")){
			if (length(root) != 2){
				System.out.print("MINUS undefined when length != 2");
				System.exit(1);
			}
			return minus(car(root), car(cdr(root)));
		}

		if (expression.equals("TIMES")){
			if (length(root) != 2){
				System.out.print("TIMES undefined when length != 2");
				System.exit(1);
			}
			return times(car(root), car(cdr(root)));
			
		}

		if (expression.equals("LESS")){
			if (length(root) != 2){
				System.out.print("LESS undefined when length != 2");
				System.exit(1);
			}
			return less(car(root), car(cdr(root)));			
		}

		if (expression.equals("GREATER")){
			if (length(root) != 2){
				System.out.print("GREATER undefined when length != 2");
				System.exit(1);
			}
			return greater(car(root), car(cdr(root)));			
		}

		if (expression.equals("EQ")){
			if (length(root) != 2){
				System.out.print("EQ undefined when length != 2");
				System.exit(1);
			}
			return eq(car(root), car(cdr(root)));			
		}

		if (expression.equals("ATOM")){
			if (length(root) != 1){
				System.out.print("ATOM undefined when length != 1");
				System.exit(1);
			}
			
			return atom(car(root));
		}
		
		if (expression.equals("INT")){
			if (length(root) != 1){
				System.out.print("INT undefined when length != 1");
				System.exit(1);
			}
			return intt(car(root));
		}

		if (expression.equals("NULL")){
			if (length(root) != 1){
				System.out.print("NULL undefined when length != 1");
				System.exit(1);
			}
			return nulll(car(root));
		}
		
		//pPrint(car(list.currentDlist.body.get(expression)));
		//pPrint(list.currentDlist.params.get(expression));
		//pPrint(root);
		
		return eval(car(list.currentDlist.body.get(expression)),
				addPairs(list.currentDlist.params.get(expression), root, aList));
		
		//return new Node("");
	}
	
	static HashMap<String, Node> addPairs(Node params, Node root, HashMap<String, Node> aList){
		if (length(params) != length(root)) {
			System.out.println("Number of Formal parameters must be equal to actual parameters");
			System.exit(1);
		}
		while (!root.key.equals("NIL")) {
			aList.put(car(params).key, car(root) );
			root = cdr(root);
			params = cdr(params);
		}
		
		return aList;
	}
	

	public static Node eval(Node root, HashMap<String, Node> aList){
		
		//pPrint(root);
		
		if(atom(root).key.equals("T")) {
		
			if (root.key.equals("T")){
				return root;
			}
			if (root.key.equals("NIL")){
				return root;
			}
			if (intt(root).key.equals("T")){
				return root;
			}
			
			//pPrint(root);
			//System.out.println();
			//pPrint(aList.getOrDefault(root.key, new Node("ERROR")));
			//System.out.println(getVal)
			
			if(bound(root.key, aList)) {
				return getVal(root.key, aList);
			}
			
			//pPrint(root);
			
			System.out.println("UNBOUND LITERAL");
			System.exit(1);

		}
		
		//if (length(root) < 2){
			//System.out.println("EVAL undefined because length < 2.");
			//System.exit(1);
		//}

		String command = car(root).key;
		Node commandNode = car(root);
		
		if (command.equals("QUOTE")){
			if (length(root) != 2){
				System.out.print("QUOTE undefined when length != 1");
				System.exit(1);
			}
			return car(cdr(root));
		}
		
		if (command.equals("COND")){

			Node copy2 = cdr(root);
			while (copy2 != null){
				String test = "";
				if (copy2.left != null){
					test = atom(copy2.left).key;
				}
				if (copy2.left != null && length(copy2.left) != 2) {
					System.out.println("Some s-sub-i is not of length 2");
					System.exit(1);
				}
				if (test.equals("T") && !copy2.key.equals("NIL")){
					System.out.println("Some s-sub-i is not a list, COND undefined.");
					System.exit(1);
				}
				copy2 = copy2.right;
			}
			int iterations = length(root) - 1;
			Node copy = cdr(root);
			for (int i = 0; i < iterations; i++){
				if (length(car(copy)) != 2 ){
					System.out.println("Length of s-sub-i in cond != 2");
					System.exit(1);
				}
				if (atom(copy).key.equals("T")){
					System.out.println("s-sub-i is not a list in cond expression.");
					System.exit(1);
				}
				if (!eval(car(car(copy)), aList).key.equals("NIL")){
					return eval(car(cdr(car(copy))), aList);
				} else {
					copy = cdr(copy);
				}
			}
			System.out.println("No b evaluated to non-NIL, COND expression undefined.");
			System.exit(1);
		}	
		
		if (command.equals("DEFUN")) {
			Set<String> defined = new HashSet<>();
			defined.add("T");
			defined.add("NIL");
			defined.add("CAR");
			defined.add("CDR");
			defined.add("CONS");
			defined.add("ATOM");
			defined.add("EQ");
			defined.add("NULL");
			defined.add("INT");
			defined.add("PLUS");
			defined.add("MINUS");
			defined.add("TIMES");
			defined.add("LESS");
			defined.add("GREATER");
			defined.add("COND");
			defined.add("QUOTE");
			defined.add("DEFUN");
			
			if (defined.contains(car(cdr(root)).key)) {
				System.out.println("function name is already taken");
				System.exit(1);
			}

			
			int length = length(car(cdr(cdr(root))));
			Node params = car(cdr(cdr(root)));
			Set<String> parameters = new HashSet<String>();
			for (int i = 0; i < length; i++) {
				String param = car(params).key;
				if (!parameters.contains(param)) {
					parameters.add(param);
				}
				else {
					System.out.println("Duplicate parameters in DEFUN expression");
					System.exit(1);
				}
				if (defined.contains(param)) {
					System.out.println("parameter's name is already taken");
					System.exit(1);
				}
				params = cdr(params);	
			}
			// print out items in the set, check they aren't defined

			list.currentDlist.params.put(car(cdr(root)).key, car(cdr(cdr(root))));
			list.currentDlist.body.put(car(cdr(root)).key, cdr(cdr(cdr(root))));
			
			System.out.print(car(cdr(root)).key);
			return new Node("");
			
			}
		
		return apply(commandNode, evlist(cdr(root), aList), aList);

	}


	public static void main(String[] args) throws FileNotFoundException{
		File file = new File("/Users/mcmurtry/eclipse-workspace/Interpreter/src/input.txt");
		System.setIn(new FileInputStream(file));
		Scanner.init();
		Parser.parseStart();
		
	}
}
