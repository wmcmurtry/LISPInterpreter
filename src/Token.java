public class Token{
	// EOF, openParen, literal, etc
	private String type;

	// DEFUN, X, 23, etc
	private String value;

	// int value for numeric literals 
	private int intValue;

	// Setters
	public void setType(String type){
	    this.type = type;
	}

	public void setValue(String value){
	    this.value = value;
	}

	public void setIntValue(int intValue){
	    this.intValue = intValue;
	}

        // Getters
        public String getType(){
	    return this.type;
	}

        public String getValue(){
	    return this.value;
	}

        public int getIntValue(){
	    return this.intValue;
	}
	
	
	// constructor
	public Token(){
	    this.type = "none";
	    this.value = "";
	    this.intValue = -1;
	}
}
