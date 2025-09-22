import org.json.JSONObject;

public class ChuckNorrisAdapter implements JokeService {
    private ChuckNorrisAPI api;

    public ChuckNorrisAdapter() {
        this.api = new ChuckNorrisAPI();
    }

    @Override
    public String getJoke() {
        String json = api.fetchJoke();
        try {
            // Obtener el chiste en inglés de la respuesta JSON
            JSONObject obj = new JSONObject(json);
            String jokeInEnglish = obj.getString("value");

            // Traducir el chiste a español utilizando MyMemoryTraductor
            MyMemoryTraductor translator = new MyMemoryTraductor(jokeInEnglish, "en", "es");
            String jokeInSpanish = translator.getJoke();

            // Devolver el chiste en ambos idiomas (inglés y español)
            return "Inglés: " + jokeInEnglish + "\nEspañol: " + jokeInSpanish;

        } catch (Exception e) {
            return "Error procesando el chiste o traduciendo: " + e.getMessage();
        }
    }
}
