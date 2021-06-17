package logic;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Configs {
	
	public static final int GRID_EDGE = 30;
	public static final double WALL_SIZE = Constants.GAME_SIZE / Configs.GRID_EDGE; //24
	public static final double TANK_SIZE = WALL_SIZE * 2; 		//48
	public static final double TRAP_SIZE = WALL_SIZE;			//24
	
	public static final double TANK_SPEED = WALL_SIZE / 1;		//24
	public static final double MISSILE_SPEED = WALL_SIZE / 10;	//2.4
	public static final int MAX_LIVES = 3;
	public static final int MAX_TRAPS = 2;
	public static final JSONArray matrices;
	
	static {
		String resourceName = "/conf/Map.json";
        InputStream is = Configs.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }
        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        
        matrices = object.getJSONArray("MAPS");
	}
	public static int[][] getFakeMap(int code) {
		int[][] result = new int[Configs.GRID_EDGE][Configs.GRID_EDGE];
		JSONArray map = (JSONArray) matrices.get(code);
		for (int i = 0; i < Configs.GRID_EDGE; i++) {
			for (int j = 0; j < Configs.GRID_EDGE; j++) {
				result[i][j] = (int) ((JSONArray) map.get(i)).get(j);
			}
		}
		return result;
	}
	
}
