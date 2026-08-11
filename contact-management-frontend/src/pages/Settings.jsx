import { useState } from "react";
import {
    FaUserCircle,
    FaEnvelope,
    FaPhone,
    FaLock
} from "react-icons/fa";

import ChangePasswordModal from "../components/profile/ChangePasswordModal";

import DashboardLayout from "../layouts/DashboardLayout";


const Settings = () => {

    const user = JSON.parse(localStorage.getItem("user")) || {};
    const [showModal, setShowModal] = useState(false);

    return (

        <DashboardLayout>

            <div className="space-y-8">

                <div>

                    <h1 className="text-4xl font-bold text-slate-800">

                        Profile

                    </h1>

                    <p className="text-slate-500 mt-2">

                        View your profile information.

                    </p>

                </div>

                <div className="bg-white rounded-3xl shadow-lg p-8">

                    <div className="flex items-center gap-6">

                        <div className="w-24 h-24 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex items-center justify-center text-white text-5xl">

                            <FaUserCircle />

                        </div>

                        <div>

                            <h2 className="text-3xl font-bold">

                                {user.firstName} {user.lastName}

                            </h2>

                            <p className="text-slate-500">

                                Contact Management System User

                            </p>

                        </div>

                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-10">

                        <div>

                            <label className="font-semibold text-slate-700">

                                Email

                            </label>

                            <div className="mt-2 flex items-center gap-3 border rounded-xl p-4">

                                <FaEnvelope className="text-blue-600" />

                                <span>{user.email}</span>

                            </div>

                        </div>

                        <div>

                            <label className="font-semibold text-slate-700">

                                Phone Number

                            </label>

                            <div className="mt-2 flex items-center gap-3 border rounded-xl p-4">

                                <FaPhone className="text-green-600" />

                                <span>{user.phoneNumber}</span>

                            </div>

                        </div>

                    </div>

                    <div className="mt-10">

<button
    onClick={() => setShowModal(true)}
    className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-xl flex items-center gap-3 transition"
>
    <FaLock />
    Change Password
</button>

                    </div>

                </div>

            </div>
            {
    showModal && (

        <ChangePasswordModal

            user={user}

            onClose={() => setShowModal(false)}

        />

    )
}

        </DashboardLayout>

    );

};

export default Settings;