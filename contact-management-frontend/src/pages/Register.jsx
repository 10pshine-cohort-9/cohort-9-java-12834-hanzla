import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import {
    FaUser,
    FaEnvelope,
    FaPhone,
    FaLock,
    FaUserPlus
} from "react-icons/fa";
import { toast, ToastContainer } from "react-toastify";
import { useEffect } from "react";

import authService from "../services/authService";

const Register = () => {

    const navigate = useNavigate();
    useEffect(() => {

    if (localStorage.getItem("user")) {

        navigate("/dashboard");

    }

}, []);

    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState({

        firstName: "",

        lastName: "",

        email: "",

        phoneNumber: "",

        password: ""

    });

    const handleChange = (e) => {

        setFormData({

            ...formData,

            [e.target.name]: e.target.value

        });

    };

    const validateForm = () => {

        if (!formData.firstName.trim()) {

            toast.error("First Name is required");

            return false;

        }

        if (!formData.lastName.trim()) {

            toast.error("Last Name is required");

            return false;

        }

        if (!formData.email.trim()) {

            toast.error("Email is required");

            return false;

        }

        if (!formData.phoneNumber.trim()) {

            toast.error("Phone Number is required");

            return false;

        }

        if (!formData.password.trim()) {

            toast.error("Password is required");

            return false;

        }

        return true;

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!validateForm()) {

            return;

        }

        try {

            setLoading(true);

            await authService.register(formData);

toast.success("Registration Successful");

setTimeout(() => {

    navigate("/login");

}, 800);

        }

        catch (error) {

console.error(error);

toast.error(

    error.response?.data?.message ||

    "Registration Failed"

);

        }

        finally {

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

                className="bg-white/10 backdrop-blur-xl border border-white/20 rounded-3xl shadow-2xl w-full max-w-3xl p-10"

            >

                <div className="text-center mb-10">

                    <h1 className="text-4xl font-bold text-white">

                        Create Account

                    </h1>

                    <p className="text-gray-300 mt-2">

                        Join Contact Management System

                    </p>

                </div>

                <form
                    onSubmit={handleSubmit}
                    className="grid grid-cols-1 md:grid-cols-2 gap-6"
                >

                    <div>

                        <label className="text-white font-medium">

                            First Name

                        </label>

                        <div className="relative mt-2">

                            <FaUser className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"/>

                            <input

                                type="text"

                                name="firstName"

                                value={formData.firstName}

                                onChange={handleChange}

                                placeholder="First Name"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"

                            />

                        </div>

                    </div>

                    <div>

                        <label className="text-white font-medium">

                            Last Name

                        </label>

                        <div className="relative mt-2">

                            <FaUser className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"/>

                            <input

                                type="text"

                                name="lastName"

                                value={formData.lastName}

                                onChange={handleChange}

                                placeholder="Last Name"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"

                            />

                        </div>

                    </div>

                    <div>

                        <label className="text-white font-medium">

                            Email

                        </label>

                        <div className="relative mt-2">

                            <FaEnvelope className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"/>

                            <input

                                type="email"

                                name="email"

                                value={formData.email}

                                onChange={handleChange}

                                placeholder="Email"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"

                            />

                        </div>

                    </div>

                    <div>

                        <label className="text-white font-medium">

                            Phone Number

                        </label>

                        <div className="relative mt-2">

                            <FaPhone className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"/>

                            <input

                                type="text"

                                name="phoneNumber"

                                value={formData.phoneNumber}

                                onChange={handleChange}

                                placeholder="Phone Number"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"

                            />

                        </div>

                    </div>

                    <div className="md:col-span-2">

                        <label className="text-white font-medium">

                            Password

                        </label>

                        <div className="relative mt-2">

                            <FaLock className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"/>

                            <input

                                type="password"

                                name="password"

                                value={formData.password}

                                onChange={handleChange}

                                placeholder="Password"

                                className="w-full bg-white rounded-xl py-3 pl-12 pr-4 border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"

                            />

                        </div>

                    </div>

                    <div className="md:col-span-2 mt-4">

                        <button

                            type="submit"

                            disabled={loading}

                            className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-xl flex justify-center items-center gap-3 transition shadow-lg"

                        >

                            <FaUserPlus />

                            {

                                loading

                                    ? "Creating Account..."

                                    : "Register"

                            }

                        </button>

                    </div>

                </form>

                <div className="text-center mt-8">

                    <p className="text-gray-300">

                        Already have an account?

                    </p>

                    <Link

                        to="/login"

                        className="text-blue-300 hover:text-white font-semibold"

                    >

                        Login Here

                    </Link>

                </div>

            </motion.div>
            <ToastContainer
    position="top-right"
    autoClose={2500}
    theme="colored"
/>

        </div>

    );

};

export default Register;