import { FaSearch, FaTimes } from "react-icons/fa";

const SearchBar = ({ keyword, setKeyword }) => {

    return (

        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-5">

            <div className="relative w-full md:w-[420px]">

                <FaSearch
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                />

                <input

                    type="text"

                    value={keyword}

                    onChange={(e) => setKeyword(e.target.value)}

                    placeholder="Search by name, email, phone or title..."

                    className="w-full pl-12 pr-12 py-3 rounded-xl border border-slate-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"

                />

                {

                    keyword &&

                    (

                        <button

                            onClick={() => setKeyword("")}

                            className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-500 hover:text-red-600 transition"

                        >

                            <FaTimes />

                        </button>

                    )

                }

            </div>

            <div className="text-sm text-slate-500">

                Search contacts instantly

            </div>

        </div>

    );

};

export default SearchBar;