import Navbar from "../components/common/Navbar";
import Sidebar from "../components/common/Sidebar";

const DashboardLayout = ({ children }) => {

    return (

        <div className="min-h-screen bg-slate-100">

            <Sidebar />

            <div className="ml-72">

                <Navbar />

                <main className="p-8">

                    {children}

                </main>

            </div>

        </div>

    );

};

export default DashboardLayout;