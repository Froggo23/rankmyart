# Google SSO Integration Guide

This guide explains how to set up and configure Google Single Sign-On (SSO) for the RankMyArt application.

## Overview

This implementation uses Google Identity Services (the new Google Sign-In) to allow users to log in with their Google accounts. The integration is client-side, using Google's JavaScript library to handle OAuth authentication.

## Prerequisites

1. A Google Cloud Platform (GCP) project
2. Google OAuth 2.0 credentials (Client ID)
3. PostgreSQL database with the updated schema (google_id field)

## Setup Instructions

### 1. Create Google OAuth 2.0 Credentials

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Navigate to "APIs & Services" > "Credentials"
4. Click "Create Credentials" > "OAuth client ID"
5. Select "Web application" as the application type
6. Configure the OAuth consent screen if prompted
7. Add authorized JavaScript origins:
   - `http://localhost:8080` (for development)
   - Your production domain (e.g., `https://rankmyart.com`)
8. Add authorized redirect URIs:
   - `http://localhost:8080` (for development)
   - Your production domain (e.g., `https://rankmyart.com`)
9. Click "Create" and copy the Client ID

### 2. Configure the Application

#### Update auth.js with Your Client ID

1. Open `src/main/resources/static/js/auth.js`
2. Find the line:
   ```javascript
   client_id: 'YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com'
   ```
3. Replace `YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com` with your actual Google Client ID

#### Database Schema

The User model has been updated to support Google SSO. Ensure your database schema includes:

```sql
ALTER TABLE users ADD COLUMN google_id VARCHAR(255) UNIQUE;
```

The `password` field is now nullable to support Google SSO users who don't have a traditional password.

### 3. How It Works

#### Backend Components

1. **UserRepository** (`domain/user/repository/UserRepository.java`)
   - Added `findByGoogleId(String googleId)` method
   - Updated `save()` method to include `google_id` field
   - Updated `findByUsername()` and `findByEmail()` to include `google_id` in SELECT queries

2. **UserService** (`domain/user/service/UserService.java`)
   - Added `findOrCreateGoogleUser(String googleId, String email, String username)` method
   - Handles user creation or retrieval for Google SSO users
   - Sets `password` to null for Google SSO users

3. **UserRestController** (`domain/user/controller/UserRestController.java`)
   - Added `/auth/google` POST endpoint
   - Receives Google user information (googleId, email, name)
   - Creates or retrieves user via `UserService`
   - Sets login cookie for session management

#### Frontend Components

1. **signup.html** (`resources/templates/signup.html`)
   - Includes Google Identity Services library
   - Contains a div with id `google-signin-button` where the Google button will be rendered
   - Shows "-- OR --" divider between traditional login and Google SSO

2. **auth.js** (`resources/static/js/auth.js`)
   - `initializeGoogleSignIn()`: Initializes Google Sign-In and renders the button
   - `handleGoogleCallback(response)`: Handles the OAuth callback from Google
   - `parseJwt(token)`: Parses the JWT token to extract user information
   - Sends Google user data to backend `/auth/google` endpoint

### 4. User Flow

1. User visits the signup/login page
2. User clicks "Sign in with Google" button
3. Google OAuth popup opens for authentication
4. User authenticates with Google
5. Google returns a JWT credential token
6. JavaScript parses the token to extract user information (sub/googleId, email, name)
7. Frontend sends this information to `/auth/google` endpoint
8. Backend:
   - Checks if user exists by Google ID
   - If not, checks if user exists by email (for account linking)
   - If neither exists, creates a new user with Google credentials
   - Sets login cookie with username
9. Frontend reloads the page, user is now logged in

### 5. Security Considerations

1. **JWT Validation**: The current implementation trusts the JWT token from Google. In a production environment, you should verify the token signature on the backend using Google's public keys.

2. **HTTPS**: Always use HTTPS in production to prevent man-in-the-middle attacks.

3. **CORS**: Ensure your CORS configuration allows requests from authorized origins only.

4. **Session Management**: The current implementation uses cookies for session management. Consider using more secure session management techniques (e.g., HttpOnly cookies, CSRF tokens).

### 6. Testing

1. Start the application:
   ```bash
   ./gradlew bootRun
   ```

2. Navigate to `http://localhost:8080/signup`

3. You should see:
   - Traditional login form at the top
   - "-- OR --" divider
   - Google Sign-In button below the divider

4. Click the Google Sign-In button and authenticate with your Google account

5. After successful authentication, you should be logged in and redirected to the homepage

### 7. Troubleshooting

#### Google Sign-In button doesn't appear
- Check browser console for errors
- Verify Google Identity Services library is loaded
- Ensure `initializeGoogleSignIn()` is being called on page load
- Check that the Client ID is correctly set in auth.js

#### "Google Identity Services library not loaded" error
- Ensure the script tag is present in signup.html: `<script src="https://accounts.google.com/gsi/client" async defer></script>`
- Check network tab to ensure the script is loading successfully

#### Login fails with 400 Bad Request
- Check that the `/auth/google` endpoint is receiving the correct data format
- Verify googleId and email are present in the request body
- Check backend logs for detailed error messages

#### User is created but login cookie is not set
- Verify the cookie path is set to "/"
- Check that the username is being correctly extracted from the User object
- Ensure cookies are enabled in the browser

### 8. Future Enhancements

1. **Account Linking**: Add UI to link existing accounts with Google SSO
2. **Profile Picture Sync**: Automatically sync profile picture from Google
3. **JWT Verification**: Add backend JWT token verification for enhanced security
4. **Multiple OAuth Providers**: Add support for Facebook, GitHub, etc.
5. **Two-Factor Authentication**: Add 2FA for additional security

## Additional Resources

- [Google Identity Services Documentation](https://developers.google.com/identity/gsi/web)
- [OAuth 2.0 Best Practices](https://oauth.net/2/)
- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)

## Support

For issues or questions, please open an issue on the GitHub repository.
