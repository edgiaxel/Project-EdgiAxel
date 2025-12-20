package project.edgiaxel;

import java.io.*;
import java.util.Properties;

public class SessionManager {

    private static final String PROP_FILE = "session.properties";
    private static int currentUserId = -1;
    private static String currentUsername = null;

    public static boolean isUserLoggedIn() {
        if (currentUserId != -1) {
            return true;
        }
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROP_FILE)) {
            props.load(input);
            String idStr = props.getProperty("user_id");
            String userStr = props.getProperty("username");

            if (idStr != null && !idStr.isEmpty()) {
                currentUserId = Integer.parseInt(idStr);
                currentUsername = userStr;
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    public static void login(int userId, String username) {
        currentUserId = userId;
        currentUsername = username;
        saveSession();
    }

    public static void logout() {
        currentUserId = -1;
        currentUsername = null;
        File file = new File(PROP_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    private static void saveSession() {
        Properties props = new Properties();
        props.setProperty("user_id", String.valueOf(currentUserId));
        props.setProperty("username", currentUsername);
        try (OutputStream output = new FileOutputStream(PROP_FILE)) {
            props.store(output, "User Session");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
