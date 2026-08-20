import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import {
    FaEnvelope,
    FaLock,
    FaSignInAlt
} from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";

import authService from "../services/authService";

const Login = () => {

    const navigate = useNavigate();

    /*
     * If the user is already logged in,
     * redirect them to the dashboard.
     */
    useEffect(() => {

        if (localStorage.getItem("user")) {

            navigate("/dashboard");

        }

    }, [navigate]);


    const [loading, setLoading] = useState(false);


    const [loginData, setLoginData] = useState({

        username: "",

        password: ""

    });


    /*
     * Handle input changes.
     */
    const handleChange = (e) => {

        setLoginData({

            ...loginData,

            [e.target.name]: e.target.value

        });

    };


    /*
     * Basic frontend validation.
     */
    const validateForm = () => {

        if (!loginData.username.trim()) {

            toast.error(
                "Email or Phone Number is required"
            );

            return false;

        }


        if (!loginData.password.trim()) {

            toast.error(
                "Password is required"
            );

            return false;

        }


        return true;

    };


    /*
     * Login handler.
     */
    const handleSubmit = async (e) => {

        e.preventDefault();


        /*
         * Validate form before making API calls.
         */
        if (!validateForm()) {

            return;

        }


        try {

            setLoading(true);


            /*
             * First request the CSRF token.
             *
             * Spring Security creates the
             * XSRF-TOKEN cookie here.
             */
            await authService.getCsrfToken();


            /*
             * Now send the login request.
             *
             * Axios is configured to read the
             * XSRF-TOKEN cookie and send it as
             * X-XSRF-TOKEN.
             */
            const response =
                await authService.login(loginData);


            /*
             * Log only the HTTP status.
             *
             * Do not log the complete response because
             * it may contain user profile information.
             */
            console.log(
                "Login successful:",
                response.status
            );


            /*
             * Store the logged-in user's information.
             */
            localStorage.setItem(

                "user",

                JSON.stringify(response.data)

            );


            /*
             * Show success message.
             */
            toast.success(
                "Login Successful"
            );


            /*
             * Redirect to dashboard.
             */
            setTimeout(() => {

                navigate("/dashboard");

            }, 800);


        } catch (error) {

            /*
             * Log only a sanitized status.
             *
             * Do not log the complete Axios error because
             * error.config.data may contain the password.
             */
            console.error(
                "Login failed:",
                error.response?.status || "network error"
            );


            /*
             * Display backend error when available.
             */
            toast.error(

                error.response?.data?.message ||

                "Invalid Email or Password"

            );


        } finally {

            setLoading(false);

        }

    };


    return (

        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-800 flex justify-center items-center p-6">


            <motion.div

                initial={{
                    opacity: 0,
                    y: 40
                }}

                animate={{
                    opacity: 1,
                    y: 0
                }}

                transition={{
                    duration: 0.5
                }}

                className="bg-white/10 backdrop-blur-xl border border-white/20 rounded-3xl shadow-2xl w-full max-w-md p-10"

            >


                {/* Header */}

                <div className="text-center mb-10">

                    <h1 className="text-4xl font-bold text-white">

                        Welcome Back

                    </h1>


                    <p className="text-gray-300 mt-3">

                        Sign in to your account

                    </p>

                </div>


                {/* Login Form */}

                <form

                    onSubmit={handleSubmit}

                    className="space-y-6"

                >


                    {/* Username */}

                    <div>

                        <label className="text-white font-medium">

                            Email or Phone Number

                        </label>


                        <div className="relative mt-2">

                            <FaEnvelope

                                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"

                            />


                            <input

                                type="text"

                                name="username"

                                value={loginData.username}

                                onChange={handleChange}

                                placeholder="Email or Phone Number"

                                autoComplete="username"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-400 outline-none"

                            />

                        </div>

                    </div>


                    {/* Password */}

                    <div>

                        <label className="text-white font-medium">

                            Password

                        </label>


                        <div className="relative mt-2">

                            <FaLock

                                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"

                            />


                            <input

                                type="password"

                                name="password"

                                value={loginData.password}

                                onChange={handleChange}

                                placeholder="Enter your password"

                                autoComplete="current-password"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-400 outline-none"

                            />

                        </div>

                    </div>


                    {/* Login Button */}

                    <button

                        type="submit"

                        disabled={loading}

                        className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-60 disabled:cursor-not-allowed text-white py-3 rounded-xl flex justify-center items-center gap-3 transition shadow-lg"

                    >

                        <FaSignInAlt />


                        {

                            loading

                                ? "Signing In..."

                                : "Login"

                        }

                    </button>


                </form>


                {/* Register Link */}

                <div className="text-center mt-8">

                    <p className="text-gray-300">

                        Don't have an account?

                    </p>


                    <Link

                        to="/register"

                        className="text-blue-300 hover:text-white font-semibold"

                    >

                        Create Account

                    </Link>

                </div>


            </motion.div>


            {/* Toast Messages */}

            <ToastContainer

                position="top-right"

                autoClose={2500}

                theme="colored"

            />

        </div>

    );

};

export default Login;