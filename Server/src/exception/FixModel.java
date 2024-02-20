package exception;

public class FixModel {

	public FixModel(){
		
	}
	
	public void fix(int errno) {
	
		switch(errno) {
		case 100://DUPLICATE_OPTSETNAME
			break;
		case 101://FULL_AUTOSET
			break;
		case 102://MISSING_AUTOSET_SIZE
			break;
		case 103://OPTSET_NOT_FOUND
			break;
		case 104://DUPLICATE_OPTION_TYPE
			break;
		case 105://FULL_OPTSET
			break;
		case 106://MISSING_OPTSET_SIZE
			break;
		case 107://OPTION_NOT_FOUND
			break;
		case 108://CHOICE_NOT_SET
			break;
			
		}
	}
}
