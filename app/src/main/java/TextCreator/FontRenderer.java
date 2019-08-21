package TextCreator;

import java.util.HashMap;
import java.util.Map;

public class FontRenderer {

    public static final float PIXEL_SIZE = 1.0f / 512.0f;

    Map<Integer, Characters> charactersData = new HashMap<Integer, Characters>();

    public void addCharacterData(Characters charDat){
        charactersData.put(charDat.id,charDat);
    }

    public Characters getCharacter(int id){
        return charactersData.get(id);
    }

}
