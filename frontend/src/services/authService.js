// src/services/authService.js

const API_BASE_URL = 'http://localhost:8080/SocialMediaApp/api'; // You can also put this in an env variable

export const loginUser = async (username, password) => {
    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            credentials: 'include', 
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${username}&password=${password}`,
        });
        console.log(response);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || `Login failed: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        throw error;
    }
};

export const logoutUser = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/logout`, {
            method: 'GET',
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || `Logout Failed: ${response.status}`);
        }
    } catch (error) {
        throw error;
    }
    localStorage.removeItem('userId');
};

export const getCurrentUserId = () => {
    return localStorage.getItem('userId');
};

export const registerUser = async (username, password,email) => {
    try {
        const response = await fetch(`${API_BASE_URL}/register`, {
            method: 'POST',
            headers: {
                // 2. Set Content-Type to application/json
                'Content-Type': 'application/json',
                // Add other headers if needed, like 'Accept': 'application/json'
            },
            // 3. Stringify the credentials object for the body
            body: JSON.stringify({
                username, // Replace with actual values
                password,
                email,
            })
        });
        

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || `Registration failed: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        throw error;
    }
};

export const loginAdmin = async (username, password) => {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/login`, { // Corrected endpoint
            credentials: 'include', 
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${username}&password=${password}`,
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `Admin Login Failed: ${response.status}`); //Use message
        }

        const data = await response.json();
        return data;
    } catch (error) {
        throw error;
    }
};

export const logoutAdmin = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/logout`, {  // Added admin logout
            credentials: 'include',
            method: 'GET', //  usually logout is post.
        });
        if (!response.ok) {
             const errorData = await response.json();
             throw new Error(errorData.message || `Admin Logout Failed: ${response.status}`);
        }
    } catch (error) {
        throw error;
    }
    localStorage.removeItem('loggedInAdmin');
};

export const createAdmin = async (username, password) => {
      try {
        const response = await fetch(`${API_BASE_URL}/admin/createAdmin`, {
            credentials: 'include', 
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${username}&password=${password}`,
        });

        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || `Failed to create admin`);
        }
        return data;

    } catch(error){
        throw error;
    }
}