package externplugin;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Element;

import com.valarion.gameengine.core.VarLong;
public class ConstantTen implements VarLong {

	@Override
	public long getLong() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public void load(Element node, Object context) throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
