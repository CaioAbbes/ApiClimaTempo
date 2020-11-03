package com.example.apiclimatempo;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PegarCod {
    private static final String LOG_TAG = PegarCod.class.getSimpleName();
    // Constantes utilizadas pela API
    // URL para a API de metereologia.
    //                                      http://dataservice.accuweather.com/locations/v1/cities/search?apikey=uS4MACAkFEZHV7pqqM1HDPocakID0704&q=S%C3%A3o%20Paulo

    private static final String TEMPO_URL = "https://dataservice.accuweather.com/locations/v1/cities/search?";
    private static String PREVTEMPO_URL = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    private static final String GEOURL = "https://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
    private static String ATUALCONDICAOCIDADE = "https://dataservice.accuweather.com/currentconditions/v1/";
    // Parametros da api key, necessário para consultar
    private static final String API_KEY = "apikey"; //QUERY_PARAM
    private static final String CHAVE = "iyxyqiV6ZEwnFrs32cXHLb7beiu1z00z"; //QUERY_PARAM

    private static final String METRIC = "metric";
    // Parametro de pesquisa que será enviado pelo usuário
    private static final String QUERY_PARAM= "q"; //maxResults
    // Parametro do idioma
    private static final String LANGUAGE = "language";

    // Parametro se deve retornar detalhes completos
    private static final String DETAILS = "details";
    // Parametro de toplevel
    private static final String toplevel = "toplevel";
    private static final String ID = "";
    static String buscaCodigo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String cod = null;
        String Chave=null;
        // Construção da URI de Busca
        Uri builtURI;
        String CodigoJSONString = null;
        Log.d("asdas0", "asd");
        try {

            builtURI = Uri.parse(TEMPO_URL).buildUpon()
                    .appendQueryParameter(API_KEY, CHAVE)
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")
                    .build();
            Log.d("Chgeou", "link pega cod" + builtURI);
            // Converte a URI para a URL.

            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            Log.d("asdas3", "asd" + urlConnection);
               // urlConnection.setRequestMethod("GET");
            //ERRO AQUI
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();
            JSONArray itemsArray = new JSONArray(CodigoJSONString);
            JSONObject obj = itemsArray.getJSONObject(0);
            Log.d("azul","Chegou aquiii");
            Chave = obj.getString("Key");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log

        return Chave;
    }

    static String[] buscaCodigoLocal(String lat, String lon) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String cod = null;
        String Chave=null;
        String Cidade= null;
        // Construção da URI de Busca
        String coord= lat+","+lon;
        Uri builtURI;
        String CodigoJSONString = null;
        try {

            builtURI = Uri.parse(GEOURL).buildUpon()
                    .appendQueryParameter(API_KEY, CHAVE)
                    .appendQueryParameter(QUERY_PARAM, coord)
                    .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")
                    .appendQueryParameter(toplevel, "true")
                    .build();
            // Converte a URI para a URL.

            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            Log.d("Acesso", "PegarCod" + urlConnection);
            // urlConnection.setRequestMethod("GET");
            //ERRO AQUI
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();
            JSONObject obj = new JSONObject(CodigoJSONString);
            Log.d("Acesso","Conectou cod");
            Chave = obj.getString("Key");
            Cidade = obj.getString("LocalizedName");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log
        //Log.d(LOG_TAG, CodigoJSONString);

        String [] ArraysVolta = new String[2];
        ArraysVolta[0] = Chave;
        ArraysVolta[1] = Cidade;
        return ArraysVolta;
    }

    public static String buscaTemperatura(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String CodigoJSONString = null;
        String Codigo = null;
        Codigo = buscaCodigo(queryString);
        Uri builtURI1;
        // Construção da URI de Busca
        try {
            ATUALCONDICAOCIDADE= ATUALCONDICAOCIDADE.concat(Codigo);
            builtURI1= Uri.parse(ATUALCONDICAOCIDADE).buildUpon()
                    .appendQueryParameter(API_KEY, CHAVE)
                    .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")

                    .build();
            Log.d("asdas", "asd" + builtURI1);

            // Converte a URI para a URL.
            URL requestURL = new URL(builtURI1.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log
       // Log.d(LOG_TAG, CodigoJSONString);
        Log.d("JSOS", "JSON ATUALCIDADE" + CodigoJSONString);
        return CodigoJSONString;

    }

    //Nao eh o ideal, mas por falta de tempo colaremos o mesmo método com a diferença de uma linha
    static String[] buscaTemperaturaLocal(String lat, String lon) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String CodigoJSONString = null;
        String Codigo;
            Codigo = buscaCodigoLocal(lat, lon)[0];
        Log.d("Acesso", "COD: " + Codigo);
        String Cidade = buscaCodigoLocal(lat, lon)[1];
        Uri builtURI1;
        // Construção da URI de Busca
        try {
            PREVTEMPO_URL= PREVTEMPO_URL.concat(Codigo);
            builtURI1= Uri.parse(PREVTEMPO_URL).buildUpon()
                    .appendQueryParameter(API_KEY, CHAVE)
                    .appendQueryParameter(LANGUAGE, "pt-br")
                    .appendQueryParameter(DETAILS, "false")
                    .appendQueryParameter(METRIC, "true")
                    .build();
            Log.d("Acesso", "linkprev" + builtURI1);
            // Converte a URI para a URL.
            URL requestURL = new URL(builtURI1.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            Log.d("Acesso", "linkprev" + urlConnection);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Busca o InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            CodigoJSONString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
        // escreve o Json no log
        //Log.d(LOG_TAG, CodigoJSONString);
        String [] ArraysVolta = new String[2];
        Log.d("Acesso", "JSON FORECAS" + CodigoJSONString);
        ArraysVolta[0] = CodigoJSONString;
        ArraysVolta[1] = Cidade;
        return ArraysVolta;

    }
}

