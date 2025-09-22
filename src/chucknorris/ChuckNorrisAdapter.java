// Adapter: traduce la API externa a nuestra interfaz JokeService
import org.json.JSONObject; //descargar json-20230618.jar

public class ChuckNorrisAdapter implements JokeService {
    private ChuckNorrisAPI api;

    public ChuckNorrisAdapter() {
        this.api = new ChuckNorrisAPI();
    }

    @Override
    public String getJoke() {
        String json = api.fetchJoke();
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getString("value"); // campo "value" trae el chiste
        } catch (Exception e) {
            return "Error procesando JSON: " + e.getMessage();
        }
    }
}
