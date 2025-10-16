package chucknorris;

import org.json.JSONObject;

public class ChuckNorrisAdapter implements JokeService {
    private ChuckNorrisAPI api;

    // ✅ Constructor que inicializa la API
    public ChuckNorrisAdapter() {
        this.api = new ChuckNorrisAPI();
    }

    @Override
    public String getJoke() {
        String json = api.fetchJoke();
        try {
            JSONObject obj = new JSONObject(json);
            String jokeInEnglish = obj.getString("value");

            MyMemoryTraductor translator = new MyMemoryTraductor(jokeInEnglish, "en", "es");
            String jokeInSpanish = translator.getJoke();

            return String.format("Inglés: %s\nEspañol: %s", jokeInEnglish, jokeInSpanish);
        } catch (Exception e) {
            return "Error al procesar el chiste o traducir: " + e.getMessage();
        }
    }
}
