import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { FaAddressBook, FaStar } from "react-icons/fa6";
import { FaUserCircle, FaHistory } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../layouts/DashboardLayout";
import dashboardService from "../services/dashboardService";

import DashboardCards from "../components/dashboard/DashboardCards";
import RecentContacts from "../components/dashboard/RecentContacts";
import QuickActions from "../components/dashboard/QuickActions";

const Dashboard = () => {

    const navigate = useNavigate();

    const [refreshing, setRefreshing] = useState(false);

    const [dashboard, setDashboard] = useState({

        totalContacts: 0,
        favoriteContacts: 0,
        totalUsers: 0,
        recentContacts: []

    });

    useEffect(() => {

        loadDashboard();

    }, []);

    const loadDashboard = async (showMessage = false) => {

        try {

            setRefreshing(true);

            console.log("Refreshing Dashboard...");

const response = await dashboardService.getDashboard();

console.log(response.data);

            setDashboard(response.data);

            if (showMessage) {

                toast.success("Dashboard refreshed");

            }

        }

        catch (error) {

            console.error(error);

            toast.error("Unable to load dashboard");

        }

        finally {

            setRefreshing(false);

        }

    };

    const cards = [

        {
            title: "Total Contacts",
            value: dashboard.totalContacts,
            icon: <FaAddressBook />,
            color: "from-blue-600 to-indigo-600"
        },

        {
            title: "Favorite Contacts",
            value: dashboard.favoriteContacts,
            icon: <FaStar />,
            color: "from-yellow-500 to-orange-500"
        },

        {
            title: "Total Users",
            value: dashboard.totalUsers,
            icon: <FaUserCircle />,
            color: "from-green-500 to-emerald-600"
        },

        {
            title: "Recent Contacts",
            value: dashboard.recentContacts.length,
            icon: <FaHistory />,
            color: "from-purple-600 to-pink-600"
        }

    ];

return (

    <DashboardLayout>

        <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="space-y-8"
        >

            <div>

                <h1 className="text-4xl font-bold text-slate-800">

                    Welcome Back 👋

                </h1>

                <p className="text-slate-500 mt-2">

                    Here's what's happening today.

                </p>

            </div>

            <DashboardCards cards={cards} />

            <div className="grid grid-cols-1 xl:grid-cols-3 gap-8 items-start">

                <RecentContacts
                    recentContacts={dashboard.recentContacts}
                />

                <QuickActions
                    navigate={navigate}
                />

            </div>

        </motion.div>

    </DashboardLayout>

);

};

export default Dashboard;