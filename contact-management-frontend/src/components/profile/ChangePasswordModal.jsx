import { useState } from "react";
import { toast } from "react-toastify";

import authService from "../../services/authService";

const ChangePasswordModal = ({ user, onClose }) => {

    const [loading, setLoading] = useState(false);

    const [data, setData] = useState({

        email: user.email,

        oldPassword: "",

        newPassword: "",

        confirmPassword: ""

    });

    const handleChange = (e) => {

        setData({

            ...data,

            [e.target.name]: e.target.value

        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            setLoading(true);

            await authService.changePassword(data);

            toast.success("Password Updated Successfully");

            onClose();

        }

        catch (error) {

            toast.error(

                error.response?.data?.message ||

                "Unable to change password"

            );

        }

        finally {

            setLoading(false);

        }

    };

    return (

        <div className="fixed inset-0 bg-black/40 flex justify-center items-center z-50">

            <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md p-8">

                <h2 className="text-3xl font-bold mb-8">

                    Change Password

                </h2>

                <form

                    onSubmit={handleSubmit}

                    className="space-y-5"

                >

                    <input

                        type="password"

                        name="oldPassword"

                        placeholder="Old Password"

                        value={data.oldPassword}

                        onChange={handleChange}

                        className="w-full border rounded-xl p-3"

                    />

                    <input

                        type="password"

                        name="newPassword"

                        placeholder="New Password"

                        value={data.newPassword}

                        onChange={handleChange}

                        className="w-full border rounded-xl p-3"

                    />

                    <input

                        type="password"

                        name="confirmPassword"

                        placeholder="Confirm Password"

                        value={data.confirmPassword}

                        onChange={handleChange}

                        className="w-full border rounded-xl p-3"

                    />

                    <div className="flex justify-end gap-4">

                        <button

                            type="button"

                            onClick={onClose}

                            className="border px-6 py-3 rounded-xl"

                        >

                            Cancel

                        </button>

                        <button

                            type="submit"

                            disabled={loading}

                            className="bg-blue-600 text-white px-6 py-3 rounded-xl"

                        >

                            {

                                loading

                                    ? "Updating..."

                                    : "Update Password"

                            }

                        </button>

                    </div>

                </form>

            </div>

        </div>

    );

};

export default ChangePasswordModal;