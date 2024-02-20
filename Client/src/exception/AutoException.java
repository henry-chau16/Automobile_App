package exception;

//Custom exception class that uses the Error Enum to designate error types
public class AutoException extends Exception{

	private int errno;
	private String errmsg;
	private static final long serialVersionUID = 1L;
	private Error e;
	
	public AutoException() {
		super();
	}
	public AutoException(Error e) {
		super();
		errno = e.getErrNo();
		errmsg = e.getErrMsg();
		this.e = e;
	}
	
	public int getErrNo(){
		return errno;
	}
	
	public String getErrMsg(){
		return errmsg+" Error code: "+errno;
	}
	
	public Error getError() {
		return e;
	}
	public void setErr(Error e) {
		errno = e.getErrNo();
		errmsg = e.getErrMsg();
		this.e = e;
	}
	
	public void print() {
		System.out.println();
		System.out.print(errmsg+"   Error code ");
		System.out.println(errno);
		System.out.println();
	}
	
	
	public void fix() {
		FixUtil fix1 = new FixUtil();
		FixModel fix2 = new FixModel();
		
		switch(e){
		
		//util errors
		case MISSING_HEADER:
			fix1.fix(0);
			break;
		
		case BAD_HEADER:
			fix1.fix(1);
			break;
			
		case MISSING_SETLINE:
			fix1.fix(2);
			break;
			
		case BAD_SETLINE:
			fix1.fix(3);
			break;
			
		case MISSING_OPTLINE:
			fix1.fix(4);
			break;
			
		case BAD_OPTLINE:
			fix1.fix(5);
			break;
			
		case BAD_PROP:
			fix1.fix(6);
			break;
			
		//model errors	
		case DUPLICATE_OPTSETNAME:
			fix2.fix(100);
			break;
			
		case EMPTY_AUTOSET:
			fix2.fix(102);
			break;
			
		case OPTSET_NOT_FOUND:
			fix2.fix(103);
			break;
			
		case DUPLICATE_OPTION:
			fix2.fix(104);
			break;
			
		case EMPTY_OPTSET:
			fix2.fix(106);
			break;
			
		case OPTION_NOT_FOUND:
			fix2.fix(107);
			break;
			
		case CHOICE_NOT_SET:
			fix2.fix(108);
			break;
		
			
		}
		
	}
}
