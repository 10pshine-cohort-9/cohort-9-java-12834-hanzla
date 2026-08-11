import { useMemo } from "react";
import { useNavigate } from "react-router-dom";
import {
    FaBell,
    FaUserCircle,
    FaSearch,
    FaSignOutAlt
} from "react-icons/fa";

const Navbar = () => {

    const navigate = useNavigate();

    const user = JSON.parse(localStorage.getItem("user")) || {};

    const currentDate = useMemo(() => {

        return new Date().toLocaleDateString("en-US", {
            weekday: "long",
            month: "long",
            day: "numeric",
            year: "numeric"
        });

    }, []);

    const handleLogout = () => {

        localStorage.removeItem("user");

        navigate("/login");

    };

    return (

        <header className="sticky top-0 z-40 backdrop-blur-xl bg-white/90 border-b border-slate-200 shadow-sm">

            <div className="flex justify-between items-center px-8 py-5">

                <div>

                    <h1 className="text-3xl font-bold text-slate-800">

                        Contact Management

                    </h1>

                    <p className="text-slate-500 text-sm mt-1">

                        {currentDate}

                    </p>

                </div>


                <div className="flex items-center gap-6">

                    <button className="relative hover:scale-110 transition">

                        <FaBell className="text-2xl text-slate-700 hover:text-blue-600" />

                        <span className="absolute -top-1 -right-1 h-3 w-3 rounded-full bg-red-500"></span>

                    </button>

                    <div className="flex items-center gap-3">

                        <div className="w-12 h-12 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex justify-center items-center text-white text-xl shadow-lg">

                            {

                                user.firstName

                                    ? user.firstName.charAt(0).toUpperCase()

                                    : <FaUserCircle />

                            }

                        </div>

                        <div>

                            <h3 className="font-semibold text-slate-800">

                                {

                                    user.firstName

                                        ? `${user.firstName} ${user.lastName}`

                                        : "Guest"

                                }

                            </h3>

                            <p className="text-sm text-slate-500">

                                Contact Manager

                            </p>

                        </div>

                    </div>

                    <button

                        onClick={handleLogout}

                        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-xl flex items-center gap-2 transition"

                    >

                        <FaSignOutAlt />

                        Logout

                    </button>

                </div>

            </div>

        </header>

    );

};

export default Navbar;