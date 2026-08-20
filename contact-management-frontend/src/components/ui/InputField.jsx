import React from "react";

const InputField = ({
    label,
    icon,
    type = "text",
    name,
    value,
    onChange,
    placeholder = "",
    disabled = false
}) => {

    return (

        <div>

            <label className="font-semibold text-slate-700">

                {label}

            </label>

            <div className="relative mt-2">

                {

                    icon &&

                    <span className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">

                        {icon}

                    </span>

                }

                <input

                    type={type}

                    name={name}

                    value={value}

                    onChange={onChange}

                    placeholder={placeholder}

                    disabled={disabled}

                    className="w-full border rounded-xl py-3 pr-4 pl-12 focus:ring-2 focus:ring-blue-500 outline-none"

                />

            </div>

        </div>

    );

};

export default InputField;