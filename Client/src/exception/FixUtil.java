package exception;


public class FixUtil {

	public FixUtil() {
		
	}
	
	public void fix(int errno) {
		switch(errno) {
		case 0://MISSING_HEADER
			break;
		case 1://BAD_HEADER
			break;
		case 2://MISSING_SETLINE
			break;
		case 3://BAD_SETLINE
			break;
		case 4://MISSING_OPTLINE
			break;
		case 5://BAD_OPTLINE
			break;
		}
	}
}
