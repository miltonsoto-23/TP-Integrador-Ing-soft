import React from 'react';

export default function Footer() {
    return (
        <footer className="fixed bottom-0 left-0 w-full bg-[#f1f5f9] border-t border-[#cbd5e1] p-[15px] z-50">
            <details className="group team-info">
                <summary className="cursor-pointer font-bold text-[#475569] list-none outline-none select-none">
                    Development team
                </summary>
                <div className="grid grid-cols-3 gap-5 mt-[15px]">
                <div>
                    <h4 className="text-[#1e293b] font-bold mb-1.5 border-b-2 border-[#cbd5e1] pb-0.5">
                        Frontend
                    </h4>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Gabriel Arrieta - gabriel.arrieta@mi.unc.edu.ar
                    </p>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Esteban Fernández Fassi - esteban.fernandez.fassi@mi.unc.edu.ar
                    </p>
                </div>
                <div>
                    <h4 className="text-[#1e293b] font-bold mb-1.5 border-b-2 border-[#cbd5e1] pb-0.5">
                        Backend
                    </h4>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Angela Marioni - angela.marioni@mi.unc.edu.ar
                    </p>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Maximiliano Brullo - mabrullo@mi.unc.edu.ar
                    </p>
                </div>
                <div>
                    <h4 className="text-[#1e293b] font-bold mb-1.5 border-b-2 border-[#cbd5e1] pb-0.5">
                        Firmware
                    </h4>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Federico Gaspar - federico.gaspar@mi.unc.edu.ar
                    </p>
                    <p className="text-[13px] text-[#64748b] mb-1">
                        Milton Soto - milton.soto@mi.unc.edu.ar
                    </p>
                </div>
            </div>
        </details>
    </footer>
    );
}