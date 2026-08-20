import api from "../api/axios";

const getCurrentUser = () => {

    let storedUser;

    try {

        storedUser = localStorage.getItem("user");

    } catch (error) {

        console.error(
            "Unable to access stored user session."
        );

        throw new Error("User is not logged in");

    }

    if (!storedUser) {

        throw new Error("User is not logged in");

    }

    try {

        const user = JSON.parse(storedUser);

        /*
         * Validate that the stored value is a
         * valid user object with a valid ID.
         */
        if (
            user === null ||
            typeof user !== "object" ||
            Array.isArray(user) ||
            typeof user.id !== "number"
        ) {

            localStorage.removeItem("user");

            throw new Error("Invalid user session");

        }

        return user;

    } catch (error) {

        /*
         * Remove malformed persisted user data.
         */
        try {

            localStorage.removeItem("user");

        } catch {
            // Ignore storage cleanup failure.
        }

        throw new Error("Invalid user session");

    }

};


const getDashboard = () => {

    /*
     * Validate the local user state before making
     * the authenticated dashboard request.
     *
     * Authentication itself is handled by the
     * server-side session cookie through Axios
     * withCredentials configuration.
     */
    getCurrentUser();

    return api.get("/dashboard");

};


export default {
    getDashboard
};