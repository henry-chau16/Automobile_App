package adapter;
import scale.*;
// does not implement AutoServer interface because it is in the server package, not client package
public class BuildAuto extends proxyAutomobile implements CreateAuto, UpdateAuto, ChooseAuto, EditThread{

}
