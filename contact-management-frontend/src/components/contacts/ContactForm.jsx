import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { toast } from "react-toastify";
import {
    FaUser,
    FaEnvelope,
    FaPhone,
    FaBriefcase,
    FaTimes,
    FaSave
} from "react-icons/fa";

import contactService from "../../services/contactService";

const ContactForm = ({

    contact,

    onClose,

    onSuccess

}) => {

    const [formData, setFormData] = useState({

        firstName: "",

        lastName: "",

        title: "",

        email: "",

        phoneNumber: "",

        userId: 1

    });

    const [loading, setLoading] = useState(false);

    useEffect(() => {

        if (contact) {

            setFormData({

                firstName: contact.firstName,

                lastName: contact.lastName,

                title: contact.title,

                email: contact.email,

                phoneNumber: contact.phoneNumber,

                userId: 1

            });

        }

    }, [contact]);



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

        return true;

    };



    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!validateForm()) {

            return;

        }

        try {

            setLoading(true);

            if (contact) {

                await contactService.updateContact(

                    contact.id,

                    formData

                );

                toast.success("Contact Updated");

            }

            else {

                await contactService.createContact(

                    formData

                );

                toast.success("Contact Added");

            }

            onSuccess();

        }

        catch (error) {

            console.log(error);

            toast.error("Operation Failed");

        }

        finally {

            setLoading(false);

        }

    };
        return (

        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex justify-center items-center z-50">

            <motion.div

                initial={{ opacity: 0, scale: 0.85 }}

                animate={{ opacity: 1, scale: 1 }}

                exit={{ opacity: 0, scale: 0.85 }}

                transition={{ duration: 0.25 }}

                className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden"

            >

                <div className="bg-gradient-to-r from-blue-600 to-indigo-600 p-6 flex justify-between items-center">

                    <div>

                        <h2 className="text-3xl font-bold text-white">

                            {contact ? "Edit Contact" : "Add Contact"}

                        </h2>

                        <p className="text-blue-100 mt-1">

                            Fill in the contact details

                        </p>

                    </div>

                    <button

                        onClick={onClose}

                        className="text-white hover:text-red-200 transition"

                    >

                        <FaTimes size={24} />

                    </button>

                </div>

                <form

                    onSubmit={handleSubmit}

                    className="p-8"

                >

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

                        <div>

                            <label className="font-semibold text-slate-700">

                                First Name

                            </label>

                            <div className="relative mt-2">

                                <FaUser className="absolute left-4 top-4 text-slate-400"/>

                                <input

                                    type="text"

                                    name="firstName"

                                    value={formData.firstName}

                                    onChange={handleChange}

                                    className="w-full border rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500"

                                />

                            </div>

                        </div>

                        <div>

                            <label className="font-semibold text-slate-700">

                                Last Name

                            </label>

                            <div className="relative mt-2">

                                <FaUser className="absolute left-4 top-4 text-slate-400"/>

                                <input

                                    type="text"

                                    name="lastName"

                                    value={formData.lastName}

                                    onChange={handleChange}

                                    className="w-full border rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500"

                                />

                            </div>

                        </div>

                        <div>

                            <label className="font-semibold text-slate-700">

                                Title

                            </label>

                            <div className="relative mt-2">

                                <FaBriefcase className="absolute left-4 top-4 text-slate-400"/>

                                <input

                                    type="text"

                                    name="title"

                                    value={formData.title}

                                    onChange={handleChange}

                                    className="w-full border rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500"

                                />

                            </div>

                        </div>

                        <div>

                            <label className="font-semibold text-slate-700">

                                Email

                            </label>

                            <div className="relative mt-2">

                                <FaEnvelope className="absolute left-4 top-4 text-slate-400"/>

                                <input

                                    type="email"

                                    name="email"

                                    value={formData.email}

                                    onChange={handleChange}

                                    className="w-full border rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500"

                                />

                            </div>

                        </div>

                    </div>

                    <div className="mt-6">

                        <label className="font-semibold text-slate-700">

                            Phone Number

                        </label>

                        <div className="relative mt-2">

                            <FaPhone className="absolute left-4 top-4 text-slate-400"/>

                            <input

                                type="text"

                                name="phoneNumber"

                                value={formData.phoneNumber}

                                onChange={handleChange}

                                className="w-full border rounded-xl pl-12 pr-4 py-3 focus:ring-2 focus:ring-blue-500"

                            />

                        </div>

                    </div>

                    <div className="flex justify-end gap-4 mt-10">

                        <button

                            type="button"

                            onClick={onClose}

                            className="px-6 py-3 rounded-xl border hover:bg-slate-100 transition"

                        >

                            Cancel

                        </button>

                        <button

                            type="submit"

                            disabled={loading}

                            className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-3 rounded-xl flex items-center gap-3 transition shadow-lg"

                        >

                            <FaSave />

                            {

                                loading

                                    ? "Saving..."

                                    : contact

                                    ? "Update Contact"

                                    : "Save Contact"

                            }

                        </button>

                    </div>

                </form>

            </motion.div>

        </div>

    );

};

export default ContactForm;