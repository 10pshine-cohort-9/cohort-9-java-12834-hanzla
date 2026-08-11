import { motion } from "framer-motion";
import { FaEnvelope, FaPhone } from "react-icons/fa";

const RecentContacts = ({ recentContacts }) => {

    return (

        <motion.div

            initial={{
                opacity: 0,
                x: -40
            }}

            animate={{
                opacity: 1,
                x: 0
            }}

            className="xl:col-span-2 bg-white rounded-3xl shadow-lg p-8"

        >

            <div className="flex justify-between items-center mb-8">

                <h2 className="text-2xl font-bold text-slate-800">

                    Recent Contacts

                </h2>

                <span className="text-sm text-slate-500">

                    {recentContacts.length} Contact{recentContacts.length !== 1 ? "s" : ""}

                </span>

            </div>

            {

                recentContacts.length === 0 ?

                    (

                        <div className="py-20 flex flex-col items-center">

                            <div className="text-6xl">

                                📭

                            </div>

                            <h3 className="text-xl font-semibold text-slate-700 mt-5">

                                No Recent Contacts

                            </h3>

                            <p className="text-slate-500 mt-2">

                                Create your first contact to get started.

                            </p>

                        </div>

                    )

                    :

                    <div className="space-y-4">

                        {

                            recentContacts.map((contact) => (

                                <motion.div

                                    key={contact.id}

                                    whileHover={{
                                        y: -3,
                                        scale: 1.01
                                    }}

                                    className="flex justify-between items-center rounded-2xl p-5 hover:bg-slate-50 transition-all duration-300"

                                >

                                    <div className="flex items-center gap-4">

                                        <div className="w-14 h-14 rounded-full bg-gradient-to-r from-blue-600 to-indigo-600 flex justify-center items-center text-white font-bold text-xl shadow">

                                            {contact.firstName?.charAt(0).toUpperCase()}

                                        </div>

                                        <div>

                                            <h3 className="font-semibold text-slate-800 text-lg">

                                                {contact.firstName} {contact.lastName}

                                            </h3>

                                            <p className="text-slate-500">

                                                {contact.title || "No Title"}

                                            </p>

                                        </div>

                                    </div>

                                    <div className="text-right space-y-2">

                                        <div className="flex items-center justify-end gap-2 text-blue-600">

                                            <FaEnvelope />

                                            <span>

                                                {contact.email}

                                            </span>

                                        </div>

                                        <div className="flex items-center justify-end gap-2 text-slate-500">

                                            <FaPhone />

                                            <span>

                                                {contact.phoneNumber}

                                            </span>

                                        </div>

                                    </div>

                                </motion.div>

                            ))

                        }

                    </div>

            }

        </motion.div>

    );

};

export default RecentContacts;