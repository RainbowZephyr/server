/*
EduMsg is made available under the OSI-approved MIT license.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package edumsg.database.commands.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.server.UID;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.POJONode;
import org.postgresql.util.PSQLException;

import edumsg.database.BCrypt;
import edumsg.database.Command;
import edumsg.database.CommandsHelp;
import edumsg.database.PostgresConnection;
import edumsg.database.User;
import edumsg.shared.MyObjectMapper;

public class LoginCommand implements Command, Runnable {
	private final Logger LOGGER = Logger
			.getLogger(LoginCommand.class.getName());
	private HashMap<String, String> map;

	@Override
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	@Override
	public void execute() {
		Connection dbConn = null;
		CallableStatement proc = null;
		ResultSet set = null; //new
		try {
			String sessionID = URLEncoder.encode(new UID().toString(), "UTF-8");
			dbConn = PostgresConnection.getDataSource().getConnection();
			dbConn.setAutoCommit(false);
			proc = dbConn.prepareCall("{? = call get_password_info(?)}");
			proc.setPoolable(true);
			proc.registerOutParameter(1, Types.VARCHAR);
			proc.setString(2, map.get("username"));
			proc.execute();

			String enc_password = proc.getString(1);
			
			if (enc_password == null) {
				CommandsHelp.handleError(map.get("app"), map.get("method"),
						"Invalid username", map.get("correlation_id"), LOGGER);
				return;
			}
			dbConn.commit();
			proc.close();

			boolean authenticated = BCrypt.checkpw(map.get("password"), enc_password);
			if (authenticated) {
				proc = dbConn.prepareCall("{call login(?,?)}");
				proc.setPoolable(true);
				
				proc.registerOutParameter(1, Types.OTHER); //new

				proc.setString(1, map.get("username"));
				proc.setString(2, sessionID);
				proc.execute();
				
				set = (ResultSet) proc.getObject(1); //new

				MyObjectMapper mapper = new MyObjectMapper();
				JsonNodeFactory nf = JsonNodeFactory.instance;
				ObjectNode root = nf.objectNode();
				root.put("app", map.get("app"));
				root.put("method", map.get("method"));
				root.put("status", "ok");
				root.put("code", "200");
				root.put("session_id", sessionID);
				
				//new
				User user = new User();
				if (set.next()) {
					Integer id = set.getInt(1);
					String username = set.getString(2);
					String email = set.getString(3);
					String name = set.getString(5);
					String language = set.getString(6);
					String country = set.getString(7);
					String bio = set.getString(8);
					String website = set.getString(9);
					Timestamp created_at = set.getTimestamp(10);
					String avatar_url = set.getString(11);
					Boolean overlay = set.getBoolean(12);
					String link_color = set.getString(13);
					String background_color = set.getString(14);
					Boolean protected_tweets = set.getBoolean(15);
					String session_id = set.getString(16);
					
//					String date_of_birth = set.getString(17);
//					String gender = set.getString(18);
					
					user.setId(id);
					user.setUsername(username);
					user.setEmail(email);
					user.setName(name);
					user.setLanguage(language);
					user.setCountry(country);
					user.setBio(bio);
					user.setWebsite(website);
					user.setCreatedAt(created_at);
					user.setAvatarUrl(avatar_url);
					user.setOverlay(overlay);
					user.setLinkColor(link_color);
					user.setBackgroundColor(background_color);
					user.setProtectedTweets(protected_tweets);
					user.setSessionID(session_id);
//					user.setDate_of_birth(date_of_birth);
//					user.setGender(gender);
				}

				POJONode child = nf.POJONode(user);
				root.put("user", child);
				//-------------------------------------

				try {
					CommandsHelp.submit(map.get("app"),
							mapper.writeValueAsString(root),
							map.get("correlation_id"), LOGGER);
				} catch (JsonGenerationException e) {
					//Logger.log(Level.SEVERE, e.getMessage(), e);
				} catch (JsonMappingException e) {
					//Logger.log(Level.SEVERE, e.getMessage(), e);
				} catch (IOException e) {
					//Logger.log(Level.SEVERE, e.getMessage(), e);
				}
			} else {
				CommandsHelp.handleError(map.get("app"), map.get("method"),
						"Invalid Password", map.get("correlation_id"), LOGGER);
			}

		} catch (PSQLException e) {
			CommandsHelp.handleError(map.get("app"), map.get("method"),
					e.getMessage(), map.get("correlation_id"), LOGGER);
			//Logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (SQLException e) {
			CommandsHelp.handleError(map.get("app"), map.get("method"),
					e.getMessage(), map.get("correlation_id"), LOGGER);
			//Logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			//Logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			PostgresConnection.disconnect(null, proc, dbConn);
		}
	}

	@Override
	public void run() {
		execute();
	}
}
