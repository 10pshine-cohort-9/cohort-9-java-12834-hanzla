import api from "../api/axios";


const getCurrentUser = () => {

    const user = localStorage.getItem("user");

    if (!user) {
        throw new Error("User is not logged in");
    }

    return JSON.parse(user);
};


const getDashboard = () => {

    return api.get("/dashboard");

};


export default {
    getDashboard
};