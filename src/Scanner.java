import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/*
 * Will McMurtry
 * CSE 6341
 * Project1
 */

// class to hold whatever the current token is
class current{
	public static Token token = null;
}


public class Scanner{

	// Creates a buffered reader that reads in from stdin
	static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	
	// Gets next token as described in project 1 assignment
	public static Token getNextToken(){
		
		// Creates a new token with dummy values
		Token currentToken = new Token();

		// stdin.read() returns an int value
		int r = 0;

		// stdin.read() must be wrapped in try-catch statement
		try{
			r = stdin.read();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		// cast the result of stdin.read() to a char
		char c = (char) r;
	
		// stdin returns -1 if it reaches EOF	
		if (r == -1){

			// Assign token type and dummy values
			currentToken.setType("EOF");
			currentToken.setValue("");
			currentToken.setIntValue(-1);
			return currentToken;
		}

		// consumes whitespace
		if (c == '\r' || c == '\n' || c == ' '){
			while (c == '\r' || c == '\n' || c == ' '){

				// read and cast
				try {
					r = stdin.read();
				}
				catch(IOException e){
					e.printStackTrace();
				}

				c = (char) r;
			}
		}
		
		// check again for EOF
		if (r == -1){
			currentToken.setType("EOF");
			currentToken.setValue("");
			currentToken.setIntValue(-1);
			return currentToken;
		}

		// handles case of left parentheses
		if (c == '('){
			currentToken.setType("openParen");
			currentToken.setValue("(");
			currentToken.setIntValue(-1);
			return currentToken;
		}
	
		// handles case of right parentheses
		if (c == ')'){
			currentToken.setType("closeParen");
			currentToken.setValue(")");
			currentToken.setIntValue(-1);
			return currentToken;
		}

		// if token is letter, consume all following letters and/or digits
		if (Character.isLetter(c)){
		
			// append following chars to empty string	
			String tokenAsString = "";
			while (Character.isLetter(c) || Character.isDigit(c)){

				// mark location of reader in case we consume one token too many
				// must be wrapped in try-catch
				try{
					stdin.mark(1);
				}
				catch(IOException e){
					e.printStackTrace();
				}
				
				// cast char to string and append
				tokenAsString += Character.toString(c);

				//read next char and cast it
				try {
					r = stdin.read();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

				c = (char) r;
			}

			// since we consumed a token that wasn't a letter or char
			// reset so that char can be correctly consumed
			// reset must be wrapped in try-catch
			try{
				stdin.reset();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		
			// assign token type and value	
			currentToken.setType("literalAtom");
			currentToken.setValue(tokenAsString);
			currentToken.setIntValue(-1);

			return currentToken;
		}

		// if char is a digit, consume all following digits
		if (Character.isDigit(c)){

			// append chars to empty string
			String tokenAsString = "";

			// consume digits
			while (Character.isDigit(c) || Character.isLetter(c)){

				// mark in case we consume extra token
				try {
					stdin.mark(1);
				}
				catch(IOException e) {
					e.printStackTrace();
				}

				// cast char and append
				tokenAsString += Character.toString(c);
				
				// read char and cast
				try{
					r = stdin.read();
				}
				catch(IOException e) {
					e.printStackTrace();
				}

				c = (char) r;
			}

			// If tokenAsString cannot be parsed to an int
			// then we know there is an error
			try {
				Integer.parseInt(tokenAsString);
			} catch (NumberFormatException e){
				currentToken.setType("ERROR");
				currentToken.setValue(tokenAsString);
				return currentToken;
			}
		

			try {
				stdin.reset();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			// If token correctly formed
			// then assign values
			currentToken.setType("numericAtom");
			currentToken.setValue(tokenAsString);
			currentToken.setIntValue(Integer.parseInt(tokenAsString));

			return currentToken;
		}

		// if none of the cases above applied, assume there is an error
		currentToken.setType("ERROR");

		return currentToken;

	
	}

	// sets the current token to the value returned by getNextToken
	public static void init(){
		current.token = getNextToken();
	}
	
	// returns the current token
	public static Token getCurrent(){
		return current.token;
	}

	// advances the current token
	public static void moveToNext(){
		current.token = getNextToken();
	}


	public static void main(String[] args){

		
	}
}
