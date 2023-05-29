package org.huminlabs.huminlabplugin.Backend;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public final class BackendRequestHandler {
    private HuMInLabPlugin plugin;
    private String url;
    private String token;

    public BackendRequestHandler(HuMInLabPlugin plugin, String url, String authorization) {
        this.plugin = plugin;
        this.url = url;
        this.token = authorization;
    }




    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }



    public JsonObject sendRequest(String queryString) throws URISyntaxException, IOException {

            //TODO
            // When changing data, you need to first get the data from the server, edit it, then send it back
            // This is so that you don't overwrite other data that may have been changed since you last got it
            String line;
            CloseableHttpClient client = HttpClientBuilder.create().build();
            CloseableHttpResponse response = null;

            HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "*/*");
            post.addHeader("Authorization", token);

        try{
            JsonObject json = new JsonObject();
            json.addProperty("query", queryString);

            post.setEntity(new StringEntity(json.toString()));

            response = client.execute(post);

            BufferedReader buffReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();

            while((line = buffReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
          //  System.out.println(builder.toString());
            String jsonString = builder.toString();
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
            return jsonObject;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void objectiveUpdate(String uuid, String unit, String objectiveID) {
        System.out.println("Updating Backend with objectiveID: " + objectiveID);
        String queryString = "mutation MyMutation {" +
                                "\ncreateUserData(userID: \""+ uuid +"\", key: \"myUserData\", data: \"{\\\"minecraft\\\": { \\\"unitID\\\": \\\""+unit+"\\\",\\\"objectiveID\\\": \\\""+objectiveID+"\\\"}}\")" +
                            "\n}";
//        }
        try {
            System.out.println(sendRequest(queryString));
        } catch (URISyntaxException | IOException e) {
            System.out.println("Error sending request to backend");
        }

        //TODO
        // Send notification to Drew-Backend that a user has advanced to a new objective
    }

    public void userBackendUpdate(String uuid, String name){
        System.out.println("Updating Backend with userID: " + uuid);
        String queryString = "mutation MyMutation {" +
                                "\ncreateUser(userID:\""+ uuid +"\", accessLevel: \"STUDENT\", groups: [\"G1\", \"G2\"], email: \""+name+"\") {" +
                                    "\nuserID" +
                                    "\nemail"+
                                    "\ndateCreated" +
                                    "\nlastUpdate" +
                                    "\naccessLevel" +
                                    "\ngeneratedID" +
                                    "\ngroups" +
                                "\n}" +
                            "\n}";
        try {
            System.out.println(sendRequest(queryString));
        } catch (URISyntaxException | IOException e) {
            System.out.println("Error sending request to backend");
        }

        //TODO
        // Send notification to Drew-Backend that a new user has been created/joined
    }
}