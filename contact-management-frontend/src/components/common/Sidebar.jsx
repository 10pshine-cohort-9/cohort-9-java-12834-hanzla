import {
    FaHome,
    FaAddressBook,
    FaStar,
    FaCog,
    FaSignOutAlt,
    FaUserFriends
} from "react-icons/fa";

import { NavLink, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import Swal from "sweetalert2";

import authService from "../../services/authService";

const Sidebar = () => {

    const navigate = useNavigate();

    const handleLogout = async () => {

        const result = await Swal.fire({
            title: "Logout",
            text: "Are you sure you want to logout?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#2563eb",
            cancelButtonColor: "#dc2626",
            confirmButtonText: "Logout"
        });

        if (!result.isConfirmed) {
            return;
        }

        try {

            // Get CSRF token before logout
            await authService.getCsrfToken();

            // Tell Spring Security to invalidate the session
            await authService.logout();

        } catch (error) {

            console.error("Logout request failed:", error);

        } finally {

            // Always clear local login information
            localStorage.removeItem("user");

            // Return to login page
            navigate("/login");
        }
    };

    const navClass = ({ isActive }) =>
        `flex items-center gap-4 px-6 py-4 rounded-2xl transition-all duration-300 font-medium ${
            isActive
                ? "bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg"
                : "text-slate-300 hover:bg-slate-800 hover:text-white"
        }`;

    return (

        <aside className="fixed left-0 top-0 h-screen w-72 bg-slate-900 border-r border-slate-800 flex flex-col justify-between">

            <div>

                <motion.div
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="h-28 flex flex-col justify-center items-center border-b border-slate-700"
                >

                    <div className="w-16 h-16 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex justify-center items-center text-white text-3xl shadow-xl">

                        <FaUserFriends />

                    </div>

                    <h1 className="text-2xl font-bold text-white mt-3">
                        Contact CMS
                    </h1>

                    <p className="text-slate-400 text-sm">
                        10Pearls Internship
                    </p>

                </motion.div>

                <nav className="p-5 space-y-3">

                    <NavLink
                        to="/dashboard"
                        className={navClass}
                    >
                        <FaHome />
                        Dashboard
                    </NavLink>

                    <NavLink
                        to="/contacts"
                        className={navClass}
                    >
                        <FaAddressBook />
                        Contacts
                    </NavLink>

                    <NavLink
                        to="/favorites"
                        className={navClass}
                    >
                        <FaStar />
                        Favorites
                    </NavLink>

                    <NavLink
                        to="/settings"
                        className={navClass}
                    >
                        <FaCog />
                        Profile
                    </NavLink>

                </nav>

            </div>

            <div className="p-5">

                <button
                    onClick={handleLogout}
                    className="w-full bg-red-600 hover:bg-red-700 text-white py-4 rounded-2xl flex justify-center items-center gap-3 transition shadow-lg"
                >

                    <FaSignOutAlt />

                    Logout

                </button>

            </div>

        </aside>
    );
};

export default Sidebar;