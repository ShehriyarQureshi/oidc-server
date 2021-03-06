/*
 * Copyright (c) 2019 Muhammad Shehriyar Qureshi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.ufone.api.authorization_code;

import com.ufone.api.request.AuthorizationServerRequest;
import javax.ws.rs.core.Response;
import org.apache.commons.text.RandomStringGenerator;

import java.sql.*;

import java.sql.SQLException;
import java.lang.ClassNotFoundException;

import java.util.Properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * Generates authorization code and returns a string containing redirect_uri along with
 * authorization code and parameters that are required to be present.
 */
public class AuthorizationCodeResponse {
        private String responseString;

        /*
         * Generate a random code
         *
         * @return authorization code
         */
        public String generateCode() {
                char[][] codeFormat = {{'0', '9'}, {'A', 'Z'}, {'a', 'z'}};
                RandomStringGenerator generator =
                    new RandomStringGenerator.Builder().withinRange(codeFormat).build();
                String code = generator.generate(30);
                return code;
        }

        /*
         * Build a response object to return to the client
         *
         * @param state state value passed as query parameter by client
         *
         * @param nonce nonce value passed as query paramteer by client
         *
         * @param correlationID correlation_id passed as query parameter by client
         *
         * @return Response object containing redirect_uri passed as query paramter by client along
         *     with authorization code, state, nonce and correlation_id if passed, as query
         *     parameters.
         */
        public Response buildResponse(String redirectURI, String authorizationCode, String state,
            String nonce, String correlationID) {
                if (correlationID == null || correlationID.equals("")) {
                        responseString = String.format("%s?code=%s&state=%s&nonce=%s", redirectURI,
                            authorizationCode, state, nonce);
                } else {
                        responseString =
                            String.format("%s?code=%s&state=%s&nonce=%s&correlation_id=%s",
                                redirectURI, authorizationCode, state, nonce, correlationID);
                }
                return Response.status(302).header("Location", responseString).build();
        }

        public void insertToDatabase(String authorizationCode, AuthorizationServerRequest request) {
                int rowsInserted;
                Properties properties = new Properties();
                Connection connection = null;

                // Load values from config.properties, throw exception if something goes wrong
                try {
                        properties.load(this.getClass().getClassLoader().getResourceAsStream(
                            "/config.properties"));
                } catch (Exception e) {
                        // raise appropriate exception and catch it in the handler to call the
                        // correct response class
                }
                try {
                        // this shouldn't be required on newer versions but this project doesn't
                        // seem to work without this for me
                        Class.forName(properties.getProperty("databaseDriver"));

                        connection = DriverManager.getConnection(
                            properties.getProperty("CodeDatabaseConnection"),
                            properties.getProperty("databaseUser"),
                            properties.getProperty("databaseUserPassword"));
                        PreparedStatement statement = connection.prepareStatement(
                            properties.getProperty("saveCodeToDatabase"));
                        statement.setString(1, authorizationCode);
                        statement.setString(2, request.getRedirectURI());
                        statement.setString(3, request.getClientID());
                        statement.executeUpdate();
                        statement.close();
                } catch (Exception e) {
                        // exception here
                }
        }
}
