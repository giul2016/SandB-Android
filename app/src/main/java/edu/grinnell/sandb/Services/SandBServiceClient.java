package edu.grinnell.sandb.Services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;

import edu.grinnell.sandb.Model.Article;
import edu.grinnell.sandb.Model.QueryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.io.IOException;
import java.util.List;


/**
 * Created by albertowusu-asare on 3/13/16.
 */
public class SandBServiceClient {

    public static final String API_URL = "http://www.thesandb.com";


    public void getRecentArticles(int numArticles) throws IOException {
        // Create a very simple REST adapter which points the GitHub API.

        ExclusionStrategy exclusionStrategy = new SandBGsonExclusionStrategy();
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(exclusionStrategy)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Create an instance of our GitHub API interface.
        SandBServiceAPI sandBService = retrofit.create(SandBServiceAPI.class);
        Call<QueryResponse> call = sandBService.posts(numArticles);

        //Asynchronous call made with background thread
        call.enqueue(new Callback<QueryResponse>() {
            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                QueryResponse responseBody = response.body();
                System.out.println(responseBody);
                /*
                List<Article> articles = responseBody.getPosts();
                cacheArticles(articles);
                */
            }

            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                //Log error TODO: determine what actions to take on Failure
            }
        });

       /* Synchronous
        QueryResponse response = call.execute().body(); //TODO determine if to use synchronous
        //return response.getPosts();
        */

    }


    private class SandBGsonExclusionStrategy implements ExclusionStrategy{
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == SugarRecord.class && f.getName().equals("id"));
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
    private void cacheArticles(List<Article> articles){
        for (Article article : articles)
            article.save();
    }


    public static void main(String... args) throws IOException {
        SandBServiceClient sandBService = new SandBServiceClient();
        sandBService.getRecentArticles(3);
    }

}
