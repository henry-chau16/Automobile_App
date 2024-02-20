package exception;

//Enum for all AutoException errors. Used to designate error type when handling AutoExceptions
public enum Error {

	//util package errors (reserved errno # 0-99)
	MISSING_HEADER(0, "Header is empty\n"),
	BAD_HEADER(1, "Illegal Header Format\n"),
	MISSING_SETLINE(2,"Missing Setline\n"),
	BAD_SETLINE(3,"Illegal Setline Format\n"),
	MISSING_OPTLINE(4, "Missing Option Line\n"),
	BAD_OPTLINE(5, "Illegal Option Line Format\n"),
	BAD_PROP(6, "Error parsing properties file"),
	
	//model package errors (reserved errno # 100-199)
	DUPLICATE_OPTSETNAME(100, "***OptionSet of the same name already exists\n"),
	//FULL_AUTOSET(101,"***Set is full\n"), retired from version 3 & onward
	EMPTY_AUTOSET(102, "***Set is Empty\n"),
	OPTSET_NOT_FOUND(103, "***Unable to find Option Set\n"),
	DUPLICATE_OPTION(104, "***Option of the same type already exists in this set\n"),
	//FULL_OPTSET(105, "***Option Set is full\n"), retired from version 3 & onward
	EMPTY_OPTSET(106, "***Option Set is Empty\n"),
	OPTION_NOT_FOUND(107, "***Option not found in Option Set\n"),
	CHOICE_NOT_SET(108, "***Choice has not been selected for this Option Set\n");
	
	private final int errno;
	private final String errmsg;
	
	Error(int errno, String errmsg) {
		this.errno = errno;
		this.errmsg = errmsg;
	}
	
	public int getErrNo() {
		return errno;
	}
	
	public String getErrMsg() {
		return errmsg;
	}
}

